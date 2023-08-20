package karakum.mui.adapters

// TODO: Removing comments required
//  It can be deleted when MUI finishes separating `Slot` & `Component` props to separate interfaces
fun String.adaptComponentsAndSlots(): String {
    return cleanupFormControlLabelSlots()
        .cleanupStepLabelSlots()
        .cleanupBadgeOwnSlots()
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
