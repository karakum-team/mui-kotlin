package com.github.turansky.materialui

import java.io.File

internal fun convertDefinitions(
    definitionFile: File,
): String {
    val name = definitionFile.name.substringBefore(".")

    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val declarations = mutableListOf<String>()

    val propsName = "${name}Props"
    val propsContent = content.substringAfter("export interface $propsName", "")
    if (propsContent.isNotEmpty()) {
        val membersContent = propsContent
            .substringAfter("{\n")
            .substringBefore(";\n}")

        val props = "external interface $propsName: react.RProps {\n" +
                convertMembers(membersContent) +
                "\n}"

        declarations.add(props)
    }

    val componentDeclaration = "export default function $name(props: $propsName): JSX.Element;"
    if (componentDeclaration in content) {
        val comment = content.substringBefore("\n$componentDeclaration")
            .substringAfterLast("\n\n")

        declarations.add("$comment\n@JsName(\"default\")\nexternal val $name: react.FC<$propsName>")
    }

    return declarations.joinToString("\n\n")
}

private fun convertMembers(
    source: String,
): String {
    return source
        .replace(";\n    ", "??11??")
        .replace(";\n  })", "??22??")
        .replace(";\n  }[", "??33??")
        .splitToSequence(";\n")
        .map { it.replace("??11??", ";\n    ") }
        .map { it.replace("??22??", ";\n  })") }
        .map { it.replace("??33??", ";\n  }[") }
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
        declaration = "// " + declaration
    }
    return declaration
}

private fun kotlinName(name: String): String =
    if (name == "in" || name.startsWith("'")) {
        "`${name.removeSurrounding("'")}`"
    } else name
