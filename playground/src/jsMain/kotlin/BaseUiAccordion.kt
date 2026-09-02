import baseui.Accordion
import baseui.AccordionRootChangeEventDetails
import emotion.react.Global
import emotion.react.styles
import react.FC
import react.Props
import react.useState
import web.cssom.*
import web.cssom.LineStyle.Companion.solid

val BaseUiAccordion = FC<Props> {
    var expandedItems by useState<Array<*>>(emptyArray<String>())
    var lastChangeReason by useState("-")

    Global {
        styles {
            ".bui-accordion-root" {
                display = Display.flex
                flexDirection = FlexDirection.column
                width = 300.px
                border = Border(1.px, solid, Color("#ccc"))
                borderRadius = 4.px
                backgroundColor = NamedColor.white
            }
            ".bui-accordion-item" {
                borderBottom = Border(1.px, solid, Color("#eee"))
            }
            ".bui-accordion-header" {
                margin = 0.px
            }
            ".bui-accordion-trigger" {
                display = Display.flex
                width = 100.pct
                padding = 12.px
                backgroundColor = Color("transparent")
                border = Border(0.px, solid, Color("transparent"))
                cursor = web.cssom.Cursor.pointer
            }
            ".bui-accordion-panel" {
                padding = 12.px
                backgroundColor = Color("#fafafa")
            }
        }
    }

    react.dom.html.ReactHTML.div {
        +"Expanded: ${expandedItems.joinToString(", ")} | Reason: $lastChangeReason"
    }

    Accordion.Root {
        className = ClassName("bui-accordion-root")
        defaultValue = arrayOf("item-1")
        onValueChange = { next: Any?, details: AccordionRootChangeEventDetails ->
            expandedItems = next as Array<*>
            lastChangeReason = details.reason
        }

        Accordion.Item {
            className = ClassName("bui-accordion-item")
            value = "item-1"

            Accordion.Header {
                className = ClassName("bui-accordion-header")
                Accordion.Trigger {
                    className = ClassName("bui-accordion-trigger")
                    +"Trigger 1"
                }
            }

            Accordion.Panel {
                className = ClassName("bui-accordion-panel")
                +"Content 1"
            }
        }

        Accordion.Item {
            className = ClassName("bui-accordion-item")
            value = "item-2"

            Accordion.Header {
                className = ClassName("bui-accordion-header")
                Accordion.Trigger {
                    className = ClassName("bui-accordion-trigger")
                    +"Trigger 2"
                }
            }

            Accordion.Panel {
                className = ClassName("bui-accordion-panel")
                +"Content 2"
            }
        }
    }
}
