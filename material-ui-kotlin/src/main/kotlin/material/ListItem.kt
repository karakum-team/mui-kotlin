// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/ListItem")
@file:JsNonModule

package material

external interface ListItemProps : react.RProps {
    var alignItems: dynamic /* 'flex-start' | 'center' */

    var autoFocus: Boolean

    var button: Boolean

    var ContainerComponent: dynamic

    var ContainerProps: dynamic

    var dense: Boolean

    var disabled: Boolean

    var disableGutters: Boolean

    var divider: Boolean

    var focusVisibleClassName: String

    var selected: Boolean
}

/**
 * Uses an additional container component if `ListItemSecondaryAction` is the last child.
 * Demos:
 *
 * - [Lists](https://material-ui.com/components/lists/)
 * - [Transfer List](https://material-ui.com/components/transfer-list/)
 *
 * API:
 *
 * - [ListItem API](https://material-ui.com/api/list-item/)
 */
@JsName("default")
external val ListItem: react.FC<ListItemProps>
