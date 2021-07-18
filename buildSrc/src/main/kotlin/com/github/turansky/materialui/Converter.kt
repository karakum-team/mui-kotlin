package com.github.turansky.materialui

import java.io.File

internal fun convertDefinitions(
    definitionFile: File,
): String {
    val name = definitionFile.name.substringBefore(".")

    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val propsType = "${name}Props"
    val propsContent = content.substringAfter("export interface $propsType", "")
    val props = if (propsContent.isNotEmpty() && propsContent[0] != '\n') {
        val membersContent = propsContent
            .substringAfter("{\n")
            .substringBefore(";\n}")

        "external interface $propsType: react.RProps {\n" +
                convertMembers(membersContent) +
                "\n}"
    } else ""

    return props
}

private fun convertMembers(
    source: String,
): String {
    return source
        .replace(";\n    ", "??--??")
        .splitToSequence(";\n")
        .map { it.replace("??--??", ";\n    ") }
        .map { it.trimIndent() }
        .map { convertMember(it) }
        .joinToString("\n\n")
}

private fun convertMember(
    source: String,
): String {
    return source.splitToSequence("*/\n")
        .map { if (it.startsWith("/**")) "$it*/" else convertProperty(it) }
        .joinToString("\n")
}

private fun convertProperty(
    source: String,
): String {
    val name = source.substringBefore(": ")
        .removeSuffix("?")

    val type = source.substringAfter(": ")

    return "var $name: $type"
}
