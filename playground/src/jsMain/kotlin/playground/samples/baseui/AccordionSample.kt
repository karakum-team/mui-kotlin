package playground.samples.baseui

import baseui.Accordion
import baseui.AccordionRootChangeEventDetails
import emotion.react.Global
import emotion.react.styles
import js.array.ReadonlyArray
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.useState
import web.cssom.Border
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.pct
import web.cssom.px

// Shared by `defaultValue` and the status, so the status matches what is open before the first change.
private val defaultExpanded: ReadonlyArray<Any> = arrayOf("item-1")

val AccordionSample: Sample = Sample(
    id = "accordion",
    library = Libraries.BaseUi,
    title = "Accordion",
    kind = Kind.Check,
    covers = listOf(
        "Accordion.Root/Item/Header/Trigger/Panel (5 parts)",
        "AccordionRootChangeEventDetails",
        "uncontrolled defaultValue + onValueChange",
    ),
    fileName = "AccordionSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("49e7bc24", "https://github.com/karakum-team/mui-kotlin/commit/49e7bc24"),
    ),
    notes = "Mounts all five namespace members, so a key missing at runtime takes the page down. Toggling a " +
        "trigger proves Base UI calls `onValueChange` with the array of expanded values and change details " +
        "whose `reason` reads back, and that the `className` values reach the rendered parts.",
    content = FC<Props> {
        var expandedItems by useState(defaultExpanded)
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
                    cursor = Cursor.pointer
                }
                ".bui-accordion-panel" {
                    padding = 12.px
                    backgroundColor = Color("#fafafa")
                }
            }
        }

        StatusLine {
            id = "accordion-status"
            fields = listOf(
                "expanded" to expandedItems.joinToString(",").ifEmpty { "-" },
                "reason" to lastChangeReason,
            )
        }

        Accordion.Root {
            className = ClassName("bui-accordion-root")
            defaultValue = defaultExpanded
            onValueChange = { next: ReadonlyArray<Any>, details: AccordionRootChangeEventDetails ->
                expandedItems = next
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
    },
)
