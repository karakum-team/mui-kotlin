package karakum.mui

internal fun convertMembers(
    source: String,
): String {
    if (source.isEmpty())
        return ""

    return source
        .replaceIndent("  ")
        .replace("  // component tokens\n", "")
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
        .filter { it.isNotEmpty() }
        .joinToString("\n\n")
}

private fun convertMember(
    source: String,
): String {
    // Peel ONLY the member's leading documentation (a `/** … */` block and/or `//` lines),
    // then convert the whole remainder as a single property. The property body may itself
    // contain `*/\n` — v7 gives inline object-literal values (`componentsProps`/`slotProps`)
    // their own inner JSDoc — and those inner docs must stay with the property so
    // kotlinType()/componentInterface()/stripInlineDocs() can strip them. Splitting on every
    // `*/\n` here used to tear such a member apart into an empty interface plus a leaked member.
    val comments = mutableListOf<String>()
    var rest = source

    while (true) {
        val trimmed = rest.trimStart()
        when {
            trimmed.startsWith("/**") -> {
                val end = rest.indexOf("*/")
                if (end < 0) break
                comments += rest.substring(0, end + 2).trimStart()
                rest = rest.substring(end + 2).removePrefix("\n")
            }

            trimmed.startsWith("//") -> {
                val nl = rest.indexOf("\n")
                if (nl < 0) {
                    comments += rest.trimStart()
                    rest = ""
                } else {
                    comments += rest.substring(0, nl).trimStart()
                    rest = rest.substring(nl + 1)
                }
            }

            else -> break
        }
    }

    val deprecatedAnnotation = comments
        .firstOrNull { "@deprecated" in it }
        ?.substringAfter("@deprecated")
        ?.lines()?.first()?.trim()
        ?.removePrefix("* ")?.trim()
        ?.takeIf { it.isNotEmpty() }
        ?.let { msg -> "@Deprecated(\"${msg.replace("\"", "\\\"")}\")\n" }
        ?: if (comments.any { "@deprecated" in it }) "@Deprecated\n" else ""

    val cleanedComments = if (deprecatedAnnotation.isEmpty()) {
        comments
    } else {
        comments.mapNotNull { comment ->
            if ("@deprecated" !in comment) return@mapNotNull comment
            val filtered = comment.lines().filter { "@deprecated" !in it }
            // Drop the comment entirely if nothing meaningful remains beyond /** and */
            val hasContent = filtered.any { line ->
                val t = line.trim()
                t.isNotEmpty() && t != "/**" && t != "*/" && t != "*"
            }
            if (hasContent) filtered.joinToString("\n") else null
        }
    }

    val property = rest.trimStart().takeIf { it.isNotEmpty() }
        ?.let { deprecatedAnnotation + convertProperty(it) }
        ?: ""

    return (cleanedComments + property)
        .filter { it.isNotEmpty() }
        .joinToString("\n")
}

private const val CSS_RECORD = "[k: string]: unknown | CSSProperties"

private fun convertProperty(
    source: String,
): String {
    if (source == CSS_RECORD)
        return "// $CSS_RECORD"

    val rawNameToken = source.substringBefore(":").removeSuffix("?")
    val jsNameAnnotation = if (rawNameToken.startsWith("'"))
        "@JsName(\"${rawNameToken.removeSurrounding("'")}\")\n"
    else ""
    val name = kotlinName(rawNameToken)

    if (name == "ref" || name == "}")
        return ""

    val type = kotlinType(
        source.substringAfter(":").removePrefix(" "),
        name,
    )

    if (name == "children") {
        if (type == "react.ReactNode") {
            return CHILDREN
        }

        if (type == "react.ReactElement<*>") {
            return "$CHILDREN /* react.ReactElement<*>? */"
        }

        // Fallback type with ReactNode/ReactElement in the original TS — still treat as React children.
        if (type.startsWith("Any? /*") && ("ReactNode" in type || "ReactElement" in type)) {
            val comment = type.substringAfter("Any? /* ").removeSuffix(" */")
            return "$CHILDREN /* $comment */"
        }
    }

    if (name == "id")
        return ID

    if (name == "className" && type == "String")
        return CLASS_NAME

    if (name == "sx")
        return SX

    val optional = source.substringBefore(":")
        .endsWith("?")

    val fullType = when {
        !optional -> type
        type == DYNAMIC -> type
        type.startsWith("$DYNAMIC ") -> type
        type.startsWith("Components\n") -> type.replaceFirst("Components\n", "Components?\n")
        type.startsWith("ComponentsProps\n") -> type.replaceFirst("ComponentsProps\n", "ComponentsProps?\n")
        type.startsWith("Slots\n") -> type.replaceFirst("Slots\n", "Slots?\n")
        type.startsWith("SlotProps\n") -> type.replaceFirst("SlotProps\n", "SlotProps?\n")
        type.startsWith("(") -> "($type)?"
        "? /*" in type -> type
        type.endsWith("*/") -> type.replace(" /*", "? /*")
        " //" in type -> type.replace(" //", "? //")
        type.endsWith("?") -> type
        else -> "$type?"
    }

    val modifier = if (": Readonly<" in source) "val" else "var"
    val declaration = jsNameAnnotation + "$modifier $name: $fullType"

    return declaration
}

private fun kotlinName(name: String): String =
    when {
        name == "in" ->
            "`in`"

        name.startsWith("'") -> {
            val raw = name.removeSurrounding("'")
            raw.split("-").mapIndexed { i, s ->
                val part = if (i == 0) s else s.replaceFirstChar { it.uppercaseChar() }
                if (i == 0) part.dropWhile { !it.isLetter() && it != '_' } else part
            }.joinToString("")
        }

        name.isNotEmpty() && name[0].isDigit() ->
            "`$name`"

        else -> name
    }
