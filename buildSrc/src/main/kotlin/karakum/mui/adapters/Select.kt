package karakum.mui.adapters

fun String.adaptSelect(): String {
    return replace(
        """
export interface SelectTypeMap<OptionValue extends {}, Multiple extends boolean, AdditionalProps = {}, RootComponentType extends React.ElementType = 'button'> {
    props: SelectOwnProps<OptionValue, Multiple> & AdditionalProps;
    defaultComponent: RootComponentType;
}""",
        "",
    ).replace(
        "export type SelectProps<OptionValue extends {}, Multiple extends boolean, RootComponentType extends React.ElementType = SelectTypeMap<OptionValue, Multiple>['defaultComponent']> = PolymorphicProps<SelectTypeMap<OptionValue, Multiple, {}, RootComponentType>, RootComponentType>;",
        "export interface SelectProps<OptionValue> extends SelectOwnProps<OptionValue> {\ncomponent?: D;\n}",
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

// TODO: Was needed for mui-material 5.11.3 but was reverted bu mui team in 5.11.4
//  Need to check will it be needed in future
private fun String.additionalAdaptSelect(): String {
    return replace(
        """
interface CommonProps<T>
""",
        """
export interface SelectProps<Value = unknown>
""",
    ).replace(
        """
}

type ConditionalRenderValueType<T> =
  | {
      /**
       * If `true`, a value is displayed even if no items are selected.
       *
       * In order to display a meaningful value, a function can be passed to the `renderValue` prop which
       * returns the value to be displayed when no items are selected.
       *
       * ⚠️ When using this prop, make sure the label doesn't overlap with the empty displayed value.
       * The label should either be hidden or forced to a shrunk state.
       * @default false
       */
      displayEmpty?: false;
      /**
       * If `true`, `value` must be an array and the menu will support multiple selections.
       * @default false
       */
      multiple?: boolean;
      /**
       * Render the selected value.
       * You can only use it when the `native` prop is `false` (default).
       *
       * @param {any} value The `value` provided to the component.
       * @returns {ReactNode}
       */
      renderValue?: (value: T) => React.ReactNode;
    }
  | {
      /**
       * If `true`, a value is displayed even if no items are selected.
       *
       * In order to display a meaningful value, a function can be passed to the `renderValue` prop which
       * returns the value to be displayed when no items are selected.
       *
       * ⚠️ When using this prop, make sure the label doesn't overlap with the empty displayed value.
       * The label should either be hidden or forced to a shrunk state.
       * @default false
       */
      displayEmpty: true;
      /**
       * If `true`, `value` must be an array and the menu will support multiple selections.
       * @default false
       */
      multiple?: false;
      /**
       * Render the selected value.
       * You can only use it when the `native` prop is `false` (default).
       *
       * @param {any} value The `value` provided to the component.
       * @returns {ReactNode}
       */
      renderValue?: (value: T | '') => React.ReactNode;
    }
  | {
      /**
       * If `true`, a value is displayed even if no items are selected.
       *
       * In order to display a meaningful value, a function can be passed to the `renderValue` prop which
       * returns the value to be displayed when no items are selected.
       *
       * ⚠️ When using this prop, make sure the label doesn't overlap with the empty displayed value.
       * The label should either be hidden or forced to a shrunk state.
       * @default false
       */
      displayEmpty: true;
      /**
       * If `true`, `value` must be an array and the menu will support multiple selections.
       * @default false
       */
      multiple: true;
      /**
       * Render the selected value.
       * You can only use it when the `native` prop is `false` (default).
       *
       * @param {any} value The `value` provided to the component.
       * @returns {ReactNode}
       */
      renderValue?: (value: T) => React.ReactNode;
    };
""",
        """
  /**
   * If `true`, a value is displayed even if no items are selected.
   *
   * In order to display a meaningful value, a function can be passed to the `renderValue` prop which
   * returns the value to be displayed when no items are selected.
   *
   * ⚠️ When using this prop, make sure the label doesn't overlap with the empty displayed value.
   * The label should either be hidden or forced to a shrunk state.
   * @default false
   */
  displayEmpty?: boolean;
  /**
   * If `true`, `value` must be an array and the menu will support multiple selections.
   * @default false
   */
  multiple?: boolean;
  /**
   * Render the selected value.
   * You can only use it when the `native` prop is `false` (default).
   *
   * @param {any} value The `value` provided to the component.
   * @returns {ReactNode}
   */
  renderValue?: (value: T) => React.ReactNode;
};
""",
    )
}
