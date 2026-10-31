package playground.samples.baseui

import baseui.Toast
import baseui.ToastManagerAddOptions
import baseui.ToastManagerUpdateOptions
import baseui.className
import baseui.style
import emotion.react.css
import js.objects.unsafeJso
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.CSSProperties
import react.FC
import react.Key
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import web.cssom.Border
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.Display
import web.cssom.FlexDirection
import web.cssom.LineStyle
import web.cssom.Padding
import web.cssom.Position
import web.cssom.px
import web.dom.document

// The generator's existing Partial<Omit<ToastObject<Data>, ...>> gap leaves UpdateOptions empty.
// Keep that workaround local; the manager's callable object/callback overloads are fully generated.
private external interface SampleToastUpdate : ToastManagerUpdateOptions<Any> {
    var title: ReactNode?
    var description: ReactNode?
}

private fun toastUpdate(titleText: String, previousTitle: ReactNode? = null): SampleToastUpdate =
    unsafeJso {
        title = titleText.unsafeCast<ReactNode>()
        description = previousTitle
    }

private val myToastManager = Toast.createToastManager<Any>()

val ToastSample: Sample = Sample(
    id = "toast",
    library = Libraries.BaseUi,
    title = "Toast",
    kind = Kind.Check,
    covers = listOf(
        "Toast.Provider/Portal/Viewport/Root/Content/Title/Description/Close (8 of 11 parts)",
        "Toast.createToastManager / Toast.useToastManager",
        "ToastManager.add / update (object and callback, manager and hook)",
        "className/style state helpers",
    ),
    fileName = "ToastSample.kt",
    refs = listOf(
        Ref("BASE_UI_TODO.md", "https://github.com/karakum-team/mui-kotlin/blob/main/BASE_UI_TODO.md"),
        Ref("f296d5bd", "https://github.com/karakum-team/mui-kotlin/commit/f296d5bd"),
    ),
    notes = "An external manager drives `Toast.Provider`; the hook reads the list back, so `count` follows " +
        "add and close. Each of the four update buttons proves one manager/hook x object/callback combination " +
        "of the callable `update` reaches Base UI and rewrites the title. Action, Arrow, Positioner, manager " +
        "`promise` and anchored positioning are not exercised here.",
    content = FC<Props> {
        Toast.Provider {
            this.toastManager = myToastManager

            div {
                css {
                    padding = Padding(16.px, 16.px)
                }
                button {
                    onClick = {
                        myToastManager.add(unsafeJso<ToastManagerAddOptions<Props>> {
                            title = "Success".unsafeCast<ReactNode>()
                            description = "Your action was completed.".unsafeCast<ReactNode>()
                            type = "success"
                            // Keep the toast visible while exercising both update overloads.
                            timeout = 0
                        })
                    }
                    +"Show Toast"
                }

                ToastStatus()
            }

            val ToastViewport = Toast.Viewport

            Toast.Portal {
                container = document.body
                className { ClassName("toast-portal") }
                ToastViewport {
                    className = ClassName("toast-viewport")
                    style = {
                        unsafeJso<CSSProperties> {
                            position = Position.fixed
                            bottom = 16.px
                            right = 16.px
                            display = Display.flex
                            flexDirection = FlexDirection.column
                            gap = 8.px
                        }
                    }

                    ToastList {}
                }
            }
        }
    },
)

private val ToastStatus = FC<Props> {
    val manager = Toast.useToastManager<Any>()

    StatusLine {
        id = "toast-status"
        fields = listOf("count" to manager.toasts.size)
    }
}

private val ToastList = FC<Props> {
    val manager = Toast.useToastManager<Any>()

    val ToastRoot = Toast.Root
    val ToastContent = Toast.Content
    val ToastTitle = Toast.Title
    val ToastDescription = Toast.Description
    val ToastClose = Toast.Close

    for (toastObj in manager.toasts) {
        val toastId = toastObj.id.unsafeCast<String>()
        ToastRoot {
            key = toastObj.id.unsafeCast<Key>()
            this.toast = toastObj

            className {
                ClassName("toast-root")
            }

            style {
                unsafeJso<CSSProperties> {
                    padding = Padding(16.px, 16.px)
                    backgroundColor = Color("white")
                    border = Border(1.px, LineStyle.solid, Color("#ccc"))
                }
            }

            // These toasts stack in the viewport. Toast.Positioner is for anchored toasts and
            // requires its own toast prop; it is not a content wrapper inside Toast.Root.
            ToastContent {
                ToastTitle {
                    className = ClassName("toast-title")
                }
                ToastDescription {
                    className = ClassName("toast-description")
                }
                button {
                    onClick = { myToastManager.update(toastId, toastUpdate("Manager object")) }
                    +"Update toast (object)"
                }
                button {
                    onClick = {
                        myToastManager.update(toastId) { previous ->
                            toastUpdate("Manager callback", previous.title)
                        }
                    }
                    +"Update toast (callback)"
                }
                button {
                    onClick = { manager.update(toastId, toastUpdate("Hook object")) }
                    +"Hook update (object)"
                }
                button {
                    onClick = {
                        manager.update(toastId) { previous ->
                            toastUpdate("Hook callback", previous.title)
                        }
                    }
                    +"Hook update (callback)"
                }
                ToastClose {
                    className = ClassName("toast-close")
                    +"Close"
                }
            }
        }
    }
}
