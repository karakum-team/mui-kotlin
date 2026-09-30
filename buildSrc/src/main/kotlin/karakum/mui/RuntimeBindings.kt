package karakum.mui

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import java.io.File

/** One generation's provenance and runtime import requests. Never reads the committed Kotlin tree. */
internal class RuntimeBindings(
    private val nodeModulesDir: File,
    private val sourceDir: File,
) {
    private data class Output(val file: File, val text: String, val requests: List<Map<String, Any?>>)

    private val outputs = linkedMapOf<File, Output>()

    // Called at emission, while the original .d.ts is still known. In particular, classes must use
    // their own source file, not their component's source or its guessed module path.
    fun File.writeGenerated(text: String, source: File? = null) {
        writeText(text)
        outputs.remove(this)
        val module = MODULE.find(text)?.groupValues?.get(1)
        val declarations = runtimeDeclarations(text)
        check(module != null || declarations.isEmpty()) { "Runtime declarations without JsModule: $this" }
        if (module == null) return
        val pkg = Regex("(?m)^package ([\\w.]+)").find(text)!!.groupValues[1]
        val requests = declarations.map { declaration ->
            mapOf(
                "file" to relativeTo(sourceDir).invariantSeparatorsPath,
                "key" to "$pkg.${declaration.name}",
                "module" to module,
                "name" to declaration.name,
                "importName" to (declaration.jsName ?: declaration.name),
                "source" to source?.absolutePath,
                "members" to declaration.members,
            )
        }
        outputs[this] = Output(this, text, requests)
    }

    fun resolve(nodeExecutable: String) {
        val files = outputs.values.sortedBy { it.file.relativeTo(sourceDir).invariantSeparatorsPath }
        val requests = files.flatMap { it.requests }.mapIndexed { id, request -> request + ("id" to id) }
        val workDir = sourceDir.parentFile.resolve("runtime-bindings").also { it.mkdirs() }
        val script = workDir.resolve("runtime-exports.cjs")
        script.writeText(checkNotNull(javaClass.getResource("/karakum/mui/runtime-exports.cjs")).readText())
        val input = workDir.resolve("requests.json")
        input.writeText(JsonOutput.toJson(mapOf("nodeModulesDir" to nodeModulesDir.absolutePath, "requests" to requests)))
        val output = workDir.resolve("resolved.json")
        val log = workDir.resolve("resolution.log")
        val process = ProcessBuilder(nodeExecutable, script.absolutePath, input.absolutePath, output.absolutePath)
            .redirectErrorStream(true)
            .redirectOutput(log)
            .start()
        val exit = process.waitFor()
        print(log.readText())
        check(exit == 0) { "Runtime import resolution failed; declarations were not installed. See $input" }
        @Suppress("UNCHECKED_CAST")
        val resolved = JsonSlurper().parse(output) as List<Map<String, Any?>>
        check(resolved.size == requests.size) { "Incomplete runtime export resolution" }
        var offset = 0
        for (file in files) {
            val contents = renderRuntimeFiles(file.text, file.requests.map {
                val id = offset++
                val result = resolved[id]
                check((result["id"] as Number).toInt() == id)
                ResolvedRuntimeBinding(result["module"] as String?, result["name"] as String?)
            })
            file.file.writeText(contents.first())
            contents.drop(1).forEachIndexed { index, content ->
                val extra = file.file.resolveSibling("${file.file.nameWithoutExtension}.runtime${index + 1}.kt")
                check(!extra.exists()) { "Runtime module split would overwrite $extra" }
                extra.writeText(content)
            }
        }
        println("Verified ${requests.size} runtime bindings; report: $output")
    }
}

internal data class ResolvedRuntimeBinding(val module: String?, val name: String?)

internal data class RuntimeDeclaration(
    val start: Int,
    val end: Int,
    val name: String,
    val jsName: String?,
    val members: List<String>,
)

private val MODULE = Regex("""@file:JsModule\("([^"]+)"\)""")
private val RUNTIME = Regex("""(?m)^(?:@JsName\("([^"]+)"\)\n)?external (val|var|fun|object|class)[\t ]+""")

private fun runtimeName(text: String, start: Int): Pair<String, Int> {
    var cursor = start
    while (text[cursor].isWhitespace()) cursor++
    if (text[cursor] == '<') cursor = balancedEnd(text, cursor, '<', '>')
    while (text[cursor].isWhitespace()) cursor++
    val name = Regex("\\w+").find(text, cursor)
    check(name != null && name.range.first == cursor) { "Missing runtime name at $cursor" }
    return name.value to (name.range.last + 1)
}

/** Extract only the generator's top-level runtime templates, retaining exact spans and signatures. */
internal fun runtimeDeclarations(text: String): List<RuntimeDeclaration> = RUNTIME.findAll(text).map { match ->
    val kind = match.groupValues[2]
    val (name, afterName) = runtimeName(text, match.range.last + 1)
    val end = when (kind) {
        "object", "class" -> balancedEnd(text, text.indexOf('{', afterName), '{', '}')
        "fun" -> {
            val close = balancedEnd(text, text.indexOf('(', afterName), '(', ')')
            text.indexOf("\n\n", close).takeIf { it >= 0 } ?: text.length
        }
        else -> text.indexOf('\n', afterName).takeIf { it >= 0 } ?: text.length
    }
    val members = if (kind == "object") {
        val body = text.substring(afterName, end)
        Regex("""(?m)^[\t ]*(?:val|fun)[\t ]+""")
            .findAll(body).map { runtimeName(body, it.range.last + 1).first }.toList()
    } else emptyList()
    RuntimeDeclaration(match.range.first, end, name, match.groupValues[1].ifEmpty { null }, members)
}.toList()

private fun balancedEnd(text: String, start: Int, open: Char, close: Char): Int {
    check(start >= 0) { "Malformed runtime declaration" }
    // Kotlin templates can contain comments and quoted JS names; neither contributes delimiters.
    val tokens = Regex("""/\*.*?\*/|//[^\n]*|"(?:\\.|[^"\\])*"|->|[${Regex.escape("$open$close")}]""", RegexOption.DOT_MATCHES_ALL)
    var depth = 0
    for (token in tokens.findAll(text, start)) {
        if (token.value == open.toString()) depth++
        if (token.value == close.toString() && --depth == 0) return token.range.last + 1
    }
    error("Unclosed runtime declaration at $start")
}

private fun declarationStart(text: String, declaration: RuntimeDeclaration): Int {
    // Include only the immediately preceding KDoc, never the documentation of a retained type.
    val prefix = text.substring(0, declaration.start).trimEnd()
    if (prefix.endsWith("*/")) {
        val doc = prefix.lastIndexOf("/**")
        if (doc >= 0 && !prefix.substring(doc).contains("*/\n")) return doc
    }
    return declaration.start
}

internal fun renderRuntimeFiles(text: String, bindings: List<ResolvedRuntimeBinding>): List<String> {
    val declarations = runtimeDeclarations(text)
    check(declarations.size == bindings.size)
    val modules = bindings.mapNotNull { it.module }.distinct()
    if (modules.size <= 1) return listOf(renderRuntimeBindings(text, bindings))

    // Declaration-level @JsModule always imports a default in Kotlin/JS, even with @JsName.
    // Keep types and the first module in the original file, and split the other values into files
    // with their own file-level annotation. Package, imports and file-level suppressions travel too.
    val headerEnd = Regex("(?m)^(?:package [\\w.]+|import [^\\n]+)\\n").findAll(text).last().range.last + 1
    val header = text.substring(0, headerEnd)
    return listOf(renderRuntimeBindings(text, bindings.map {
        if (it.module == modules.first()) it else ResolvedRuntimeBinding(null, null)
    })) + modules.drop(1).map { module ->
        val selected = declarations.zip(bindings).filter { it.second.module == module }
        val body = selected.joinToString("\n\n") { (declaration, _) ->
            text.substring(declarationStart(text, declaration), declaration.end)
        }
        renderRuntimeBindings("$header\n$body\n", selected.map { it.second })
    }
}

internal fun renderRuntimeBindings(text: String, bindings: List<ResolvedRuntimeBinding>): String {
    val declarations = runtimeDeclarations(text)
    check(declarations.size == bindings.size)
    val originalModule = MODULE.find(text)?.groupValues?.get(1)
    if (declarations.isNotEmpty() && declarations.zip(bindings).all { (declaration, binding) ->
            binding.module == originalModule && binding.name == (declaration.jsName ?: declaration.name)
        }) return text
    val modules = bindings.mapNotNull { it.module }.distinct()
    check(modules.size <= 1) { "Different JS modules must be emitted into separate Kotlin files" }
    var result = text
    for ((declaration, binding) in declarations.zip(bindings).asReversed()) {
        var start = declaration.start
        val replacement = if (binding.module == null) {
            start = declarationStart(text, declaration)
            ""
        } else {
            val body = text.substring(declaration.start, declaration.end)
                .removePrefix(declaration.jsName?.let { "@JsName(\"$it\")\n" } ?: "")
            buildString {
                if (binding.name != declaration.name) append("@JsName(\"${binding.name}\")\n")
                append(body)
            }
        }
        result = result.replaceRange(start, declaration.end, replacement)
    }
    result = if (modules.size == 1) MODULE.replace(result, "@file:JsModule(\"${modules.single()}\")")
    else result.replace(Regex("""@file:JsModule\("[^"]+"\)\n\n"""), "")
    return result.trimEnd() + "\n"
}
