package karakum.mui

fun String.cleanup(): String {
    return cleanupInputUnstyled()
        .cleanupMultiSelectUnstyled()
        .cleanupOptionUnstyled()
        .cleanupSelectUnstyled()
        .cleanupFormControlLabelSlots()
        .cleanupStepLabelSlots()
}

// TODO: Remove when `ButtonHTMLAttributes` will support Type parameter for `value` (and `defaultValue` in common)
private fun String.cleanupMultiSelectUnstyled(): String {
    return replace(
        """
export interface MultiSelectUnstyledTypeMap<TValue extends {}, P = {}, D extends React.ElementType = 'button'> {
    props: P & MultiSelectUnstyledOwnProps<TValue>;
    defaultComponent: D;
}""",
        "",
    ).replace(
        "declare type MultiSelectUnstyledProps<TValue extends {}, D extends React.ElementType = MultiSelectUnstyledTypeMap<TValue>['defaultComponent']> = OverrideProps<MultiSelectUnstyledTypeMap<TValue, {}, D>, D> &",
        "interface MultiSelectUnstyledProps<TValue> extends MultiSelectUnstyledOwnProps<TValue>",
    ).cleanupUnstyledType("MultiSelect")
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
            "declare type OptionUnstyledProps<TValue, D extends React.ElementType = OptionUnstyledTypeMap<TValue>['defaultComponent']> = OverrideProps<OptionUnstyledTypeMap<TValue, {}, D>, D> &",
            "interface OptionUnstyledProps<TValue> extends OptionUnstyledOwnProps<TValue>",
        ).cleanupUnstyledType("Option")
}

private fun String.cleanupSelectUnstyled(): String {
    return replace(
        """
export interface SelectUnstyledTypeMap<TValue extends {}, P = {}, D extends React.ElementType = 'button'> {
    props: P & SelectUnstyledOwnProps<TValue>;
    defaultComponent: D;
}""",
        "",
    ).replace(
        "declare type SelectUnstyledProps<TValue extends {}, D extends React.ElementType = SelectUnstyledTypeMap<TValue>['defaultComponent']> = OverrideProps<SelectUnstyledTypeMap<TValue, {}, D>, D> &",
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

private fun String.cleanupInputUnstyled(): String {
    return replace(
        """
export interface SingleLineInputUnstyledProps {
    /**
     * Maximum number of rows to display when multiline option is set to true.
     */
    maxRows?: undefined;
    /**
     * Minimum number of rows to display when multiline option is set to true.
     */
    minRows?: undefined;
    /**
     * If `true`, a `textarea` element is rendered.
     * @default false
     */
    multiline?: false;
    /**
     * Number of rows to display when multiline option is set to true.
     */
    rows?: undefined;
    /**
     * Type of the `input` element. It should be [a valid HTML5 input type](https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input#Form_%3Cinput%3E_types).
     * @default 'text'
     */
    type?: React.HTMLInputTypeAttribute;
}""",
        "",
    ).replace(
        """export declare type InputUnstyledOwnProps = (SingleLineInputUnstyledProps | MultiLineInputUnstyledProps) & Omit<UseInputParameters, 'error'> & {""",
        "export interface InputUnstyledOwnProps extends MultiLineInputUnstyledProps {",
    ).replace(
        "};\nexport interface InputUnstyledTypeMap<",
        "}\nexport interface InputUnstyledTypeMap<"
    ).replace(
        "MultiLineInputUnstyledProps",
        "InputUnstyledBaseProps",
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
