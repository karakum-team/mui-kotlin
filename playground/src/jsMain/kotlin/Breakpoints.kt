import csstype.Display
import csstype.None.Companion.none
import mui.lab.Masonry
import mui.material.Box
import mui.system.Breakpoint.Companion.md
import mui.system.Breakpoint.Companion.xs
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
        @Suppress("INFERRED_TYPE_VARIABLE_INTO_POSSIBLE_EMPTY_INTERSECTION")
        columns = responsive(
            xs to 1,
            md to 4,
        )
    }

    Masonry {
        columns = responsive(2)
    }
}
