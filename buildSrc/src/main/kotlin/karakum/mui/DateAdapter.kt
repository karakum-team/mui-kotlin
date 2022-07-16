package karakum.mui

internal const val DATE_ADAPTER = "DateAdapter"
internal const val DATE_ADAPTER_BODY = "typealias $DATE_ADAPTER = JsClass<dateio.core.IUtils<*>>"

internal val DATE_ADAPTERS = listOf(
    "AdapterDateFns",
    "AdapterDayjs",
    "AdapterLuxon",
    "AdapterMoment",
).map { name ->
    name to "external val $name : $DATE_ADAPTER"
}
