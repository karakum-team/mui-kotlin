package karakum.mui

internal fun String.toTypeParameter(): String =
    removeSurrounding("Partial<", ">")
        .replace("React.HTMLAttributes<", "react.dom.html.HTMLAttributes<")
        .replace("React.LabelHTMLAttributes<", "react.dom.html.LabelHTMLAttributes<")
        .replace("React.TextareaHTMLAttributes<", "react.dom.html.TextareaHTMLAttributes<")
        .replace("<HTMLElement>", "<web.html.HTMLElement>")
        .replace("<HTMLDivElement>", "<web.html.HTMLDivElement>")
        .replace("<HTMLTextAreaElement>", "<web.html.HTMLTextAreaElement>")
        .replace("<HTMLSpanElement>", "<web.html.HTMLSpanElement>")
        .replace("<HTMLUListElement>", "<web.html.HTMLUListElement>")
        .replace("<HTMLLIElement>", "<web.html.HTMLLIElement>")
        .replace("<HTMLHeadingElement>", "<web.html.HTMLHeadingElement>")
        .replace("<HTMLLabelElement>", "<web.html.HTMLLabelElement>")
        .replace("<HTMLInputElement | HTMLTextAreaElement>", "<web.html.HTMLElement>")
        .replace("TypographyProps", "mui.material.TypographyProps")
        .replace("TransitionProps", "mui.material.transitions.TransitionProps")
