package karakum.mui.adapters

/**
 * Components whose props `Omit` the parent's `slots`/`slotProps` and mix in their own `XxxSlotsAndSlotProps`:
 *   export interface DialogProps extends StandardProps<ModalProps, 'children' | 'slots' | 'slotProps'>, DialogSlotsAndSlotProps
 * Kotlin cannot `Omit`, so `XxxProps` inherits both declarations; [adaptOwnSlotsAndSlotProps] re-declares
 * them with the component's own types, and `Converter.kt::findAdditionalProps` marks them `override`. Their
 * types differ from the parent's (`DialogSlots?` vs `ModalSlots?`), so these files suppress
 * `VAR_TYPE_MISMATCH_ON_OVERRIDE` (`Override.kt`). FilledInput is absent on purpose: in v9 it really uses
 * InputBase's slots.
 */
internal val OWN_SLOTS_AND_SLOT_PROPS_COMPONENTS = setOf(
    // Modal → Dialog / Drawer / Popover, Popover → Menu, Drawer → SwipeableDrawer
    "Dialog",
    "Drawer",
    "Popover",
    "Menu",
    "SwipeableDrawer",
    // InputBase → OutlinedInput
    "OutlinedInput",
    // SwitchBase → Checkbox / Radio / Switch
    "Checkbox",
    "Radio",
    "Switch",
)

private val NAMES = OWN_SLOTS_AND_SLOT_PROPS_COMPONENTS.joinToString("|")

fun String.adaptOwnSlotsAndSlotProps(): String =
    // Two of them intersect their own slots with the parent's. Keep only the own `CreateSlotsAndSlotProps`
    // so the alias converter emits `XxxSlotProps`; the parent's slots come back as supertypes of the
    // own interfaces (`SwipeableDrawerSlots : DrawerSlots` here, the rest in `Overrides.kt`).
    replace(
        "type SwipeableDrawerSlotsAndSlotProps = DrawerSlotsAndSlotProps & CreateSlotsAndSlotProps<",
        "type SwipeableDrawerSlotsAndSlotProps = CreateSlotsAndSlotProps<",
    )
        .replace("export interface SwipeableDrawerSlots {", "export interface SwipeableDrawerSlots extends DrawerSlots {")
        .replace(
            Regex("""(export interface ($NAMES)Props extends [^{]*?\b\2SlotsAndSlotProps) \{\n"""),
            "$1 {\n  slots?: $2Slots | undefined;\n  slotProps?: $2SlotProps | undefined;\n",
        )
