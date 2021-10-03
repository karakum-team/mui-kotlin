package com.github.turansky.mui

private val MINUS_LETTER = Regex("""-(\w)""")
private val SPACE_LETTER = Regex("""\s(\w)""")

private val toUpperCase: (MatchResult) -> CharSequence = {
    it.groupValues[1].toUpperCase()
}

internal fun String.kebabToCamel(): String =
    replace(MINUS_LETTER, toUpperCase)
        .replace(SPACE_LETTER, toUpperCase)
