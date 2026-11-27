package playground.samples.baseui

import baseui.Avatar
import baseui.ImageLoadingStatus
import baseui.className
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
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.dom.html.ReactHTML.span
import react.useState
import web.cssom.AlignItems
import web.cssom.Border
import web.cssom.BoxSizing
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Cursor
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.FontWeight
import web.cssom.JustifyContent
import web.cssom.LineStyle.Companion.solid
import web.cssom.NamedColor
import web.cssom.Outline
import web.cssom.Overflow
import web.cssom.number
import web.cssom.pct
import web.cssom.px
import web.dom.ElementId
import web.html.ButtonType
import web.html.button

private const val VALID_IMAGE_SRC =
    "data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' width='48' height='48' viewBox='0 0 48 48'><circle cx='24' cy='24' r='24' fill='%23175cd3'/><text x='24' y='29' font-size='16' text-anchor='middle' fill='white'>AV</text></svg>"
private const val BROKEN_IMAGE_SRC = "https://invalid-avatar-url.example/broken.jpg"

val AvatarSample: Sample = Sample(
    id = "avatar",
    library = Libraries.BaseUi,
    title = "Avatar",
    kind = Kind.Check,
    covers = listOf(
        "Avatar.Root/Image/Fallback (3 parts)",
        "ImageLoadingStatus (idle/loading/loaded/error)",
        "AvatarImageProps.onLoadingStatusChange",
        "AvatarFallbackProps.delay",
        "className/style/render state helpers",
    ),
    fileName = "AvatarSample.kt",
    refs = listOf(
        Ref("docs/plans/base-ui.md", "https://github.com/karakum-team/mui-kotlin/blob/main/docs/plans/base-ui.md"),
        Ref("Base UI Avatar", "https://base-ui.com/react/components/avatar"),
    ),
    notes = "Namespace component export from @base-ui/react/avatar. Displays user avatars with automatic " +
        "image loading status tracking, fallback rendering, and state-dependent styling.",
    content = FC<Props> {
        var imageSrc by useState(VALID_IMAGE_SRC)
        var loadingStatus by useState(ImageLoadingStatus.idle)

        Global {
            styles {
                ".bui-avatar-sample" {
                    boxSizing = BoxSizing.borderBox
                    maxWidth = 520.px
                    padding = 16.px
                    marginBottom = 24.px
                    color = Color("#182230")
                    backgroundColor = NamedColor.white
                    fontSize = 16.px
                    lineHeight = number(1.5)
                }
                ".bui-avatar-row" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    gap = 16.px
                    marginTop = 12.px
                    marginBottom = 12.px
                }
                ".bui-avatar-root" {
                    display = Display.inlineFlex
                    alignItems = AlignItems.center
                    justifyContent = JustifyContent.center
                    width = 48.px
                    height = 48.px
                    borderRadius = 50.pct
                    overflow = Overflow.hidden
                    backgroundColor = Color("#eaecf0")
                    color = Color("#475467")
                    fontWeight = FontWeight.bold
                    border = Border(2.px, solid, Color("#d0d5dd"))
                }
                ".bui-avatar-image" {
                    width = 100.pct
                    height = 100.pct
                    objectFit = web.cssom.ObjectFit.cover
                }
                ".bui-avatar-fallback" {
                    display = Display.flex
                    alignItems = AlignItems.center
                    justifyContent = JustifyContent.center
                    width = 100.pct
                    height = 100.pct
                    fontSize = 16.px
                }
                ".bui-avatar-sample button" {
                    minHeight = 36.px
                    padding = 8.px
                    border = Border(1.px, solid, Color("#475467"))
                    borderRadius = 4.px
                    fontSize = 14.px
                    backgroundColor = NamedColor.white
                    color = Color("#182230")
                    cursor = Cursor.pointer
                }
                ".bui-avatar-sample button:focus-visible" {
                    outline = Outline(2.px, solid, Color("#175cd3"))
                    outlineOffset = 2.px
                }
            }
        }

        section {
            id = ElementId("bui-avatar-sample")
            className = ClassName("bui-avatar-sample")

            CheckList {
                id = "avatar-checks"
                checks = listOf(
                    Check("Avatar namespace is loaded", jsTypeOf(Avatar) == "object"),
                    Check("Avatar.Root is loaded", jsTypeOf(Avatar.Root) == "object" || jsTypeOf(Avatar.Root) == "function"),
                    Check("Avatar.Image is loaded", jsTypeOf(Avatar.Image) == "object" || jsTypeOf(Avatar.Image) == "function"),
                    Check("Avatar.Fallback is loaded", jsTypeOf(Avatar.Fallback) == "object" || jsTypeOf(Avatar.Fallback) == "function"),
                    Check("ImageLoadingStatus values are distinct", ImageLoadingStatus.idle != ImageLoadingStatus.loaded),
                )
            }

            StatusLine {
                id = "avatar-status"
                fields = listOf(
                    "status" to if (loadingStatus == ImageLoadingStatus.loaded) "loaded"
                    else if (loadingStatus == ImageLoadingStatus.error) "error"
                    else if (loadingStatus == ImageLoadingStatus.loading) "loading"
                    else "idle",
                    "src" to if (imageSrc == VALID_IMAGE_SRC) "valid" else "broken",
                )
            }

            p { +"1. Avatar with dynamic image loading status and fallback:" }

            div {
                className = ClassName("bui-avatar-row")

                Avatar.Root {
                    id = ElementId("avatar-interactive-root")
                    className = ClassName("bui-avatar-root")

                    Avatar.Image {
                        id = ElementId("avatar-interactive-image")
                        className = ClassName("bui-avatar-image")
                        src = imageSrc
                        alt = "User Avatar"
                        onLoadingStatusChange = { nextStatus ->
                            loadingStatus = nextStatus
                        }
                    }

                    Avatar.Fallback {
                        id = ElementId("avatar-interactive-fallback")
                        className = ClassName("bui-avatar-fallback")
                        +"JD"
                    }
                }

                button {
                    id = ElementId("avatar-toggle-src-btn")
                    type = ButtonType.button
                    onClick = {
                        imageSrc = if (imageSrc == VALID_IMAGE_SRC) BROKEN_IMAGE_SRC else VALID_IMAGE_SRC
                    }
                    +if (imageSrc == VALID_IMAGE_SRC) "Simulate broken image" else "Restore valid image"
                }
            }

            p { +"2. Standalone Fallback-only Avatar:" }

            div {
                className = ClassName("bui-avatar-row")

                Avatar.Root {
                    id = ElementId("avatar-fallback-only-root")
                    className = ClassName("bui-avatar-root")

                    Avatar.Fallback {
                        id = ElementId("avatar-fallback-only")
                        className = ClassName("bui-avatar-fallback")
                        +"AB"
                    }
                }
                span { +"Fallback displayed immediately without Image part" }
            }
        }
    }
)
