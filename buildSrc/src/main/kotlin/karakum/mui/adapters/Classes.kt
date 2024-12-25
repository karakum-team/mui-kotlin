package karakum.mui.adapters

// TODO: Fix problem with incorrect comments processing for inline types
fun String.adaptClasses(): String {
    return cleanupTreeItemContentClasses()
}

private fun String.cleanupTreeItemContentClasses(): String {
    return replace(
        """
    classes: {
        /** Styles applied to the root element. */
        root: string;
        /** State class applied to the content element when expanded. */
        expanded: string;
        /** State class applied to the content element when selected. */
        selected: string;
        /** State class applied to the content element when focused. */
        focused: string;
        /** State class applied to the element when disabled. */
        disabled: string;
        /** Styles applied to the Tree Item icon and collapse/expand icon. */
        iconContainer: string;
        /** Styles applied to the label element. */
        label: string;
        /** Styles applied to the checkbox element. */
        checkbox: string;
        /** Styles applied to the input element that is visible when editing is enabled. */
        labelInput: string;
        /** Styles applied to the content element when editing is enabled. */
        editing: string;
        /** Styles applied to the content of the items that are editable. */
        editable: string;
    };
""",
        """
    classes: {
        root: string;
        expanded: string;
        selected: string;
        focused: string;
        disabled: string;
        iconContainer: string;
        label: string;
        checkbox: string;
        labelInput: string;
        editing: string;
        editable: string;
    };
""",
    )
}
