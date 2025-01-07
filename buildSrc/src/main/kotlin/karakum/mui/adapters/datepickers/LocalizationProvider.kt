package karakum.mui.adapters.datepickers

fun String.adaptLocalizationProvider(): String {
    return replace(
        """
export interface MuiPickersAdapterContextValue<TDate extends PickerValidDate> {
    defaultDates: {
        minDate: TDate;
        maxDate: TDate;
    };
    utils: MuiPickersAdapter<TDate>;
    localeText: PickersInputLocaleText<TDate> | undefined;
}""",
        "",
    )
}
