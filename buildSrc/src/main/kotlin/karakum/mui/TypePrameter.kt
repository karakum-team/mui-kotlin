package karakum.mui

internal fun String.toTypeParameter(): String =
    removeSurrounding("Partial<", ">")
        .replace("React.HTMLAttributes<", "react.dom.html.HTMLAttributes<")
        .replace("React.LabelHTMLAttributes<", "react.dom.html.LabelHTMLAttributes<")
        .replace("React.TextareaHTMLAttributes<", "react.dom.html.TextareaHTMLAttributes<")
        .replace("<HTMLElement>", "<dom.html.HTMLElement>")
        .replace("<HTMLDivElement>", "<dom.html.HTMLDivElement>")
        .replace("<HTMLTextAreaElement>", "<dom.html.HTMLTextAreaElement>")
        .replace("<HTMLSpanElement>", "<dom.html.HTMLSpanElement>")
        .replace("<HTMLUListElement>", "<dom.html.HTMLUListElement>")
        .replace("<HTMLLIElement>", "<dom.html.HTMLLIElement>")
        .replace("<HTMLHeadingElement>", "<dom.html.HTMLHeadingElement>")
        .replace("<HTMLLabelElement>", "<dom.html.HTMLLabelElement>")
        .replace("<HTMLInputElement | HTMLTextAreaElement>", "<dom.html.HTMLElement>")
        .replace("TypographyProps", "mui.material.TypographyProps")
        .replace("TransitionProps", "mui.material.transitions.TransitionProps")
