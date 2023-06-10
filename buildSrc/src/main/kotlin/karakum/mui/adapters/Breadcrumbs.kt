package karakum.mui.adapters

fun String.adaptBreadcrumbs(): String {
    return replace(
        """
      /**
       * Props applied to the CollapsedIcon slot.
       * @default {}
       */
      collapsedIcon?: SlotComponentProps<
        typeof SvgIcon,
        BreadcrumbsCollapsedIconSlotPropsOverrides,
        BreadcrumbsOwnerState
      >;
""",
        """
      collapsedIcon?: SlotComponentProps<
        typeof SvgIcon,
        BreadcrumbsCollapsedIconSlotPropsOverrides,
        BreadcrumbsOwnerState
      >;
""",
    )
}
