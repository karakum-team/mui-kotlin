package com.github.turansky.mui

import java.io.File

internal data class ConversionResult(
    val main: String,
    val extensions: String,
)

internal fun convertClasses(
    classesName: String,
    definitionFile: File,
): String {
    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val classes = content.substringAfter("export interface $classesName {\n")
        .substringBefore("\n}\n")
        .trimIndent()
        .splitToSequence("\n")
        .map {
            val name = it.removeSuffix(": string;")
            if (name == it) return@map it
            val line = "var $name: String"
            if (name.startsWith("'")) "    // $line" else line
        }
        .joinToString("\n")

    return "external interface $classesName {\n" +
            "$classes\n" +
            "}\n"
}

internal fun convertDefinitions(
    definitionFile: File,
): ConversionResult {
    val name = definitionFile.name.substringBefore(".")

    val content = definitionFile.readText()
        .replace("\r\n", "\n")

    val declarations = mutableListOf<String>()

    val propsName = "${name}Props"

    findProps(name, propsName, content)
        ?.also(declarations::add)

    findMapProps(name, propsName, content)
        ?.also(declarations::add)

    declarations += findAdditionalProps(propsName, content)

    val funDeclaration = "export default function $name(props: $propsName): JSX.Element;"
    val typeDeclaration = "declare const $name: React.ComponentType<$propsName>;"
    val constDeclaration = "declare const $name: "

    declarations += listOfNotNull(
        findComponent(name, propsName, funDeclaration, content),
        findComponent(name, propsName, typeDeclaration, content, "ComponentType"),
        findComponent(name, propsName, constDeclaration, content),
    ).take(1)

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

private fun findProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    if (name == "TextField")
        return props(propsName)

    val propsContent = sequenceOf(" ", "<", "\n")
        .map { content.substringAfter("export interface $propsName$it", "") }
        .singleOrNull { it.isNotEmpty() }
        ?: return null

    val membersContent = propsContent
        .substringAfter("{\n")
        .substringBefore(";\n}")

    val body = convertMembers(membersContent)
    return props(propsName, CHILDREN in body) + " {\n" +
            body +
            "\n}"
}

private fun findMapProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    val propsContent = sequenceOf(
        content.substringAfter("export interface ${name}TypeMap<", "")
            .substringBefore("\n}\n"),
        content.substringAfter("export type ${name}TypeMap<", "")
            .substringBefore("\n}>;\n"),
    ).firstOrNull { it.isNotEmpty() }
        ?: return null

    val membersContent = propsContent
        .substringAfter("props: P", "")
        .substringAfter(" & {\n", "")
        .let { str ->
            sequenceOf(
                str.substringBefore(";\n    };", ""),
                str.substringBefore(";\n  };", "")
            ).maxByOrNull { it.length }!!
        }

    return if (membersContent.isNotEmpty()) {
        val body = convertMembers(membersContent)
        props(propsName, CHILDREN in body) + " {\n" +
                body +
                "\n}"
    } else {
        props(propsName)
    }
}

private fun findAdditionalProps(
    propsName: String,
    content: String,
): List<String> {
    val bodies = content.splitToSequence("export interface ")
        .drop(1)
        .toList()

    if (bodies.isEmpty())
        return emptyList()

    return bodies.mapNotNull { body ->
        val interfaceName = body
            .substringBefore(" ")
            .substringBefore("\n")
            .substringBefore("<")

        val propsLike = interfaceName.endsWith("Props")
        if (propsLike && interfaceName == propsName)
            return@mapNotNull null

        if (!propsLike && !interfaceName.endsWith("Origin") && !interfaceName.endsWith("Position"))
            return@mapNotNull null

        val membersContent = if (interfaceName != "InputBaseComponentProps") {
            body.substringAfter("{\n")
                .substringBefore(";\n}\n")
        } else ""

        val propsBody = convertMembers(membersContent)
        val declaration = if (propsLike) {
            props(interfaceName, CHILDREN in propsBody)
        } else {
            "external interface $interfaceName"
        }

        declaration + " {\n" +
                propsBody +
                "\n}"
    }
}

private fun props(
    propsName: String,
    hasChildren: Boolean = false,
): String =
    "external interface $propsName: " +
            if (hasChildren) "react.PropsWithChildren" else "react.Props"

private fun findComponent(
    name: String,
    propsName: String,
    declaration: String,
    content: String,
    type: String = "FC",
): String? {
    if (declaration !in content)
        return null

    var comment = content.substringBefore("\n$declaration")
        .substringAfterLast("\n\n")

    if (comment.startsWith("export "))
        comment = comment
            .substringAfterLast(";\n")
            .substringAfterLast("}\n")

    return "$comment\n" +
            "@JsName(\"default\")\n" +
            "external val $name: react.$type<$propsName>"
}

private fun convertMembers(
    source: String,
): String {
    if (source.isEmpty())
        return ""

    return source
        .replaceIndent("  ")
        .replace(";\n    ", "??11??")
        .replace(";\n  }", "??12??")
        .replace(";\n  })", "??22??")
        .replace(";\n  }[", "??33??")
        .replace(";\n   * ", "??44??")
        .splitToSequence(";\n")
        .map { it.replace("??11??", ";\n    ") }
        .map { it.replace("??12??", ";\n  }") }
        .map { it.replace("??22??", ";\n  })") }
        .map { it.replace("??33??", ";\n  }[") }
        .map { it.replace("??44??", ";\n   * ") }
        .map { it.trimIndent() }
        .map { convertMember(it) }
        .joinToString("\n\n")
}

private const val REACT_IS_STRICTER = "// @types/react is stricter"

private fun convertMember(
    source: String,
): String {
    val delimiter = if (source.startsWith(REACT_IS_STRICTER) || !source.startsWith("// ")) "*/\n" else "\n"

    return source.splitToSequence(delimiter)
        .map {
            when {
                it.startsWith(REACT_IS_STRICTER) -> "    $it*/"
                it.startsWith("/**") -> "$it*/"

                it.startsWith("//") -> it

                else -> convertProperty(it)
            }
        }
        .joinToString("\n")
}

private const val CHILDREN = "override var children: Array<out react.ReactNode>?"

private fun convertProperty(
    source: String,
): String {
    val name = source.substringBefore(":")
        .removeSuffix("?")
        .let { kotlinName(it) }

    val type = kotlinType(source.substringAfter(":").removePrefix(" "))

    if (name == "children" && type == "react.ReactNode")
        return CHILDREN

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
