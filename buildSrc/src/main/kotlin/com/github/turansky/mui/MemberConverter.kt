package com.github.turansky.mui

internal fun convertMembers(
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
        .filter { it.isNotEmpty() }
        .joinToString("\n\n")
}

private fun convertMember(
    source: String,
): String {
    val delimiter = if (!source.startsWith("// ")) "*/\n" else "\n"

    return source.splitToSequence(delimiter)
        .map {
            when {
                it.startsWith("/**") -> "$it*/"

                it.startsWith("//") -> it

                else -> convertProperty(it)
            }
        }
        .joinToString("\n")
}

private val CSS_RECORD = "[k: string]: unknown | CSSProperties"

private fun convertProperty(
    source: String,
): String {
    if (source == CSS_RECORD)
        return "// $CSS_RECORD"

    val name = source.substringBefore(":")
        .removeSuffix("?")
        .let { kotlinName(it) }

    if (name == "ref")
        return ""

    val type = kotlinType(
        source.substringAfter(":").removePrefix(" "),
        name,
    )

    if (name == "children" && type == "react.ReactNode")
        return CHILDREN

    val optional = source.substringBefore(":")
        .endsWith("?")

    val fullType = when {
        !optional -> type
        type == DYNAMIC -> type
        type.startsWith("$DYNAMIC ") -> type
        type.startsWith("(") -> "($type)?"
        type.endsWith("*/") -> type.replace(" /*", "? /*")
        " //" in type -> type.replace(" //", "? //")
        type.endsWith("?") -> type
        else -> "$type?"
    }

    var declaration = "var $name: $fullType"
    if ("-" in name) {
        declaration = "    // " + declaration
    }

    return declaration
}

private fun kotlinName(name: String): String =
    if (name == "in" || name.startsWith("'")) {
        "`${name.removeSurrounding("'")}`"
    } else name
