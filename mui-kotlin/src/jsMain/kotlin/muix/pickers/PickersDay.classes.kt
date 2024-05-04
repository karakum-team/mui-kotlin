// Automatically generated - do not modify!

@file:JsModule("@mui/x-date-pickers/PickersDay")

package muix.pickers

import web.cssom.ClassName

external interface PickersDayClasses {
    /** Styles applied to the root element. */
    var root: ClassName

    /** Styles applied to the root element if `disableMargin=false`. */
    var dayWithMargin: ClassName

    /** Styles applied to the root element if `outsideCurrentMonth=true` and `showDaysOutsideCurrentMonth=true`. */
    var dayOutsideMonth: ClassName

    /** Styles applied to the root element if `outsideCurrentMonth=true` and `showDaysOutsideCurrentMonth=false`. */
    var hiddenDaySpacingFiller: ClassName

    /** Styles applied to the root element if `disableHighlightToday=false` and `today=true`. */
    var today: ClassName

    /** State class applied to the root element if `selected=true`. */
    var selected: ClassName

    /** State class applied to the root element if `disabled=true`. */
    var disabled: ClassName
}

external object pickersDayClasses : PickersDayClasses {
    override var root: ClassName = definedExternally
    override var dayWithMargin: ClassName = definedExternally
    override var dayOutsideMonth: ClassName = definedExternally
    override var hiddenDaySpacingFiller: ClassName = definedExternally
    override var today: ClassName = definedExternally
    override var selected: ClassName = definedExternally
    override var disabled: ClassName = definedExternally
}
