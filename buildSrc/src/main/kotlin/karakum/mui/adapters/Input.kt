package karakum.mui.adapters

fun String.adaptInput(): String {
    return replace(
        """
export interface SingleLineInputProps {
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
        """type InputOwnProps = (SingleLineInputProps | MultiLineInputProps) & Omit<UseInputParameters, 'error'> & {""",
        "interface InputOwnProps extends MultiLineInputProps {",
    ).replace(
        "};\nexport interface InputSlots {",
        "}\nexport interface InputSlots {"
    ).replace(
        "MultiLineInputProps",
        "InputBaseProps",
    )
}
