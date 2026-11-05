package playground.samples.dateio

import dateio.core.IUtils
import js.objects.unsafeJso
import js.reflect.Reflect
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import react.FC
import react.Props
import kotlin.js.Date

// Only the adapter constructor and locale's code are sample-local bindings. Every operation below
// goes through the generated IUtils<Date, DateFnsLocale>, not MUI's different MuiPickersAdapter API.
private external interface DateFnsLocale {
    val code: String
}

private external interface DateFnsOptions {
    var locale: DateFnsLocale
}

@JsModule("date-fns/locale/en-GB")
private external val enGB: DateFnsLocale

@JsModule("@date-io/date-fns")
@JsName("default")
private external val DateFnsUtils: JsClass<IUtils<Date, DateFnsLocale>>

val DateIoUtilsSample: Sample = Sample(
    id = "iutils",
    library = Libraries.DateIo,
    title = "date-io IUtils",
    kind = Kind.Check,
    covers = listOf(
        "IUtils.date() / date(null) / date(String) / date(Date)",
        "IUtils.isValid",
        "IUtils.getDate",
        "IUtils.getWeek",
        "IUtils.format(\"keyboardDate\")",
        "IUtils.locale",
    ),
    fileName = "DateIoUtilsSample.kt",
    refs = listOf(
        Ref(
            "docs/history/mui-v9.md — @date-io/core 3.2",
            "https://github.com/karakum-team/mui-kotlin/blob/main/docs/history/mui-v9.md#date-iocore-217--32-update-2026-10-01",
        ),
        Ref("f29b6170", "https://github.com/karakum-team/mui-kotlin/commit/f29b6170"),
    ),
    notes = "Only running this proves that the generated `IUtils<Date, Locale>` overloads match a real adapter: " +
            "every call goes through `@date-io/date-fns 3.2.1` (playground-only runtime implementation) with the " +
            "en-GB locale, not through MUI's different `MuiPickersAdapter` API.",
    content = FC<Props> {
        val utils: IUtils<Date, DateFnsLocale> = Reflect.construct(
            DateFnsUtils,
            arrayOf(unsafeJso<DateFnsOptions> { locale = enGB }),
        )
        val now: Date = utils.date()
        val empty: Date? = utils.date(null)
        val parsed: Date? = utils.date("2026-10-15T12:00:00")
        val copied: Date? = utils.date(Date(2026, 9, 15, 12))
        val locale: DateFnsLocale? = utils.locale

        val results = listOf(
            "date() returns a valid Date" to utils.isValid(now),
            "date(null) returns null" to (empty == null),
            "date(ISO string) preserves the day" to (parsed != null && utils.getDate(parsed) == 15),
            "date(Date) preserves the day" to (copied != null && utils.getDate(copied) == 15),
            "typed locale is en-GB" to (locale?.code == "en-GB"),
            "getWeek(2026-10-15) is 42" to (parsed != null && utils.getWeek(parsed) == 42),
            "localized keyboardDate is 15/10/2026" to
                    (parsed != null && utils.format(parsed, "keyboardDate") == "15/10/2026"),
        )

        CheckList {
            id = "iutils"
            checks = results.map { (label, passed) -> Check(label, passed) }
        }
    },
)
