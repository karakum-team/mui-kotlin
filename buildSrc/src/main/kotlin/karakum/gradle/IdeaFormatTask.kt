package karakum.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Properties
import javax.inject.Inject

/**
 * Reformats [sourceDir] in place with IntelliJ IDEA's own formatter, run headless.
 *
 * Declares no inputs and no outputs, so it re-runs on every build — for the same reason
 * `generateDeclarations` declares none: that task rewrites the whole tree unconditionally, which leaves
 * everything downstream of it permanently stale. Every property here is therefore `@Internal`.
 */
abstract class IdeaFormatTask : DefaultTask() {

    /**
     * Gradle 9 removed `Project.exec` and `Project.javaexec`, so the formatter is forked through the
     * injected service instead — the only way a task action can start a process.
     */
    @get:Inject
    abstract val execOperations: ExecOperations

    /** Reformatted in place, recursively, `*.kt` only. */
    @get:Internal
    abstract val sourceDir: DirectoryProperty

    /**
     * The code style handed to the formatter with `-s`, which the usage text describes as taking effect
     * "regardless to the surrounding project settings" — including this repository's own `.idea`.
     *
     * Committed rather than read from the developer's IDE configuration: that lives under a version-named
     * path (`…/JetBrains/IntelliJIdea2026.2/`) which an IDE upgrade moves, and reading it would make the
     * generated tree depend on personal settings no one else has.
     */
    @get:Internal
    abstract val codeStyleFile: RegularFileProperty

    /** `idea.home` / `$IDEA_HOME`; absent means "look in the usual places" — see [findIdeaFormatScript]. */
    @get:Internal
    abstract val ideaHome: Property<String>

    /**
     * Config / system / plugins / log root handed to the spawned IDE through `IDEA_PROPERTIES`.
     *
     * Not optional. Without it the formatter tries to share the desktop IDE's configuration directory,
     * which is locked while that IDE runs, and it exits with "Only one instance of IDEA can be run at a
     * time" — so a build could not be started from the IDE that has the project open. Isolation has a
     * second, more valuable effect: the run cannot see third-party plugins or personal settings, so its
     * output is a function of the IDEA build and [codeStyleFile] alone.
     */
    @get:Internal
    abstract val ideaWorkDir: DirectoryProperty

    @TaskAction
    fun format() {
        val formatScript = findIdeaFormatScript(ideaHome.orNull)

        if (!formatScript.canExecute())
            throw GradleException(
                "Not executable: ${formatScript.absolutePath}\n" +
                    "`chmod +x` it, or reinstall IntelliJ IDEA."
            )

        val target = sourceDir.get().asFile
        val expected = target.walkTopDown().count { it.isFile && it.extension == "kt" }

        if (expected == 0)
            throw GradleException(
                "No Kotlin files below ${target.absolutePath} — did `generateDeclarations` run?"
            )

        val style = codeStyleFile.get().asFile

        if (!style.isFile)
            throw GradleException("No code style file at ${style.absolutePath}")

        logger.lifecycle("Formatting $expected Kotlin file(s) with ${ideaBuild(formatScript) ?: "IntelliJ IDEA"}")

        val console = ByteArrayOutputStream()

        val result = execOperations.exec {
            // No shell is involved — the arguments reach the process verbatim — so `*.kt` must be passed
            // unquoted, the way it would not be on a command line.
            //
            // `-allowDefaults` is deliberately absent. With `-s` it is redundant, and leaving it out gives
            // the better failure mode: should the style file ever stop being read, the formatter ignores
            // the files instead of quietly falling back to factory defaults, and the count check below
            // catches it.
            commandLine(
                formatScript.absolutePath,
                "-s", style.absolutePath,
                // Explicit, because the tree holds non-ASCII in KDoc — a U+00A0 in `CssTransition.kt` and
                // a degree sign in `PickersCalendarHeader.types.kt`.
                "-charset", "UTF-8",
                "-r",
                "-m", "*.kt",
                target.absolutePath,
            )
            environment("IDEA_PROPERTIES", ideaPropertiesFile().absolutePath)

            // Captured rather than inherited: the formatter prints a line per file, which would bury the
            // build log, and the summary has to be parsed anyway. Replayed at `--info`, and in full in the
            // failure message.
            standardOutput = console
            errorOutput = console

            // Checked by hand below, so that the failure carries the formatter's own output and log path.
            isIgnoreExitValue = true
        }

        val output = console.toString(Charsets.UTF_8)
        logger.info(output)

        // Cross-checked against the walk rather than trusted: the formatter reports what it scanned, and a
        // style it cannot read makes it skip files while still exiting 0. `scanned`, not `formatted` — a
        // run over an already formatted tree legitimately formats fewer files than it scans.
        val scanned = SCANNED.find(output)?.groupValues?.get(1)?.toIntOrNull()

        if (result.exitValue != 0 || scanned != expected)
            throw GradleException(
                """
                IntelliJ IDEA's formatter did not format the generated declarations.

                  exit code : ${result.exitValue}
                  scanned   : ${scanned ?: "not reported"} of $expected Kotlin file(s)
                  formatter : ${formatScript.absolutePath}
                  style     : ${style.absolutePath}
                  IDE log   : ${ideaWorkDir.get().asFile.resolve("log/idea.log").absolutePath}

                Formatter output:
                ${output.trim().prependIndent("  ")}
                """.trimIndent()
            )

        logger.lifecycle(FORMATTED.find(output)?.value ?: "$scanned file(s) scanned.")

        val commas = insertTrailingCommas(target)

        if (commas > 0) logger.lifecycle("$commas file(s) given a trailing comma the formatter cannot add.")
    }

    /**
     * Adds the trailing comma that the headless formatter leaves out, and returns how many files gained one.
     *
     * `format` honours `ALLOW_TRAILING_COMMA` when deciding how to wrap a list — it is why these lists are
     * chopped one element per line at all — but it never *inserts* the comma itself: in the IDE that is a
     * post-format cleanup step, and the command-line entry point does not run it. Without this, ⌥⌘L on the
     * 24 files holding such a list would add the comma and dirty a freshly built tree, which is the exact
     * failure this whole pipeline exists to prevent.
     *
     * Matched on layout rather than by parsing: the last element of a chopped list is an indented line that
     * does not already end in a comma, followed by the line that closes the list. Only `)`, `>` and `]` are
     * treated as closers — every construct they end (value and type argument lists, parameter lists,
     * annotation array literals) accepts a trailing comma, whereas a supertype list, which does not, is
     * closed by no bracket at all and so cannot match. A line opening a list is skipped, so an empty one
     * stays empty, and comment bodies are skipped so that a `*` continuation is never treated as an element.
     *
     * Should the shape ever be misjudged, `compileKotlinJs` runs immediately after and fails on the result.
     */
    private fun insertTrailingCommas(target: File): Int =
        target.walkTopDown()
            .filter { it.isFile && it.extension == "kt" }
            .count { file ->
                val lines = file.readText().lines()
                val patched = lines.toMutableList()
                var changed = false

                for (i in 0 until lines.size - 1) {
                    val line = lines[i].trimEnd()
                    val body = line.trimStart()

                    if (body.isEmpty() || line == lines[i].trimStart()) continue // not indented
                    if (line.endsWith(",")) continue
                    if (body.startsWith("*") || body.startsWith("//")) continue
                    if (line.last() in "(<[") continue // opens a list; leave an empty one empty

                    val closer = lines[i + 1].trimStart()

                    if (closer.isEmpty() || closer.first() !in ")>]") continue
                    // The closer may share the element's indentation (a chopped type parameter list puts
                    // `>` there) but never exceeds it — deeper means a continuation, not the end of a list.
                    if (indentOf(lines[i + 1]) > indentOf(lines[i])) continue

                    patched[i] = "$line,"
                    changed = true
                }

                if (changed) file.writeText(patched.joinToString("\n"))

                changed
            }

    private fun indentOf(line: String): Int = line.length - line.trimStart().length

    /**
     * Writes the `IDEA_PROPERTIES` file that points the spawned IDE away from the desktop one's
     * directories, creating each of them first — a missing `idea.config.path` parent chain is not created
     * by the IDE itself.
     */
    private fun ideaPropertiesFile(): File {
        val root = ideaWorkDir.get().asFile

        val redirects = Properties().apply {
            for ((key, dirName) in DIRECTORIES) {
                val dir = root.resolve(dirName)
                dir.mkdirs()
                setProperty(key, dir.absolutePath)
            }
        }

        // `Properties.store` rather than string concatenation: `:`, `=` and `\` are all legal in a macOS
        // path and all significant in a properties file, and a hand-built line holding one would be
        // truncated silently rather than rejected.
        return root.resolve("idea.properties").also { file ->
            file.outputStream().use { redirects.store(it, "Generated by $path") }
        }
    }

    private companion object {
        /**
         * An empty `idea.plugins.path` is the point rather than an oversight: the bundled plugins that do
         * the formatting load from the installation, and only user-installed ones would come from here.
         */
        val DIRECTORIES = listOf(
            "idea.config.path" to "config",
            "idea.system.path" to "system",
            "idea.plugins.path" to "plugins",
            "idea.log.path" to "log",
        )

        val SCANNED = Regex("""(\d+) file\(s\) scanned""")
        val FORMATTED = Regex("""\d+ file\(s\) formatted[^\n]*""")
    }
}
