package com.github.turansky.mui

internal fun findDefaultFunction(
    name: String,
    content: String,
): String? {
    if (!name.startsWith("use") && !name.startsWith("create"))
        return null

    if (name == "createTransitions")
        return null

    // TEMP
    if (name == "useAutocomplete")
        return null

    val (before, source) = content.split("export default function ")

    val comment = if (before.endsWith("**/\n")) {
        before.substringAfterLast("\n\n")
    } else ""

    return comment +
            "@JsName(\"default\")\n" +
            "external fun $source"
}
