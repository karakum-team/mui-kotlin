package karakum.mui.adapters

// TODO: Fix problem with incorrect comments processing for inline types
fun String.adaptComponentsAndSlots(): String {
    return cleanupFormControlLabelSlots()
        .cleanupStepLabelSlots()
        .cleanupBadgeOwnSlots()
        .cleanupDeprecatedComponentsProps()
}

// v7 types the deprecated `componentsProps` of Badge/Popper as an indexed-access of the own props'
// `slotProps` member (`BadgeOwnProps['slotProps']`, `BasePopperProps['slotProps']`). The generator
// renders an indexed access as a member reference (`BadgeOwnProps.SlotProps`), but no such nested
// `SlotProps` type exists, so it is unresolved. The prop is deprecated; collapse it to `any` (same
// `Any?` other deprecated `componentsProps` end up as).
private fun String.cleanupDeprecatedComponentsProps(): String {
    return replace(
        "componentsProps?: BadgeOwnProps['slotProps'] | undefined;",
        "componentsProps?: any;",
    ).replace(
        "componentsProps?: BasePopperProps['slotProps'] | undefined;",
        "componentsProps?: any;",
    )
}

private fun String.cleanupFormControlLabelSlots(): String {
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

private fun String.cleanupBadgeOwnSlots(): String {
    return replace(
        """
  slots?: {
    /**
     * The component that renders the root.
     * @default 'span'
     */
    root?: React.ElementType;
    /**
     * The component that renders the badge.
     * @default 'span'
     */
    badge?: React.ElementType;
  };
""",
        """
  slots?: {
    root?: React.ElementType;
    badge?: React.ElementType;
  };
""",
    )
}
