import dateio.core.IUtils
import js.objects.unsafeJso
import js.reflect.Reflect
import react.FC
import react.Props
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.ul
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

val DateIo = FC<Props> {
    val utils: IUtils<Date, DateFnsLocale> = Reflect.construct(
        DateFnsUtils,
        arrayOf(unsafeJso<DateFnsOptions> { locale = enGB }),
    )
    val now: Date = utils.date()
    val empty: Date? = utils.date(null)
    val parsed: Date? = utils.date("2026-10-15T12:00:00")
    val copied: Date? = utils.date(Date(2026, 9, 15, 12))
    val locale: DateFnsLocale? = utils.locale

    val checks = listOf(
        "date() returns a valid Date" to utils.isValid(now),
        "date(null) returns null" to (empty == null),
        "date(ISO string) preserves the day" to (parsed != null && utils.getDate(parsed) == 15),
        "date(Date) preserves the day" to (copied != null && utils.getDate(copied) == 15),
        "typed locale is en-GB" to (locale?.code == "en-GB"),
        "getWeek(2026-10-15) is 42" to (parsed != null && utils.getWeek(parsed) == 42),
        "localized keyboardDate is 15/10/2026" to
                (parsed != null && utils.format(parsed, "keyboardDate") == "15/10/2026"),
    )

    section {
        h2 { +"@date-io/core 3.2.0 — IUtils" }
        ul {
            checks.forEach { (label, passed) ->
                li {
                    key = label.unsafeCast<react.Key>()
                    +"$label: ${if (passed) "PASS" else "FAIL"}"
                }
            }
        }
    }
}
