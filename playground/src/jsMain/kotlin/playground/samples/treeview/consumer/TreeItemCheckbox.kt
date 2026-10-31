package playground.samples.treeview.consumer

import js.objects.Object
import js.objects.unsafeJso
import js.reflect.Reflect
import mui.material.Checkbox
import mui.material.CheckboxProps
import mui.material.Size
import react.FC

// Mirrors the consumer component's `TreeItemCheckbox.kt`, rendered with `mui.material.Checkbox` instead of the
// application's own `Checkbox`. The original wrapper declares `TreeItemCheckboxProps : CheckboxProps { visible,
// disabled, cascade, selectable, checked }`; `mui.material.CheckboxProps` already has `checked` / `disabled` /
// `indeterminate`, so only the three members it lacks are added here.
//
// W10: `visible`, `checked`, `indeterminate`, `disabled`, `tabIndex` and `onChange` are what MUI's
// `getCheckboxProps()` hands to the checkbox slot, but the generated `UseTreeItemCheckboxSlotOwnProps` declares
// only `aria-hidden` — the augmentation from `internals/plugins/selection/itemPlugin.d.ts` is never merged.
internal external interface TreeItemCheckboxProps : CheckboxProps {
    var visible: Boolean
    var cascade: Boolean
    var selectable: Boolean
}

internal val TreeItemCheckbox: FC<TreeItemCheckboxProps> = FC { props ->
    if (!props.visible || !props.selectable)
        return@FC

    Checkbox {
        +props.checkboxProps()

        size = Size.small
        indeterminate = props.indeterminate == true && props.cascade
    }
}

// The original wrapper does `val [visible, restProps] = props.destruct(TreeItemCheckboxProps::visible)`.
// `destruct` takes at most two keys, and `cascade` / `selectable` must not reach MUI's Checkbox either: it spreads
// unknown props onto its root DOM element, which React reports on the console.
private fun TreeItemCheckboxProps.checkboxProps(): CheckboxProps {
    val rest: CheckboxProps = Object.assign(unsafeJso(), this)
    Reflect.deleteProperty(rest, "visible")
    Reflect.deleteProperty(rest, "cascade")
    Reflect.deleteProperty(rest, "selectable")
    return rest
}
