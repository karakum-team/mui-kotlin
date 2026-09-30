import baseui.*
import emotion.react.css
import js.objects.unsafeJso
import react.CSSProperties
import react.FC
import react.Props
import react.ReactNode
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import web.cssom.*
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

val myToastManager = Toast.createToastManager<Any>()

val BaseUiToast = FC<Props> {
    Toast.Provider {
        this.toastManager = myToastManager

        div {
            css {
                padding = Padding(16.px, 16.px)
            }
            button {
                onClick = {
                    myToastManager.add(unsafeJso<ToastManagerAddOptions<Props>> {
                        title = "Success".unsafeCast<react.ReactNode>()
                        description = "Your action was completed.".unsafeCast<react.ReactNode>()
                        type = "success"
                        // Keep the toast visible while exercising both update overloads.
                        timeout = 0
                    })
                }
                +"Show Toast"
            }
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
}

private val ToastList = FC<Props> {
    val manager = Toast.useToastManager<Any>()
    val toastsArray = manager.toasts.unsafeCast<Array<ToastObject<Any>>>()

    val ToastRoot = Toast.Root
    val ToastContent = Toast.Content
    val ToastTitle = Toast.Title
    val ToastDescription = Toast.Description
    val ToastClose = Toast.Close

    for (toastObj in toastsArray) {
        val toastId = toastObj.id.unsafeCast<String>()
        ToastRoot {
            key = toastObj.id.unsafeCast<react.Key>()
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
