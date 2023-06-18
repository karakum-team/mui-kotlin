package karakum.mui.adapters

import karakum.mui.cleanupType

fun String.adaptOption(): String {
    return replace("declare const _default: OptionType;\n", "")
        .replace(
            """
export interface OptionTypeMap<OptionValue, P = {}, D extends React.ElementType = 'li'> {
    props: P & OptionOwnProps<OptionValue>;
    defaultComponent: D;
}""",
            "",
        ).replace(
            "type OptionProps<OptionValue, D extends React.ElementType = OptionTypeMap<OptionValue>['defaultComponent']> = OverrideProps<OptionTypeMap<OptionValue, {}, D>, D> &",
            "interface OptionProps<OptionValue> extends OptionOwnProps<OptionValue>",
        ).cleanupType("Option")
}