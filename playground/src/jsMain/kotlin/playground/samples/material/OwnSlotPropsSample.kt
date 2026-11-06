package playground.samples.material

import js.objects.unsafeJso
import kotlinx.coroutines.delay
import mui.material.Checkbox
import mui.material.CheckboxSlotProps
import mui.material.Dialog
import mui.material.DialogSlotProps
import mui.material.Drawer
import mui.material.DrawerSlotProps
import mui.material.DrawerVariant
import mui.material.Menu
import mui.material.MenuItem
import mui.material.MenuListProps
import mui.material.MenuSlotProps
import mui.material.OutlinedInput
import mui.material.OutlinedInputSlotProps
import mui.material.PaperProps
import mui.material.Popover
import mui.material.PopoverSlotProps
import mui.material.Radio
import mui.material.RadioSlotProps
import mui.material.SwipeableDrawer
import mui.material.SwipeableDrawerSlotProps
import mui.material.Switch
import mui.material.SwitchSlotProps
import playground.shell.Check
import playground.shell.CheckList
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.CSSProperties
import react.FC
import react.Props
import react.dom.html.FieldsetHTMLAttributes
import react.dom.html.HTMLAttributes
import react.dom.html.InputHTMLAttributes
import react.dom.html.ReactHTML.div
import react.useEffectOnce
import react.useState
import web.cssom.ClassName
import web.cssom.Position
import web.dom.ElementId
import web.dom.document
import web.html.HTMLDivElement
import web.html.HTMLFieldSetElement
import web.html.HTMLInputElement
import web.html.HTMLSpanElement

private const val ROOT_ID = "own-slot-props"
private const val DIALOG_LABEL_ID = "$ROOT_ID-dialog-label"
private const val DIALOG_DESCRIPTION_ID = "$ROOT_ID-dialog-description"

// Each component marks the element its own slot renders; the checks look the markers up inside the sample.
private val MARKERS = listOf(
    "Dialog.slotProps.paper" to "own-slot-props-dialog-paper",
    "Menu.slotProps.list" to "own-slot-props-menu-list",
    "Popover.slotProps.paper" to "own-slot-props-popover-paper",
    "Drawer.slotProps.paper" to "own-slot-props-drawer-paper",
    "SwipeableDrawer.slotProps.swipeArea" to "own-slot-props-swipe-area",
    "OutlinedInput.slotProps.notchedOutline" to "own-slot-props-notched-outline",
    "Checkbox.slotProps.input" to "own-slot-props-checkbox-input",
    "Radio.slotProps.input" to "own-slot-props-radio-input",
    "Switch.slotProps.thumb" to "own-slot-props-switch-thumb",
)

private fun marker(slot: String): ClassName =
    ClassName(MARKERS.first { it.first == slot }.second)

private fun paper(slot: String): PaperProps =
    unsafeJso { className = marker(slot) }

val OwnSlotPropsSample: Sample = Sample(
    id = "own-slot-props",
    library = Libraries.Material,
    title = "Own slotProps over the parent's",
    kind = Kind.Check,
    covers = listOf(
        "DialogProps.slotProps: DialogSlotProps (over ModalProps)",
        "MenuProps.slotProps: MenuSlotProps (over PopoverProps)",
        "PopoverProps / DrawerProps.slotProps (over ModalProps)",
        "SwipeableDrawerProps.slotProps: SwipeableDrawerSlotProps : DrawerSlotProps",
        "OutlinedInputProps.slotProps: OutlinedInputSlotProps : InputBaseProps.SlotProps",
        "Checkbox / Radio / SwitchProps.slotProps (over SwitchBaseProps)",
        "DialogProps.ariaDescribedBy / ariaLabelledBy / ariaModal overrides",
        "CheckboxProps.checked / disabled overrides",
    ),
    fileName = "OwnSlotPropsSample.kt",
    notes = "Only running this proves that the re-declared `slotProps` reach each component's OWN slots — the ones " +
            "TypeScript gets by `Omit`-ing the parent's (`Modal`, `Popover`, `InputBase`, `SwitchBase`): every slot " +
            "below sets a marker class that must land on the element that slot renders. Dialog, Menu and Popover " +
            "stay closed with `keepMounted` so nothing overlays the page. The Dialog ARIA overrides and inherited " +
            "Checkbox flags are also checked on their DOM elements.",
    content = FC<Props> {
        // Null until the swipe area mounts: SwipeableDrawer renders it inside `NoSsr`, after the first commit.
        var checks by useState<List<Check>?>(null)

        useEffectOnce {
            delay(100)
            val root = document.getElementById(ElementId(ROOT_ID))
            val dialog = root?.querySelector(".${marker("Dialog.slotProps.paper")}")
            val checkbox = root?.querySelector(".${marker("Checkbox.slotProps.input")}")
            checks = MARKERS.map { (slot, className) ->
                Check("$slot reaches its element", root?.querySelector(".$className") != null)
            } + listOf(
                Check("Dialog.ariaDescribedBy uses aria-describedby", dialog?.getAttribute("aria-describedby") == DIALOG_DESCRIPTION_ID),
                Check("Dialog.ariaLabelledBy uses aria-labelledby", dialog?.getAttribute("aria-labelledby") == DIALOG_LABEL_ID),
                Check("Dialog.ariaModal uses aria-modal", dialog?.getAttribute("aria-modal") == "false"),
                Check("Checkbox.checked reaches its input", checkbox?.matches(":checked") == true),
                Check("Checkbox.disabled reaches its input", checkbox?.matches(":disabled") == true),
            )
        }

        div {
            id = ElementId(ROOT_ID)

            Dialog {
                open = false
                keepMounted = true
                disablePortal = true
                ariaDescribedBy = ElementId(DIALOG_DESCRIPTION_ID)
                ariaLabelledBy = ElementId(DIALOG_LABEL_ID)
                ariaModal = false
                slotProps = unsafeJso<DialogSlotProps> {
                    paper = paper("Dialog.slotProps.paper")
                }

                div {
                    id = ElementId(DIALOG_LABEL_ID)
                    +"Dialog label"
                }
                div {
                    id = ElementId(DIALOG_DESCRIPTION_ID)
                    +"Dialog description"
                }
            }

            Menu {
                open = false
                keepMounted = true
                disablePortal = true
                slotProps = unsafeJso<MenuSlotProps> {
                    list = unsafeJso<MenuListProps> { className = marker("Menu.slotProps.list") }
                }

                MenuItem { +"Item" }
            }

            Popover {
                open = false
                keepMounted = true
                disablePortal = true
                slotProps = unsafeJso<PopoverSlotProps> {
                    paper = paper("Popover.slotProps.paper")
                }
            }

            Drawer {
                variant = DrawerVariant.permanent
                slotProps = unsafeJso<DrawerSlotProps> {
                    paper = unsafeJso<PaperProps> {
                        className = marker("Drawer.slotProps.paper")
                        style = unsafeJso<CSSProperties> { position = Position.relative }
                    }
                }
                +"Permanent drawer"
            }

            SwipeableDrawer {
                open = false
                onOpen = {}
                onClose = {}
                slotProps = unsafeJso<SwipeableDrawerSlotProps> {
                    swipeArea = unsafeJso<HTMLAttributes<HTMLDivElement>> {
                        className = marker("SwipeableDrawer.slotProps.swipeArea")
                    }
                    // Inherited from DrawerSlotProps: TS intersects both slot sets.
                    paper = unsafeJso<PaperProps> { square = true }
                }
            }

            OutlinedInput {
                slotProps = unsafeJso<OutlinedInputSlotProps> {
                    notchedOutline = unsafeJso<FieldsetHTMLAttributes<HTMLFieldSetElement>> {
                        className = marker("OutlinedInput.slotProps.notchedOutline")
                    }
                    // Inherited from InputBaseProps.SlotProps: TS intersects both slot sets.
                    input = unsafeJso<InputHTMLAttributes<HTMLInputElement>> {
                        placeholder = "OutlinedInput"
                    }
                }
            }

            Checkbox {
                checked = true
                disabled = true
                slotProps = unsafeJso<CheckboxSlotProps> {
                    input = unsafeJso<InputHTMLAttributes<HTMLInputElement>> {
                        className = marker("Checkbox.slotProps.input")
                    }
                }
            }

            Radio {
                slotProps = unsafeJso<RadioSlotProps> {
                    input = unsafeJso<InputHTMLAttributes<HTMLInputElement>> {
                        className = marker("Radio.slotProps.input")
                    }
                }
            }

            Switch {
                slotProps = unsafeJso<SwitchSlotProps> {
                    thumb = unsafeJso<HTMLAttributes<HTMLSpanElement>> {
                        className = marker("Switch.slotProps.thumb")
                    }
                }
            }
        }

        checks?.let {
            CheckList {
                id = "own-slot-props-checks"
                this.checks = it
            }
        }
    },
)
