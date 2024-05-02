package karakum.mui.adapters

fun String.adaptSelect(): String {
    return replace(
        oldValue = "export type SelectVariants",
        newValue = "export type SelectVariant",
    ).replace(
        oldValue = "variant?: SelectVariants",
        newValue = "variant?: SelectVariant",
    ).replace(
        oldValue = "export interface BaseSelectProps<Value = unknown>",
        newValue = "export interface SelectProps<Value = unknown>",
    ).replace(
        """
export interface SelectTypeMap<OptionValue extends {}, Multiple extends boolean, AdditionalProps = {}, RootComponentType extends React.ElementType = 'button'> {
    props: SelectOwnProps<OptionValue, Multiple> & AdditionalProps;
    defaultComponent: RootComponentType;
}""",
        "",
    ).replace(
        oldValue = "export type SelectProps<OptionValue extends {}, Multiple extends boolean, RootComponentType extends React.ElementType = SelectTypeMap<OptionValue, Multiple>['defaultComponent']> = PolymorphicProps<SelectTypeMap<OptionValue, Multiple, {}, RootComponentType>, RootComponentType>;",
        newValue = "export interface SelectProps<OptionValue> extends SelectOwnProps<OptionValue> {\ncomponent?: D;\n}",
    ).replace(
        """
export interface SelectType {
    <OptionValue extends {}, Multiple extends boolean = false, RootComponentType extends React.ElementType = SelectTypeMap<OptionValue, Multiple>['defaultComponent']>(props: PolymorphicProps<SelectTypeMap<OptionValue, Multiple>, RootComponentType>): JSX.Element | null;
    propTypes?: any;
    displayName?: string | undefined;
}
""",
        "",
    )
}
