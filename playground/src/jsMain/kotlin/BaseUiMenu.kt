import baseui.Align
import baseui.Menu
import baseui.MenuRootOrientation
import baseui.Orientation
import baseui.Side
import baseui.className
import baseui.render
import baseui.style
import emotion.react.Global
import emotion.react.styles
import js.objects.unsafeJso
import react.CSSProperties
import react.FC
import react.Props
import react.create
import react.dom.events.MouseEvent
import react.dom.html.ReactHTML.hr
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.span
import react.useState
import web.cssom.Border
import web.cssom.BoxShadow
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Position
import web.cssom.integer
import web.cssom.pct
import web.cssom.px
import web.cssom.rem
import web.cssom.rgb
import web.dom.document

// Live check of the generated `baseui` declarations, and the only usage reference for them in the repo.
//
// Every member of the `Menu` namespace object is mounted, so the page fails loudly if a key does not
// exist at runtime, and the state-typed `className` / `style` / `render` helpers from `<Part>.ext.kt` are
// used at call sites — neither of which `:mui-kotlin:compileKotlinJs` can prove on its own. Both arms of
// each value-or-callback prop appear at least once: the callback arm resolves to the extension function,
// the value arm to the inherited `Any?` property, and the sample would not compile if either broke.
//
// Class names are literal and `bui-`-prefixed so they can be asserted against in the DOM.
val BaseUiMenu = FC<Props> {
    var shuffle by useState(true)
    var sortBy by useState("date")
    var lastAction by useState("-")
    var lastReason by useState("-")

    Global {
        styles {
            // A sibling sample (`SliderStylization`) lays an absolutely positioned element over the whole
            // viewport, so this one needs a stacking context of its own to stay clickable.
            ".bui-status, .bui-trigger" {
                position = Position.relative
                zIndex = integer(1)
            }

            ".bui-trigger" {
                padding = 8.px
                border = Border(1.px, solid, Color("#111111"))
                backgroundColor = NamedColor.white
                cursor = Cursor.pointer
            }
            ".bui-trigger--open" { backgroundColor = Color("#e6e6e6") }

            ".bui-backdrop" {
                position = Position.fixed
                top = 0.px
                left = 0.px
                width = 100.pct
                height = 100.pct
                backgroundColor = rgb(0, 0, 0, 0.06)
            }

            ".bui-positioner" { zIndex = integer(1000) }

            ".bui-popup" {
                padding = 4.px
                backgroundColor = NamedColor.white
                color = Color("#111111")
                border = Border(1.px, solid, Color("#111111"))
                borderRadius = 8.px
                boxShadow = BoxShadow(0.px, 6.px, 18.px, rgb(0, 0, 0, 0.18))
            }

            ".bui-arrow" {
                width = 8.px
                height = 8.px
                backgroundColor = NamedColor.white
                border = Border(1.px, solid, Color("#111111"))
            }

            ".bui-item" {
                display = Display.block
                padding = 8.px
                borderRadius = 4.px
                cursor = Cursor.default
            }
            ".bui-item--highlighted" {
                backgroundColor = Color("#111111")
                color = NamedColor.white
            }
            ".bui-item--checked" { color = Color("#0a7d00") }
            ".bui-item--submenu-open" { backgroundColor = Color("#e6e6e6") }
            ".bui-item--disabled" { color = Color("#999999") }

            ".bui-label" {
                padding = 8.px
                fontSize = 12.px
                color = Color("#666666")
            }

            // Transparent rather than absent: the indicator that is kept mounted while unchecked has to
            // be hidden by the consumer's CSS, Base UI does not mark it.
            ".bui-indicator" {
                display = Display.inlineBlock
                width = 14.px
                color = Color.transparent
            }
            ".bui-indicator--on" { color = Color("#0a7d00") }
        }
    }

    p {
        className = ClassName("bui-status")
        +"shuffle=$shuffle sortBy=$sortBy action=$lastAction reason=$lastReason"
    }

    Menu.Root {
        // Same as the default, and the only place in the repo where a generated seskar union is assigned
        // to a prop that gap 5 widened to `Any?` — proving those unions are still usable.
        orientation = MenuRootOrientation.vertical

        // Not modal, so the rest of the page stays inspectable while the menu is open.
        modal = false
        loopFocus = true
        highlightItemOnHover = true
        onOpenChange = { open, eventDetails ->
            lastReason = (if (open) "open:" else "close:") + eventDetails.reason
        }

        Menu.Trigger {
            className { state ->
                if (state.open) ClassName("bui-trigger bui-trigger--open")
                else ClassName("bui-trigger")
            }
            +"Song"
        }

        Menu.Portal {
            // Kept mounted so the closed-state class and style are observable in the DOM too, which is
            // what makes "the callback re-runs per state" provable rather than assumed. The default
            // unmounts the whole subtree while the menu is closed.
            keepMounted = true

            // The default target anyway, but it pins down `container` — the one prop of the hand-written
            // `FloatingPortalProps` stub, which nothing else in the repository would notice losing.
            container = document.body

            // Callback arm on the Portal: also the compile-time guard for its helpers, which exist only
            // because `FloatingPortalProps` is accepted as an element-props marker.
            className { ClassName("bui-portal") }

            Menu.Backdrop {
                className { state ->
                    if (state.open) ClassName("bui-backdrop bui-backdrop--open")
                    else ClassName("bui-backdrop")
                }
            }

            Menu.Positioner {
                // The anchor-positioning props, inherited from `UseAnchorPositioningSharedParameters`.
                // `side` and `align` are the generated seskar unions; the offsets and the collision
                // padding are `Any?`, since each is a union with a callback or an object arm.
                side = Side.bottom
                align = Align.start
                sideOffset = 8
                alignOffset = -4
                collisionPadding = 12
                arrowPadding = 6
                sticky = true

                // The same two values read back off the state, which is how the positioner reports
                // where it actually landed — flip `side` above and the class name follows.
                className { state ->
                    ClassName("bui-positioner bui-positioner--${state.side} bui-positioner--${state.align}")
                }

                Menu.Popup {
                    className { state ->
                        if (state.open) ClassName("bui-popup bui-popup--open")
                        else ClassName("bui-popup bui-popup--closed")
                    }
                    style { state ->
                        unsafeJso<CSSProperties> {
                            minWidth = if (state.open) 16.rem else 8.rem
                        }
                    }

                    Menu.Arrow {
                        className { state -> ClassName("bui-arrow bui-arrow--${state.side}") }
                    }

                    // The viewport wraps the popup's content — it is the transition container, not a
                    // sibling of the items. Mounting it also switches the positioner to its
                    // `adaptiveOrigin` middleware, so it is not a neutral addition.
                    Menu.Viewport {
                        className { state ->
                            if (state.transitioning) ClassName("bui-viewport bui-viewport--transitioning")
                            else ClassName("bui-viewport")
                        }

                        Menu.Item {
                            className { state ->
                                if (state.highlighted) ClassName("bui-item bui-item--highlighted")
                                else ClassName("bui-item")
                            }
                            // `MouseEvent`, not the declared `Any?`: the indexed-access type
                            // `BaseUiDivProps['onClick']` is not resolved yet (gap 10), and annotating the
                            // real type here keeps call sites honest until it is.
                            onClick = { _: MouseEvent<*, *> -> lastAction = "add-to-library" }
                            +"Add to Library"
                        }

                        // `render` with the props applied. No children in this builder: `+props` already
                        // carries them, which is what the helper's KDoc warns about.
                        Menu.Item {
                            className { ClassName("bui-item") }
                            onClick = { _: MouseEvent<*, *> -> lastAction = "rendered-item" }
                            render { props, state ->
                                span.create {
                                    +props
                                    title = if (state.highlighted) "highlighted" else "idle"
                                }
                            }
                            +"Rendered item (props applied)"
                        }

                        Menu.LinkItem {
                            className { state ->
                                if (state.highlighted) ClassName("bui-item bui-item--highlighted")
                                else ClassName("bui-item")
                            }
                            href = "#link-item"
                            +"Open documentation"
                        }

                        // `render` swapping the element with the props applied: the separator keeps its
                        // `role="separator"` and `aria-orientation`.
                        Menu.Separator {
                            orientation = Orientation.horizontal
                            render { props, _ -> hr.create { +props } }
                        }

                        Menu.Group {
                            // Value arm: `MenuGroupState` is empty, so there is nothing to branch on.
                            className = ClassName("bui-group")

                            Menu.GroupLabel {
                                // The element arm of `render`, which Base UI *does* merge into — unlike
                                // the callback arm below.
                                render = span.create { +"Playback" }
                            }
                            Menu.Item {
                                className { state ->
                                    if (state.highlighted) ClassName("bui-item bui-item--highlighted")
                                    else ClassName("bui-item")
                                }
                                onClick = { _: MouseEvent<*, *> -> lastAction = "play-next" }
                                +"Play Next"
                            }
                            Menu.Item {
                                className { state ->
                                    if (state.disabled) ClassName("bui-item bui-item--disabled")
                                    else ClassName("bui-item")
                                }
                                disabled = true
                                +"Play Last (disabled)"
                            }
                        }

                        // The same shape with the props dropped — no role, no aria-orientation, nothing
                        // but the class the callback set. This is the caveat in the helper's KDoc,
                        // rendered.
                        Menu.Separator {
                            render { _, _ -> hr.create { className = ClassName("bui-sep-unmerged") } }
                        }

                        Menu.CheckboxItem {
                            className { state ->
                                if (state.checked) ClassName("bui-item bui-item--checked")
                                else ClassName("bui-item")
                            }
                            checked = shuffle
                            onCheckedChange = { value, _ -> shuffle = value }

                            Menu.CheckboxItemIndicator {
                                // Kept mounted so the unchecked state is observable; `.bui-indicator`
                                // hides it. The radio indicators below are left at the default, which
                                // unmounts them instead.
                                keepMounted = true
                                className { state ->
                                    if (state.checked) ClassName("bui-indicator bui-indicator--on")
                                    else ClassName("bui-indicator")
                                }
                                +"x"
                            }
                            +"Shuffle"
                        }

                        Menu.RadioGroup {
                            value = sortBy
                            onValueChange = { value, _ -> sortBy = value.toString() }

                            className { ClassName("bui-radio-group") }

                            Menu.GroupLabel {
                                className = ClassName("bui-label")
                                +"Sort by"
                            }

                            Menu.RadioItem {
                                className { state ->
                                    if (state.checked) ClassName("bui-item bui-item--checked")
                                    else ClassName("bui-item")
                                }
                                value = "date"
                                Menu.RadioItemIndicator {
                                    className { ClassName("bui-indicator bui-indicator--on") }
                                    +"o"
                                }
                                +"date"
                            }

                            Menu.RadioItem {
                                className { state ->
                                    if (state.checked) ClassName("bui-item bui-item--checked")
                                    else ClassName("bui-item")
                                }
                                value = "title"
                                Menu.RadioItemIndicator {
                                    className { ClassName("bui-indicator bui-indicator--on") }
                                    +"o"
                                }
                                +"title"
                            }
                        }

                        Menu.SubmenuRoot {
                            Menu.SubmenuTrigger {
                                className { state ->
                                    when {
                                        state.open -> ClassName("bui-item bui-item--submenu-open")
                                        state.highlighted -> ClassName("bui-item bui-item--highlighted")
                                        else -> ClassName("bui-item")
                                    }
                                }
                                +"Share >"
                            }

                            Menu.Portal {
                                // Value arm, and the default `keepMounted = false`: this subtree is not
                                // in the DOM until the submenu opens.
                                className = ClassName("bui-portal bui-portal--sub")

                                Menu.Positioner {
                                    // A submenu wants the logical inline end rather than a physical
                                    // side, so that it opens leftwards in an RTL document.
                                    side = Side.inlineEnd
                                    align = Align.start

                                    // The callback arm of `sideOffset`, upstream `number |
                                    // OffsetFunction`. The union has no Kotlin spelling, so the prop is
                                    // `Any?` and both arms assign: the parent menu passes a plain
                                    // number, this one a function. If it were not called the submenu
                                    // would sit flush against its parent.
                                    sideOffset = { _: Any -> 4 }

                                    className { state ->
                                        ClassName("bui-positioner bui-subpositioner bui-positioner--${state.side}")
                                    }

                                    Menu.Popup {
                                        className { state ->
                                            if (state.open) ClassName("bui-popup bui-subpopup bui-popup--open")
                                            else ClassName("bui-popup bui-subpopup bui-popup--closed")
                                        }

                                        Menu.Item {
                                            className { ClassName("bui-item") }
                                            onClick = { _: MouseEvent<*, *> -> lastAction = "share-copy-link" }
                                            +"Copy link"
                                        }
                                        Menu.Item {
                                            className { ClassName("bui-item") }
                                            onClick = { _: MouseEvent<*, *> -> lastAction = "share-email" }
                                            +"Email"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
