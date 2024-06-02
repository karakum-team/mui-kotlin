package karakum.mui.adapters

fun String.adaptOption(): String {
    return replace("declare const _default: OptionType;\n", "")
        .replace(
            """
export interface OptionTypeMap<OptionValue, AdditionalProps = {}, RootComponentType extends React.ElementType = 'li'> {
    props: OptionOwnProps<OptionValue> & AdditionalProps;
    defaultComponent: RootComponentType;
}""",
            "",
        ).replace(
            "export type OptionProps<OptionValue, RootComponentType extends React.ElementType = OptionTypeMap<OptionValue>['defaultComponent']> = PolymorphicProps<OptionTypeMap<OptionValue, {}, RootComponentType>, RootComponentType>;",
            "export interface OptionProps<OptionValue> extends OptionOwnProps<OptionValue> {\ncomponent?: D;\n}",
        ).replace(
            """
export interface OptionType {
    <OptionValue, RootComponentType extends React.ElementType = OptionTypeMap<OptionValue>['defaultComponent']>(props: PolymorphicProps<OptionTypeMap<OptionValue>, RootComponentType>): React.JSX.Element | null;
    propTypes?: any;
    displayName?: string | undefined;
}
""",
            "",
        )
}
