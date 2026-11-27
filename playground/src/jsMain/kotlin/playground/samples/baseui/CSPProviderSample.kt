package playground.samples.baseui

import baseui.CSPProvider
import emotion.react.Global
import emotion.react.styles
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useState
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.BoxSizing
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.number
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

val CSPProviderSample: Sample = Sample(
    id = "csp-provider",
    library = Libraries.BaseUi,
    title = "CSPProvider",
    kind = Kind.Check,
    covers = listOf(
        "CSPProvider (direct component export)",
        "CSPProviderProps.nonce",
        "CSPProviderProps.disableStyleElements",
        "CSPProviderProps.children",
    ),
    fileName = "CSPProviderSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI CSP Provider", "https://base-ui.com/react/utils/csp-provider"),
    ),
    notes = "Direct component export from @base-ui/react/csp-provider. Context provider " +
        "configuring Content Security Policy for Base UI components that inject styles or scripts, " +
        "supporting nonce and disableStyleElements props.",
    content = FC<Props> {
        var nonceCounter by useState(1)
        var disableStyleElements by useState(false)
        val currentNonce = "csp-nonce-$nonceCounter"

        Global {
            styles {
                ".bui-csp-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-csp-section" {
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    gap = 8.px
                    marginTop = 16.px
                    marginBottom = 16.px
                }
                ".bui-csp-controls" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 12.px
                    marginTop = 8.px
                }
                ".bui-csp-container" {
                    padding = 12.px
                    backgroundColor = Color("#f8f9fa")
                    borderRadius = 6.px
                    border = Border(1.px, solid, Color("#eaecf0"))
                }
                ".bui-csp-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-csp-sample button:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-csp-sample")
            className = ClassName("bui-csp-sample")

            CheckList {
                id = "csp-provider-checks"
                checks = listOf(
                    Check("CSPProvider component is loaded", jsTypeOf(CSPProvider) == "function" || jsTypeOf(CSPProvider) == "object"),
                    Check("nonce state is non-empty", currentNonce.isNotEmpty()),
                    Check("disableStyleElements is boolean", jsTypeOf(disableStyleElements) == "boolean"),
                )
            }

            StatusLine {
                id = "csp-provider-status"
                fields = listOf(
                    "nonce" to currentNonce,
                    "disableStyleElements" to disableStyleElements.toString(),
                )
            }

            div {
                className = ClassName("bui-csp-section")
                p { +"Interactive CSPProvider wrapping child content:" }

                CSPProvider {
                    nonce = currentNonce
                    this.disableStyleElements = disableStyleElements

                    div {
                        id = ElementId("csp-provider-content")
                        className = ClassName("bui-csp-container")
                        p {
                            +"Active nonce: "
                            code { +currentNonce }
                        }
                        p {
                            +"disableStyleElements: "
                            code { disableStyleElements.toString() }
                        }
                    }
                }

                div {
                    className = ClassName("bui-csp-controls")
                    button {
                        id = ElementId("csp-rotate-nonce-btn")
                        type = ButtonType.button
                        onClick = { nonceCounter++ }
                        +"Rotate nonce"
                    }
                    button {
                        id = ElementId("csp-toggle-disable-styles-btn")
                        type = ButtonType.button
                        onClick = { disableStyleElements = !disableStyleElements }
                        +if (disableStyleElements) "Enable style elements" else "Disable style elements"
                    }
                }
            }
        }
    }
)
