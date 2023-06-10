package karakum.mui

import karakum.mui.adapters.*

fun String.cleanup(): String {
    return adaptInputUnstyled()
        .cleanupOptionUnstyled()
        .cleanupSelectUnstyled()
        .cleanupFormControlLabelSlots()
        .cleanupStepLabelSlots()
        .adaptFormControlUnstyled()
        .adaptModal()
        .adaptUseAutocomplete()
        .adaptBreadcrumbs()
        .adaptUseSlider()
}

private fun String.cleanupOptionUnstyled(): String {
    return replace("declare const _default: OptionUnstyledType;\n", "")
        .replace(
            """
export interface OptionUnstyledTypeMap<TValue, P = {}, D extends React.ElementType = 'li'> {
    props: P & OptionUnstyledOwnProps<TValue>;
    defaultComponent: D;
}""",
            "",
        ).replace(
            "type OptionUnstyledProps<TValue, D extends React.ElementType = OptionUnstyledTypeMap<TValue>['defaultComponent']> = OverrideProps<OptionUnstyledTypeMap<TValue, {}, D>, D> &",
            "interface OptionUnstyledProps<TValue> extends OptionUnstyledOwnProps<TValue>",
        ).cleanupUnstyledType("Option")
}

private fun String.cleanupSelectUnstyled(): String {
    return replace(
        """
export interface SelectUnstyledTypeMap<TValue extends {}, Multiple extends boolean, P = {}, D extends React.ElementType = 'button'> {
    props: P & SelectUnstyledOwnProps<TValue, Multiple>;
    defaultComponent: D;
}""",
        "",
    ).replace(
        "type SelectUnstyledProps<TValue extends {}, Multiple extends boolean, D extends React.ElementType = SelectUnstyledTypeMap<TValue, Multiple>['defaultComponent']> = OverrideProps<SelectUnstyledTypeMap<TValue, Multiple, {}, D>, D> &",
        "interface SelectUnstyledProps<TValue> extends SelectUnstyledOwnProps<TValue>",
    ).cleanupUnstyledType("Select")
}

// TODO: Remove after full support of `UnstyledType`s
private fun String.cleanupUnstyledType(name: String): String {
    val source = substringAfter("export interface ${name}UnstyledType {\n", "")
        .substringBefore("propTypes?: any;\n}\n")

    if (source == "") return this

    return replace(source, "").replace(
        """
export interface ${name}UnstyledType {
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
