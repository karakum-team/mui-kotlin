package karakum.mui

internal val OVERRIDE_FIX_REQUIRED = setOf(
    "Chip", // children
    "RadioGroup", // defaultValue, onChange
    "Rating", // defaultValue, onChange
    "SvgIcon",
    "MasonryItem", // children
    "Tab", // children
    "ToggleButton", // onClick

    "TimelineContent",
    "TimelineOppositeContent",
    "AppBar",
    "Accordion",
    "Alert",
    "Card",
    "Checkbox",
    "FilledInput",
    "ImageListItemBar",
    "Input",
    "InputBase",
    "InputLabel",
    "Menu",
    "MobileStepper",
    "NativeSelect",
    "OutlinedInput",
    "Radio",
    "Select",
    "Snackbar",
    "SnackbarContent",
    "SpeedDialAction",
    "Stepper",
    "Switch",
    "SwitchBase",
    "Tooltip",

    "Badge",
    "Dialog",
    "Drawer",
    "Modal",
    "Popover",
    "SwipeableDrawer",

    "PickersDay",
)

internal val VAR_TYPE_MISMATCH_ON_OVERRIDE_FIX_REQUIRED = setOf(
    "CardHeader",
    "BottomNavigationAction",
    "StepButton",
    "CardActionArea",
    "Fab",
    "IconButton",
    "DialogTitle",
    "DialogContentText",
    "TabScrollButton",
)
