package com.github.turansky.materialui

import java.io.File

internal data class ConversionResult(
    val main: String,
    val extensions: String,
)

internal fun convertDefinitions(
    definitionFile: File,
): ConversionResult {
    val name = definitionFile.name.substringBefore(".")

    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val declarations = mutableListOf<String>()

    val propsName = "${name}Props"
    val propsContent = content.substringAfter("export interface $propsName", "")
    if (propsContent.isNotEmpty() && name != "StepConnector") {
        val membersContent = propsContent
            .substringAfter("{\n")
            .substringBefore(";\n}")

        val props = "external interface $propsName: react.RProps {\n" +
                convertMembers(membersContent) +
                "\n}"

        declarations.add(props)
    }

    if (name == "StepConnector" || name == "TextField")
        declarations.add("external interface $propsName: react.RProps")

    val componentDeclaration = "export default function $name(props: $propsName): JSX.Element;"
    if (componentDeclaration in content) {
        var comment = content.substringBefore("\n$componentDeclaration")
            .substringAfterLast("\n\n")

        if (comment.startsWith("export "))
            comment = comment.substringAfter(";\n")

        declarations.add("$comment\n@JsName(\"default\")\nexternal val $name: react.FC<$propsName>")
    }

    val enums = content.splitToSequence("export type ")
        .drop(1)
        .map { it.substringBefore(";") }
        .mapNotNull { convertUnion(it) }
        .toList()

    return ConversionResult(
        main = declarations.joinToString("\n\n"),
        extensions = enums.joinToString("\n\n"),
    )
}

private fun convertMembers(
    source: String,
): String {
    return source
        .replace(";\n    ", "??11??")
        .replace(";\n  })", "??22??")
        .replace(";\n  }[", "??33??")
        .replace(";\n   * ", "??44??")
        .splitToSequence(";\n")
        .map { it.replace("??11??", ";\n    ") }
        .map { it.replace("??22??", ";\n  })") }
        .map { it.replace("??33??", ";\n  }[") }
        .map { it.replace("??44??", ";\n   * ") }
        .map { it.trimIndent() }
        .map { convertMember(it) }
        .joinToString("\n\n")
}

private fun convertMember(
    source: String,
): String {
    val delimiter = if (source.startsWith("// ")) "\n" else "*/\n"

    return source.splitToSequence(delimiter)
        .map {
            when {
                it.startsWith("//") -> it
                it.startsWith("/**") -> "$it*/"
                else -> convertProperty(it)
            }
        }
        .joinToString("\n")
}

private fun convertProperty(
    source: String,
): String {
    val name = source.substringBefore(":")
        .removeSuffix("?")
        .let { kotlinName(it) }

    val type = kotlinType(source.substringAfter(":").removePrefix(" "))

    var declaration = "var $name: $type"
    if ("-" in name) {
        declaration = "    // " + declaration
    }
    return declaration
}

private fun convertUnion(
    source: String,
): String? {
    val name = source.substringBefore(" =")
    val body = source.substringAfter(" =")
        .removePrefix(" ")
        .removePrefix("\n  | ")

    if ((!body.startsWith("'") || !body.endsWith("'")) && body.substringAfterLast("| ").toIntOrNull() == null)
        return null

    val values = body.splitToSequence(" | ", "\n  | ")
        .map { it.removeSurrounding("'") }
        .toList()

    val uppercase = values.any { "-" in it }
    val jsName = values.asSequence()
        .map { "${enumConstant(it, uppercase)}: ${it.toIntOrNull() ?: "'$it'"}" }
        .joinToString(", ", "@JsName(\"\"\"({", "})\"\"\")")

    val constantNames = values.asSequence()
        .map { "${enumConstant(it, uppercase)},\n" }
        .joinToString("")

    return """
        @Suppress("NAME_CONTAINS_ILLEGAL_CHARS")
        // language=JavaScript
        $jsName
        external enum class $name {
            $constantNames
            ;
        }
    """.trimIndent()
}

private fun kotlinName(name: String): String =
    if (name == "in" || name.startsWith("'")) {
        "`${name.removeSurrounding("'")}`"
    } else name

private fun enumConstant(
    value: String,
    uppercase: Boolean,
): String =
    when {
        value.toIntOrNull() != null
        -> "s$value"

        uppercase
        -> value.replace("-", "_")
            .toUpperCase()

        else -> value.removePrefix("@")
    }
