package playground.samples.treeview.consumer

import mui.icons.material.ChevronRight
import mui.material.SvgIconSize
import react.FC
import react.Props

// The original wrapper renders its own icon component:
// `Icon { source = Icons.Arrow_RightSmall; length = 2.spacing }`.
internal val ExpandIcon: FC<Props> = FC {
    ChevronRight {
        fontSize = SvgIconSize.small
    }
}
