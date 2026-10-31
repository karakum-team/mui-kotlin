package playground.samples.treeview.consumer

import mui.icons.material.ExpandMore
import mui.material.SvgIconSize
import react.FC
import react.Props

// The original wrapper renders its own icon component:
// `Icon { source = Icons.Arrow_DownSmall; length = 2.spacing }`.
internal val CollapseIcon: FC<Props> = FC {
    ExpandMore {
        fontSize = SvgIconSize.small
    }
}
