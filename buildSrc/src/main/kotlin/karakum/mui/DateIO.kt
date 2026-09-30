package karakum.mui

// The 3.x interface includes a commented-out constructor with an object parameter. Strip these
// line comments before the shared adapters: their semicolon/comment normalization can remove the
// commented closing brace and make the object-value adapter consume the rest of IUtils.
internal fun preprocessDateIo(source: String): String = source.lineSequence()
    .filterNot { it.trimStart().startsWith("//") }
    .joinToString("\n")

// @date-io/core 3.x expresses the return type through conditional generic defaults. Kotlin has no
// equivalent: preserve the non-null no-argument branch, and conservatively keep explicit inputs
// (including null) nullable. Never expose TResultingDate as a caller-chosen, unchecked return type.
private val dateUtilsDateSignature = Regex(
    """\bdate<\s*TArg extends unknown = undefined,\s*TResultingDate extends unknown = TArg extends null\s*\? null\s*:\s*TArg extends undefined\s*\? TDate\s*:\s*TDate \| null\s*>\(\s*value\?: TArg\s*\): TResultingDate"""
)

internal fun convertDateUtils(
    source: String,
): String {
    check(dateUtilsDateSignature.findAll(source).count() == 1) {
        "Unsupported @date-io/core IUtils.date signature; review its conditional return type."
    }

    return source
        .trimIndent()
        .replace(dateUtilsDateSignature, "date(): TDate;\ndate(value?: any): TDate | null")
        .splitToSequence("\n")
        .map { it.removeSuffix(";") }
        .joinToString("\n") { line ->
            when {
                line.isEmpty() -> line
                line.startsWith("/**") -> line
                line.startsWith(" *") -> line
                line.startsWith("/") -> line

                "(" in line -> "fun " + line
                    .replace(": TDate[][]", ": ReadonlyArray<ReadonlyArray<TDate>>")
                    .replace(": TDate[]", ": ReadonlyArray<TDate>")
                    .replace(": TDate[]", ": ReadonlyArray<TDate>")
                    .replace(": [TDate, TDate]", ": Tuple2<TDate, TDate>")
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
                    .replace("?: TLocale", ": TLocale?")
                    .replace(": string", ": String")
                    .replace(": DateIOFormats<any>", ": DateIOFormats<*>")
            }
        }
}
