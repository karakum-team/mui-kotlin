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
    return source.splitToSequence(";\n")
        .joinToString("\n\n")
}
