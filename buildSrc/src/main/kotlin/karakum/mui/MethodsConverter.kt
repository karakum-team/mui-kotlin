package karakum.mui

internal fun convertMethods(
    source: String,
): String {
    return source.splitToSequence(";\n")
        .joinToString("\n") {
            val name = it.trim()
                .substringBefore("(")
                .removeSuffix(": ")

            val declaration = it.trim()
                .removeSuffix(": void")
                .removeSuffix(" => void")
                .replace("$name: ", "$name ")
                .replace("ListAction<string>", "Any /* ListAction<string> */")
                .replace("?: React.SyntheticEvent", ": react.dom.events.SyntheticEvent<*, *> = definedExternally")
                .replace("?: StartActionOptions", ": StartActionOptions = definedExternally")
                .replace("?: () => void", ": () -> Unit  = definedExternally")

            "fun $declaration"
        }
}
