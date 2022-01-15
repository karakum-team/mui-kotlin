package karakum.mui

internal fun String.toTypeParameter(): String =
    removeSurrounding("Partial<", ">")
        .replace("React.HTMLAttributes<", "react.dom.html.HTMLAttributes<")
        .replace("React.LabelHTMLAttributes<", "react.dom.html.LabelHTMLAttributes<")
        .replace("React.TextareaHTMLAttributes<", "react.dom.html.TextareaHTMLAttributes<")
        .replace("<HTMLElement>", "<org.w3c.dom.HTMLElement>")
        .replace("<HTMLDivElement>", "<org.w3c.dom.HTMLDivElement>")
        .replace("<HTMLTextAreaElement>", "<org.w3c.dom.HTMLTextAreaElement>")
        .replace("<HTMLSpanElement>", "<org.w3c.dom.HTMLSpanElement>")
        .replace("<HTMLUListElement>", "<org.w3c.dom.HTMLUListElement>")
        .replace("<HTMLLIElement>", "<org.w3c.dom.HTMLLIElement>")
        .replace("<HTMLHeadingElement>", "<org.w3c.dom.HTMLHeadingElement>")
        .replace("<HTMLLabelElement>", "<org.w3c.dom.HTMLLabelElement>")
        .replace("<HTMLInputElement | HTMLTextAreaElement>", "<org.w3c.dom.HTMLElement>")
        .replace("TypographyProps", "mui.material.TypographyProps")
        .replace("TransitionProps", "mui.material.transitions.TransitionProps")
