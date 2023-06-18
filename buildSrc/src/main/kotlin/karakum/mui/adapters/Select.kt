package karakum.mui.adapters

import karakum.mui.cleanupType

fun String.adaptSelect(): String {
    return replace(
        """
export interface SelectTypeMap<OptionValue extends {}, Multiple extends boolean, P = {}, D extends React.ElementType = 'button'> {
    props: P & SelectOwnProps<OptionValue, Multiple>;
    defaultComponent: D;
}""",
        "",
    ).replace(
        "type SelectProps<OptionValue extends {}, Multiple extends boolean, D extends React.ElementType = SelectTypeMap<OptionValue, Multiple>['defaultComponent']> = OverrideProps<SelectTypeMap<OptionValue, Multiple, {}, D>, D> &",
        "interface SelectProps<OptionValue> extends SelectOwnProps<OptionValue>",
    ).cleanupType("Select")
}

// TODO: Was needed for mui-material 5.11.3 but was reverted bu mui team in 5.11.4
//  Need to check will it be needed in future
private fun String.additionalAdaptSelect(): String {
    return replace(
        """
interface CommonProps<T>
""",
        """
export interface SelectProps<T = unknown>
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
