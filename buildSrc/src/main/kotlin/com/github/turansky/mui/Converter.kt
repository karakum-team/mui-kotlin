package com.github.turansky.mui

import java.io.File

private const val UNION_MARKER = """/*union*/"""

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
        .removeInlineClasses()

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

private fun String.removeInlineClasses(): String =
    removeInlineClasses("  classes: ")
        .removeInlineClasses("  classes?: ")

private fun String.removeInlineClasses(
    trigger: String,
): String {
    if (trigger !in this)
        return this

    val parts = split(trigger)
    if (parts.size != 2)
        return this

    val (s, e) = parts

    val type = when {
        e.startsWith("Partial<ButtonClasses> & {")
        -> "mui.material.ButtonClasses"

        e.startsWith("{")
        -> "unknown"

        else -> return this
    }

    return s + "  classes?: $type;" + e.substringAfter("};")
}

private fun findProps(
    name: String,
    propsName: String,
    content: String,
): String? {
    when (name) {
        "TextField",
        "TreeView",
        -> return props(propsName)
    }

    val propsContent = sequenceOf(" ", "<", "\n")
        .map { content.substringAfter("export interface $propsName$it", "") }
        .singleOrNull { it.isNotEmpty() }
        ?: return null

    var parentType: String? = null
    if (" extends " in content) {
        val parentSource = content
            .substringAfter(" extends ")
            .substringBefore(" {\n")

        if (parentSource.startsWith("StandardProps<")) {
            parentType = sequenceOf(
                "mui.system.StandardProps",
                parentSource
                    .removeSurrounding("StandardProps<", ">")
                    .substringBefore(",")
                    .removeSurrounding("Partial<", ">")
                    .replace("React.HTMLAttributes<", "react.dom.html.HTMLAttributes<")
                    .replace("React.LabelHTMLAttributes<", "react.dom.html.LabelHTMLAttributes<")
                    .replace("<HTMLElement>", "<org.w3c.dom.HTMLElement>")
                    .replace("<HTMLDivElement>", "<org.w3c.dom.HTMLDivElement>")
                    .replace("<HTMLSpanElement>", "<org.w3c.dom.HTMLSpanElement>")
                    .replace("<HTMLLIElement>", "<org.w3c.dom.HTMLLIElement>")
                    .replace("<HTMLHeadingElement>", "<org.w3c.dom.HTMLHeadingElement>")
                    .replace("<HTMLLabelElement>", "<org.w3c.dom.HTMLLabelElement>")
                    .replace("TypographyProps", "mui.material.TypographyProps")
                    .replace("TransitionProps", "mui.material.transitions.TransitionProps")
            ).joinToString(",\n", "\n")
        }
    }

    val source = propsContent
        .substringAfter("{\n")

    val membersContent = source
        .takeIf { !it.startsWith("}\n") }
        ?.substringBefore(";\n}")
        ?: ""

    val body = convertMembers(membersContent)
    return props(propsName, parentType, CHILDREN in body) + " {\n" +
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
        props(propsName, hasChildren = CHILDREN in body) + " {\n" +
                body +
                "\n}"
    } else {
        props(propsName)
    }
}

private val ADDITIONAL_PREFIXES = setOf(
    "Origin",
    "Position",
    "Classes",
)

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

        if (!propsLike && ADDITIONAL_PREFIXES.all { !interfaceName.endsWith(it) })
            return@mapNotNull null

        val membersContent = if (interfaceName != "InputBaseComponentProps") {
            body.substringAfter("{\n")
                .substringBefore(";\n}\n")
        } else ""

        val propsBody = convertMembers(membersContent)
        val declaration = if (propsLike) {
            props(interfaceName, hasChildren = CHILDREN in propsBody)
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
    parentType: String? = null,
    hasChildren: Boolean = false,
): String {
    val parentTypes = when {
        parentType != null && hasChildren
        -> sequenceOf(parentType, "react.PropsWithChildren")
            .joinToString(",\n")

        parentType != null
        -> parentType

        else -> if (hasChildren) "react.PropsWithChildren" else "react.Props"
    }

    return "external interface $propsName: $parentTypes"
}

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
    comment = if ("\n\n" in comment) {
        comment.substringAfterLast("\n\n")
    } else {
        comment.substringAfterLast("};\n")
    }

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

private const val CHILDREN = "override var children: ReadonlyArray<react.ReactNode>?"

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

    val jsName = values.asSequence()
        .map { "${enumConstant(it)}: ${it.toIntOrNull() ?: "'$it'"}" }
        .joinToString(", ", "@JsName(\"\"\"($UNION_MARKER{", "}$UNION_MARKER)\"\"\")")

    val constantNames = values.asSequence()
        .map { "${enumConstant(it)},\n" }
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
): String =
    when {
        value.toIntOrNull() != null
        -> "s$value"

        else -> value.removePrefix("@")
            .kebabToCamel()
    }
