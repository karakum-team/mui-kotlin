package karakum.mui.adapters

// TODO: Remove when default function for `useAutocomplete` will be supported
fun String.adaptUseAutocomplete(): String {
    return replace(
        """export interface UseAutocompleteParameters<
  T,
  Multiple extends boolean | undefined,
  DisableClearable extends boolean | undefined,
  FreeSolo extends boolean | undefined,
> extends UseAutocompleteProps<T, Multiple, DisableClearable, FreeSolo> {}""",
        "",
    ).replace(
        """export interface UseAutocompleteRenderedOption<T> {
  option: T;
  index: number;
}

export interface UseAutocompleteReturnValue<
  T,
  Multiple extends boolean | undefined = false,
  DisableClearable extends boolean | undefined = false,
  FreeSolo extends boolean | undefined = false,
> {
  /**
   * Resolver for the root slot's props.
   * @param externalProps props for the root slot
   * @returns props that should be spread on the root slot
   */
  getRootProps: (externalProps?: any) => React.HTMLAttributes<HTMLDivElement>;
  /**
   * Resolver for the input element's props.
   * @returns props that should be spread on the input element
   */
  getInputProps: () => React.InputHTMLAttributes<HTMLInputElement>;
  /**
   * Resolver for the input label element's props.
   * @returns props that should be spread on the input label element
   */
  getInputLabelProps: () => Omit<React.HTMLAttributes<HTMLLabelElement>, 'color'>;
  /**
   * Resolver for the `clear` button element's props.
   * @returns props that should be spread on the *clear* button element
   */
  getClearProps: () => React.HTMLAttributes<HTMLButtonElement>;
  /**
   * Resolver for the popup icon's props.
   * @returns props that should be spread on the popup icon
   */
  getPopupIndicatorProps: () => React.HTMLAttributes<HTMLButtonElement>;
  /**
   * A tag props getter.
   */
  getTagProps: AutocompleteGetTagProps;
  /**
   * Resolver for the listbox component's props.
   * @returns props that should be spread on the listbox component
   */
  getListboxProps: () => React.HTMLAttributes<HTMLUListElement>;
  /**
   * Resolver for the rendered option element's props.
   * @param renderedOption option rendered on the Autocomplete
   * @returns props that should be spread on the li element
   */
  getOptionProps: (
    renderedOption: UseAutocompleteRenderedOption<T>,
  ) => React.HTMLAttributes<HTMLLIElement>;
  /**
   * Id for the Autocomplete.
   */
  id: string;
  /**
   * The input value.
   */
  inputValue: string;
  /**
   * The value of the autocomplete.
   */
  value: AutocompleteValue<T, Multiple, DisableClearable, FreeSolo>;
  /**
   * If `true`, the component input has some values.
   * @default false
   */
  dirty: boolean;
  /**
   * If `true`, the listbox is being displayed.
   * @default false
   */
  expanded: boolean;
  /**
   * If `true`, the popup is open on the component.
   * @default false
   */
  popupOpen: boolean;
  /**
   * If `true`, the component is focused.
   * @default false
   */
  focused: boolean;
  /**
   * An HTML element that is used to set the position of the component.
   */
  anchorEl: null | HTMLElement;
  /**
   * Setter for the component `anchorEl`.
   * @returns function for setting `anchorEl`
   */
  setAnchorEl: () => void;
  /**
   * Index of the focused tag for the component.
   * @default -1
   */
  focusedTag: number;
  /**
   * The options to render. It's either `T[]` or `AutocompleteGroupedOption<T>[]` if the groupBy prop is provided.
   */
  groupedOptions: T[] | Array<AutocompleteGroupedOption<T>>;
}
""",
        "",
    )
}
