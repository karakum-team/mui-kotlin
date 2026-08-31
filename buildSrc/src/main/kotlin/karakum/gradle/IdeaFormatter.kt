package karakum.gradle

import org.gradle.api.GradleException
import java.io.File

/**
 * Locating the IntelliJ IDEA installation whose formatter lays out the generated declarations.
 *
 * Kept apart from [IdeaFormatTask] so that the same resolution — and the same error messages — can run
 * before `generateDeclarations` deletes the source tree, rather than only when the formatter is reached.
 */

/**
 * Where an installation is looked for when none was named. JetBrains Toolbox installs into
 * `~/Applications`; a manual download or a shared machine puts it in `/Applications`.
 *
 * Toolbox's older `~/Library/Application Support/JetBrains/Toolbox/apps/…/<build>` layout is deliberately
 * not scanned: it nests the build number, so an update would silently move the formatter out from under
 * the build. Name such an install with `idea.home` instead.
 */
private fun candidateRoots(): List<File> =
    listOf(
        File(System.getProperty("user.home"), "Applications"),
        File("/Applications"),
    )

/** `format.sh` below [home], for the macOS application bundle and for a plain installation directory. */
private fun formatScriptIn(home: File): File? =
    sequenceOf(
        home.resolve("Contents/bin/format.sh"), // macOS `IntelliJ IDEA.app`
        home.resolve("bin/format.sh"),          // Linux / Windows installation root
    ).firstOrNull(File::isFile)

/**
 * The installation's build number (`IU-262.9437.185`), or `null` when it carries none.
 *
 * Logged on every run, because the formatter's output is a function of the IDEA build and of nothing that
 * this repository pins. An update that changes one wrapping rule shows up as churn across the generated
 * tree, and the build number is the only thing that makes that churn explainable rather than mysterious.
 */
internal fun ideaBuild(formatScript: File): String? =
    sequenceOf(
        formatScript.resolveSibling("../Resources/build.txt"), // macOS
        formatScript.resolveSibling("../build.txt"),           // Linux / Windows
    ).firstOrNull(File::isFile)
        ?.readText()
        ?.trim()

private fun expandHome(path: String): File =
    File(if (path.startsWith("~/")) System.getProperty("user.home") + path.drop(1) else path)

private val HOW_TO_NAME_ONE = """
        echo 'idea.home=/path/to/IntelliJ IDEA.app' >> ~/.gradle/gradle.properties

    or, for a single build:

        ./gradlew build -Pidea.home='/path/to/IntelliJ IDEA.app'
""".trimIndent()

/**
 * The `format.sh` of the IntelliJ IDEA that reformats the generated declarations.
 *
 * [hint] is `idea.home` or `$IDEA_HOME` when either is set. With neither, [candidateRoots] are scanned and
 * *exactly one* installation must be found: two of them may format differently, and choosing the wrong one
 * would surface only as unexplained churn in `src/jsMain/kotlin`, so ambiguity fails rather than guesses.
 */
fun findIdeaFormatScript(hint: String?): File {
    val named = hint?.trim()?.takeIf(String::isNotEmpty)

    if (named != null) {
        val home = expandHome(named)

        return formatScriptIn(home)
            ?: throw GradleException(
                """
                No IntelliJ IDEA formatter below `idea.home` = ${home.absolutePath}

                Expected `Contents/bin/format.sh` (macOS `IntelliJ IDEA.app`) or `bin/format.sh` (an
                installation directory) under it. Point `idea.home` at the application itself:

                    idea.home=~/Applications/IntelliJ IDEA.app
                """.trimIndent()
            )
    }

    val found = candidateRoots()
        .flatMap { root -> root.listFiles().orEmpty().asIterable() }
        .filter { it.name.startsWith("IntelliJ IDEA") && it.name.endsWith(".app") }
        .sortedBy(File::getName)
        .mapNotNull { app -> formatScriptIn(app)?.let { app to it } }

    return when (found.size) {
        1 -> found.single().second

        0 -> throw GradleException(
            """
            No IntelliJ IDEA installation found — the generated declarations cannot be formatted.

            `mui-kotlin/src/jsMain/kotlin` is generated output, laid out by the IDE's own formatter, so a
            local IntelliJ IDEA is a build requirement. Looked for `IntelliJ IDEA*.app` in:
            ${candidateRoots().joinToString("\n") { "    ${it.absolutePath}" }}

            Install IntelliJ IDEA, or name one:

            $HOW_TO_NAME_ONE
            """.trimIndent()
        )

        else -> throw GradleException(
            """
            Found ${found.size} IntelliJ IDEA installations and will not choose between them: they may
            format differently, and the wrong one shows up only as churn in the generated tree.

            ${found.joinToString("\n") { (app, script) -> "    ${ideaBuild(script) ?: "?"}  ${app.absolutePath}" }}

            Name the one to use:

            $HOW_TO_NAME_ONE
            """.trimIndent()
        )
    }
}
