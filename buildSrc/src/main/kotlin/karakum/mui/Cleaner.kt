package karakum.mui

fun String.cleanup(): String {
    return cleanupInputUnstyled()
        .cleanupMultiSelectUnstyled()
        .cleanupOptionUnstyled()
        .cleanupSelectUnstyled()
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
        """export declare type InputUnstyledOwnProps = (SingleLineInputUnstyledProps | MultiLineInputUnstyledProps) & UseInputParameters & {""",
        "export interface InputUnstyledOwnProps extends MultiLineInputUnstyledProps {",
    ).replace(
        "};\nexport interface InputUnstyledTypeMap<",
        "}\nexport interface InputUnstyledTypeMap<"
    )
}
