package karakum.mui

import karakum.mui.adapters.*

fun String.cleanup(): String {
    return adaptInput()
        .adaptOption()
        .adaptSelect()
        .cleanupFormControlLabelSlots()
        .cleanupStepLabelSlots()
        .adaptFormControl()
        .adaptModal()
        .adaptUseAutocomplete()
        .adaptBreadcrumbs()
        .adaptUseSlider()
}


fun String.cleanupType(name: String): String {
    val source = substringAfter("export interface ${name}Type {\n", "")
        .substringBefore("propTypes?: any;\n}\n")

    if (source == "") return this

    return replace(source, "").replace(
        """
export interface ${name}Type {
propTypes?: any;
}
""", ""
    )
}

private fun String.cleanupFormControlLabelSlots(): String {
    // TODO: Remove components after MUI completes migration to slots
    return replace(
        """
  componentsProps?: {
    /**
     * Props applied to the Typography wrapper of the passed label.
     * This is unused if disableTypography is true.
     * @default {}
     */
    typography?: TypographyProps;
  };
""",
        """
  componentsProps?: {
    typography?: TypographyProps;
  };
""",
    ).replace(
        """
  slotProps?: {
    /**
     * Props applied to the Typography wrapper of the passed label.
     * This is unused if disableTypography is true.
     * @default {}
     */
    typography?: TypographyProps;
  };
""",
        """
  slotProps?: {
    typography?: TypographyProps;
  };
""",
    )
}

private fun String.cleanupStepLabelSlots(): String {
    // TODO: Remove components after MUI completes migration to slots
    return replace(
        """
  componentsProps?: {
    /**
     * Props applied to the label element.
     * @default {}
     */
    label?: React.HTMLProps<HTMLSpanElement>;
  };
""",
        """
  componentsProps?: {
    label?: React.HTMLProps<HTMLSpanElement>;
  };
""",
    ).replace(
        """
  slotProps?: {
    /**
     * Props applied to the label element.
     * @default {}
     */
    label?: React.HTMLProps<HTMLSpanElement>;
  };
""",
        """
  slotProps?: {
    label?: React.HTMLProps<HTMLSpanElement>;
  };
""",
    )
}
