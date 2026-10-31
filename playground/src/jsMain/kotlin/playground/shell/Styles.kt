package playground.shell

import emotion.react.Global
import emotion.react.styles
import react.FC
import react.Props
import web.cssom.AlignItems
import web.cssom.Auto.Companion.auto
import web.cssom.Border
import web.cssom.BorderCollapse
import web.cssom.Color
import web.cssom.Display
import web.cssom.FlexWrap
import web.cssom.FontFamily
import web.cssom.FontWeight
import web.cssom.LineStyle.Companion.solid
import web.cssom.Margin
import web.cssom.NamedColor
import web.cssom.None.Companion.none
import web.cssom.Padding
import web.cssom.Position
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import web.cssom.WhiteSpace
import web.cssom.number
import web.cssom.pct
import web.cssom.px
import web.cssom.rem
import web.cssom.vh

private val lineColor = Color("#d0d7de")
private val textMuted = Color("#57606a")
private val linkColor = Color("#0969da")

// Plain, non-hashed `pg-*` rules so the shell is easy to inspect in DevTools and never depends on MUI.
val ShellStyles = FC<Props> {
    Global {
        styles {
            ".pg-shell" {
                display = Display.flex
                alignItems = AlignItems.flexStart
                fontFamily = FontFamily.sansSerif
                lineHeight = number(1.4)
            }

            // Sticky instead of fixed + page-level scroll: `window.scrollTo` stays meaningful and sample popups
            // are not measured against a nested scroll container.
            ".pg-sidebar" {
                position = Position.sticky
                top = 0.px
                flexShrink = number(0.0)
                width = 16.rem
                height = 100.vh
                overflowY = auto
                padding = Padding(12.px, 16.px)
                borderRight = Border(1.px, solid, lineColor)
                fontSize = 14.px
            }

            ".pg-sidebar ul" {
                listStyle = none
                margin = Margin(0.px, 0.px, 8.px, 0.px)
                padding = 0.px
            }

            ".pg-sidebar a" {
                display = Display.block
                padding = Padding(2.px, 6.px)
                color = linkColor
                textDecoration = none
                borderRadius = 4.px
            }

            ".pg-sidebar a[aria-current=page]" {
                backgroundColor = Color("#ddf4ff")
                fontWeight = FontWeight.bold
            }

            ".pg-nav-library" {
                margin = Margin(12.px, 0.px, 4.px, 0.px)
                fontSize = 13.px
            }

            ".pg-nav-library small, .pg-nav-empty" {
                display = Display.block
                color = textMuted
                fontWeight = FontWeight.normal
            }

            ".pg-main" {
                flexGrow = number(1.0)
                minWidth = 0.px
                padding = Padding(16.px, 24.px)
            }

            ".pg-sample" {
                margin = Margin(0.px, 0.px, 32.px, 0.px)
                border = Border(1.px, solid, lineColor)
                borderRadius = 6.px
            }

            ".pg-sample-header" {
                padding = Padding(8.px, 12.px)
                backgroundColor = Color("#f6f8fa")
                borderBottom = Border(1.px, solid, lineColor)
            }

            ".pg-sample-header h2" {
                margin = Margin(0.px, 0.px, 4.px, 0.px)
                fontSize = 18.px
            }

            ".pg-meta" {
                display = Display.flex
                flexWrap = FlexWrap.wrap
                columnGap = 16.px
                rowGap = 2.px
                margin = 0.px
                fontSize = 12.px
                color = textMuted
            }

            ".pg-meta-item" {
                display = Display.flex
                columnGap = 4.px
            }

            ".pg-meta dt" {
                fontWeight = FontWeight.bold
            }

            ".pg-meta dd" {
                margin = 0.px
            }

            ".pg-sample-notes" {
                margin = Margin(4.px, 0.px, 0.px, 0.px)
                fontSize = 12.px
            }

            ".pg-sample-body" {
                padding = 12.px
            }

            ".pg-index" {
                width = 100.pct
                borderCollapse = BorderCollapse.collapse
                fontSize = 14.px
            }

            ".pg-index th, .pg-index td" {
                textAlign = TextAlign.left
                verticalAlign = VerticalAlign.top
                padding = Padding(4.px, 8.px)
                borderBottom = Border(1.px, solid, lineColor)
            }

            ".pg-index code, .pg-meta code" {
                fontFamily = FontFamily.monospace
                whiteSpace = WhiteSpace.nowrap
            }

            ".pg-not-found" {
                padding = Padding(8.px, 12.px)
                margin = Margin(0.px, 0.px, 16.px, 0.px)
                border = Border(1.px, solid, NamedColor.crimson)
                borderRadius = 6.px
            }

            ".pg-footer" {
                margin = Margin(32.px, 0.px, 0.px, 0.px)
                fontSize = 12.px
                color = textMuted
            }
        }
    }
}
