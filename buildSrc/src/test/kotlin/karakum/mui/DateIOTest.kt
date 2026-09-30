package karakum.mui

import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse

class DateIOTest {
    // Verbatim conditional signature from @date-io/core 3.2.0/IUtils.d.ts.
    private val dateSignature = """
        date<
          TArg extends unknown = undefined,
          TResultingDate extends unknown = TArg extends null
            ? null
            : TArg extends undefined
            ? TDate
            : TDate | null
        >(
          value?: TArg
        ): TResultingDate;
    """.trimIndent()

    @Test
    fun conditionalDateBecomesSafeOverloads() {
        assertEquals(
            "fun date(): TDate\nfun date(value: Any?): TDate?",
            convertDateUtils(dateSignature),
        )
        assertEquals(
            convertDateUtils(dateSignature),
            convertDateUtils(dateSignature.replace(Regex("\\s+"), " ")),
        )
    }

    @Test
    fun rejectsChangedConditionalReturnType() {
        assertFailsWith<IllegalStateException> {
            convertDateUtils(dateSignature.replace("TDate | null", "TDate | string"))
        }
    }

    @Test
    fun preservesLocaleAndDateGenericsThroughFullConverter() {
        val directory = createTempDirectory("dateio-converter-test-").toFile()
        val definition = directory.resolve("IUtils.d.ts")
        try {
            definition.writeText(
                "export interface IUtils<TDate, TLocale> {\n" +
                        "  locale?: TLocale;\n" +
                        "  // Constructor type\n" +
                        "  // new (options?: {\n" +
                        "  //   formats?: Partial<DateIOFormats>;\n" +
                        "  //   locale?: TLocale;\n" +
                        "  //   instance?: any;\n" +
                        "  // }): IUtils<TDate, TLocale>;\n" +
                        "  /**\n   * Creates a date.\n   *\n   * Null stays null.\n   */\n" +
                        dateSignature.prependIndent("  ") + "\n" +
                        "  getWeek(value: TDate): number;\n" +
                        "  getWeekArray(date: TDate): TDate[][];\n" +
                        "}\n"
            )
            val converted = convertDefinitions(definition, preprocess = ::preprocessDateIo).main
            assertContains(converted, "external interface IUtils<TDate, TLocale>")
            assertContains(converted, "val locale: TLocale?")
            assertContains(converted, "fun date(): TDate")
            assertContains(converted, "fun date(value: Any?): TDate?")
            assertContains(converted, "fun getWeek(value: TDate): Int")
            assertContains(converted, "fun getWeekArray(date: TDate): ReadonlyArray<ReadonlyArray<TDate>>")
            assertFalse("TResultingDate" in converted)
            assertFalse("TArg" in converted)
            assertFalse("val  *" in converted)
            assertFalse("new (options" in converted)
            assertContains(DATE_ADAPTER_BODY, "IUtils<*, *>")
        } finally {
            definition.delete()
            directory.delete()
        }
    }
}
