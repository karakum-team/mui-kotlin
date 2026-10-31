package playground.samples.treeview.consumer

import js.objects.Object
import js.objects.unsafeJso
import js.reflect.Reflect
import mui.material.Checkbox
import mui.material.Size
import muix.tree.view.UseTreeItemCheckboxSlotOwnProps
import react.FC

// Mirrors the checkbox slot component of a downstream application's tree view wrapper, rendered with
// `mui.material.Checkbox` instead of the application's own checkbox. The slot receives what MUI's
// `getCheckboxProps()` builds — `visible`, `checked`, `indeterminate`, `disabled`, `tabIndex`, `onChange`,
// `aria-hidden` — all declared by the generated `UseTreeItemCheckboxSlotOwnProps`; the wrapper adds its own two.
internal external interface TreeItemCheckboxProps : UseTreeItemCheckboxSlotOwnProps {
    var cascade: Boolean
    var selectable: Boolean
}

internal val TreeItemCheckbox: FC<TreeItemCheckboxProps> = FC { props ->
    if (props.visible != true || !props.selectable)
        return@FC

    Checkbox {
        +props.checkboxProps()

        size = Size.small
        indeterminate = props.indeterminate == true && props.cascade
    }
}

// The original wrapper destructures `visible` away and spreads the rest onto its checkbox. `cascade` /
// `selectable` must not reach MUI's Checkbox either: it spreads unknown props onto its root DOM element, which
// React reports on the console.
private fun TreeItemCheckboxProps.checkboxProps(): UseTreeItemCheckboxSlotOwnProps {
    val rest: UseTreeItemCheckboxSlotOwnProps = Object.assign(unsafeJso(), this)
    Reflect.deleteProperty(rest, "visible")
    Reflect.deleteProperty(rest, "cascade")
    Reflect.deleteProperty(rest, "selectable")
    return rest
}
