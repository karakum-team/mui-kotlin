package playground.samples.material

import js.array.ReadonlyArray
import js.objects.unsafeJso
import mui.icons.material.Abc
import mui.material.*
import muix.tree.view.RichTreeView
import muix.tree.view.SimpleTreeView
import muix.tree.view.TreeItem
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Sample
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.p

private external interface SampleTreeItem {
    var id: String
    var label: String
}

private val sampleTreeItems: ReadonlyArray<SampleTreeItem> = arrayOf(
    unsafeJso<SampleTreeItem> {
        id = "item-1"
        label = "Item 1"
    },
    unsafeJso<SampleTreeItem> {
        id = "item-2"
        label = "Item 2"
    },
)

val MaterialV9Sample: Sample = Sample(
    id = "v9-improvements",
    library = Libraries.Material,
    title = "Material v9 improvements",
    kind = Kind.Check,
    covers = listOf("useMediaQuery", "SwipeableDrawer", "SpeedDial", "RichTreeView selection"),
    fileName = "MaterialV9Sample.kt",
    notes = "Mixed sample (also covers muix.tree.view); split in Phase 3.",
    content = FC<Props> {
        var isDrawerOpen by useState(false)
        var isSpeedDialOpen by useState(false)
        var selectedItem by useState<String?>("item-1")

        // 1. Verify mui.material useMediaQuery hook
        val isWideScreen = useMediaQuery("(min-width:600px)")

        p {
            +"Screen is wide: $isWideScreen"
        }

        // 2. Verify SwipeableDrawer with typed onClose and onOpen event handlers
        Button {
            onClick = { isDrawerOpen = true }
            +"Open Swipeable Drawer"
        }

        SwipeableDrawer {
            open = isDrawerOpen
            onClose = { event ->
                isDrawerOpen = false
            }
            onOpen = { event ->
                isDrawerOpen = true
            }
            div {
                +"Drawer Content"
            }
        }

        // 3. Verify SpeedDial with slots and slotProps
        SpeedDial {
            ariaLabel = "SpeedDial example"
            open = isSpeedDialOpen
            onOpen = { _, _ -> isSpeedDialOpen = true }
            onClose = { _, _ -> isSpeedDialOpen = false }
            icon = SpeedDialIcon.create()

            SpeedDialAction {
                icon = Abc.create()
                title = ReactNode("Action 1")
            }
        }

        // 4. Verify RichTreeView with items and selection handlers
        RichTreeView {
            items = sampleTreeItems
            selectedItems = selectedItem
            onSelectedItemsChange = { _, itemIds ->
                selectedItem = itemIds?.toString()
            }
            onItemClick = { _, itemId ->
                console.log("Clicked item: $itemId")
            }
        }

        // 5. Verify SimpleTreeView with expandedItems and selection
        SimpleTreeView {
            multiSelect = false
            TreeItem {
                itemId = "simple-1"
                label = ReactNode("Simple Item 1")
            }
        }
    },
)
