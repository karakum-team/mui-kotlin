import csstype.Display
import csstype.None.none
import mui.lab.Masonry
import mui.material.Box
import mui.system.Breakpoint.md
import mui.system.Breakpoint.xs
import mui.system.responsive
import mui.system.sx
import react.FC
import react.Props

val BreakpointsExample = FC<Props> {
    Box {
        sx {
            display = responsive(
                xs to none,
                md to Display.grid,
            )
        }
    }

    Masonry {
        columns = responsive(
            xs to 1,
            md to 4,
        )
    }

    Masonry {
        columns = responsive(2)
    }
}
