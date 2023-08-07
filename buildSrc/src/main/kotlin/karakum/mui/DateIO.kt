package karakum.mui

internal fun convertDateUtils(
    source: String,
): String {
    return source
        .trimIndent()
        .splitToSequence("\n")
        .map { it.removeSuffix(";") }
        .joinToString("\n") { line ->
            when {
                line.isEmpty() -> line
                line.startsWith("/**") -> line
                line.startsWith(" * ") -> line
                line.startsWith("/") -> line

                "(" in line -> "fun " + line
                    .replace(": TDate[][]", ": ReadonlyArray<ReadonlyArray<TDate>>")
                    .replace(": TDate[]", ": ReadonlyArray<TDate>")
                    .replace(": TDate[]", ": ReadonlyArray<TDate>")
                    .replace(": [TDate, TDate]", ": JsTuple2<TDate, TDate>")
                    .replace(": TDate | string", ": TDate")
                    .replace(": TDate | null", ": TDate?")
                    .replace(": Date", ": kotlin.js.Date")
                    .replace(": string[]", ": ReadonlyArray<String>")
                    .replace(": string", ": String")
                    .replace(": boolean", ": Boolean")
                    .replace(": number", ": Int")
                    .replace("?: any", ": Any?")
                    .replace(": any", ": Any")
                    .replace(": keyof DateIOFormats", ": String /* keyof DateIOFormats */")
                    .replace("?: Unit", ": String? /* Unit? */")
                    .replace(""": "am" | "pm"""", """: String /* "am" | "pm" */""")

                else -> "val " + line
                    .replace("?: any", ": Any?")
                    .replace(": string", ": String")
                    .replace(": DateIOFormats<any>", ": DateIOFormats<*>")
            }
        }
}
