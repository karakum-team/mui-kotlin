import baseui.*
import emotion.react.css
import js.objects.unsafeJso
import react.CSSProperties
import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import web.cssom.*

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
                    })
                }
                +"Show Toast"
            }
        }

        val ToastPortal = Toast.Portal.unsafeCast<FC<react.PropsWithChildren>>()
        val ToastViewport = Toast.Viewport

        ToastPortal {
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
    val toastsArray = Toast.useToastManager<Any>().toasts.unsafeCast<Array<ToastObject<Any>>>()

    val ToastRoot = Toast.Root
    val ToastContent = Toast.Content
    val ToastTitle = Toast.Title
    val ToastDescription = Toast.Description
    val ToastClose = Toast.Close

    for (toastObj in toastsArray) {
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
                ToastClose {
                    className = ClassName("toast-close")
                    +"Close"
                }
            }
        }
    }
}
