package playground.samples.material

import mui.icons.material.Abc
import mui.material.Button
import mui.material.SpeedDial
import mui.material.SpeedDialAction
import mui.material.SpeedDialIcon
import mui.material.SwipeableDrawer
import mui.material.useMediaQuery
import playground.shell.Kind
import playground.shell.Libraries
import playground.shell.Ref
import playground.shell.Sample
import playground.shell.StatusLine
import react.FC
import react.Props
import react.ReactNode
import react.create
import react.dom.html.ReactHTML.div
import react.useState

val MaterialV9Sample: Sample = Sample(
    id = "v9-improvements",
    library = Libraries.Material,
    title = "Material v9 improvements",
    kind = Kind.Check,
    covers = listOf(
        "useMediaQuery",
        "SwipeableDrawer.onOpen/onClose",
        "SpeedDial.open/onOpen/onClose/icon",
        "SpeedDialIcon",
        "SpeedDialAction.icon/title",
    ),
    fileName = "MaterialV9Sample.kt",
    refs = listOf(
        Ref("10dc9f7c", "https://github.com/karakum-team/mui-kotlin/commit/10dc9f7c"),
    ),
    notes = "Only running this proves that `useMediaQuery` is a live hook from the material barrel (the status " +
            "line flips when the window crosses 600px), and that the typed `SwipeableDrawer` and `SpeedDial` " +
            "handlers are the ones upstream actually calls: opening and closing each updates the status line.",
    content = FC<Props> {
        var isDrawerOpen by useState(false)
        var isSpeedDialOpen by useState(false)

        // 1. Verify mui.material useMediaQuery hook
        val isWideScreen = useMediaQuery("(min-width:600px)")

        StatusLine {
            id = "v9-status"
            fields = listOf(
                "wide" to isWideScreen,
                "drawer" to isDrawerOpen,
                "speedDial" to isSpeedDialOpen,
            )
        }

        // 2. Verify SwipeableDrawer with typed onClose and onOpen event handlers
        Button {
            onClick = { isDrawerOpen = true }
            +"Open Swipeable Drawer"
        }

        SwipeableDrawer {
            open = isDrawerOpen
            onClose = { _ -> isDrawerOpen = false }
            onOpen = { _ -> isDrawerOpen = true }
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
    },
)
