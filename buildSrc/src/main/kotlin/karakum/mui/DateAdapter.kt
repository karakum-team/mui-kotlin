package karakum.mui

internal val DATE_ADAPTERS = listOf(
    "AdapterDateFns",
    "AdapterDayjs",
    "AdapterLuxon",
    "AdapterMoment",
).map { name ->
    name to "external val $name : DateAdapter"
}

internal const val DATE_ADAPTER_BODY = "sealed external interface DateAdapter"
