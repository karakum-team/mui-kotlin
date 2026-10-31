package playground.samples.treeview.consumer

import emotion.css.ClassName
import muix.tree.view.TreeItemDataAttributes
import muix.tree.view.TreeViewCssVars
import muix.tree.view.richTreeViewClasses
import muix.tree.view.treeItemClasses
import web.cssom.*
import web.cssom.None.Companion.none

// Mirrors the consumer component's `TreeView.styles.kt`. The application's theme tokens (`Colors`, `Fonts`,
// `Filters`, `SurfaceStyles` and its `@container` query) are replaced by literal values; the selectors and the
// `var(depth) * var(indentation)` inset are kept as they are, spelled through the generated class, custom property
// and data attribute constants.

private val ICON_TOP_INSET: Length = 1.25.spacing

private val ITEM_HOVER_BACKGROUND: CustomPropertyName<Color> = CustomPropertyName("--tree-view-item-hover-background")

private val TEXT_COLOR = Color("#1b1b1b")
private val MUTED_TEXT_COLOR = Color("#6b6b6b")
private val HOVER_FILL = Color("#e6e6e6")
private val GRAY_FILTER: FilterFunction = array(grayscale(0.5), opacity(0.5))

// Replaces the application's hover surface class (`SurfaceStyles.NestedOnHover`), applied to the content of
// every enabled, selectable item.
internal val nestedOnHover: ClassName = ClassName {
    hover {
        backgroundColor = HOVER_FILL
    }
}

internal fun treeViewStyle(): ClassName = ClassName {
    fontSize = 14.px
    lineHeight = number(1.4)
    color = TEXT_COLOR

    val itemInset = 1.spacing + `var`(TreeViewCssVars.itemDepth) * `var`(TreeViewCssVars.itemChildrenIndentation)

    "&.${richTreeViewClasses.root}" {
        margin = -1.spacing
    }

    ".${treeItemClasses.content}" {
        padding = 0.px
        gap = 0.px
        alignItems = AlignItems.stretch
        borderRadius = 0.5.spacing

        set(ITEM_HOVER_BACKGROUND, HOVER_FILL)

        hover {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[${TreeItemDataAttributes.focused}]" {
            backgroundColor = Color.transparent
        }

        "&[${TreeItemDataAttributes.focused}]:hover" {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[${TreeItemDataAttributes.selected}]" {
            backgroundColor = Color.transparent

            "&[${TreeItemDataAttributes.focused}]" {
                backgroundColor = Color.transparent
            }
        }

        "&[${TreeItemDataAttributes.selected}]:hover" {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[${TreeItemDataAttributes.disabled}]" {
            opacity = number(1.0)
            filter = GRAY_FILTER
            cursor = Cursor.default

            "*" {
                pointerEvents = none
            }
        }

        "&[${TreeItemDataAttributes.disabled}]:hover" {
            backgroundColor = Color.transparent
        }

        "&[$UNSELECTABLE_ATTRIBUTE]" {
            cursor = Cursor.default

            ".${treeItemClasses.label}" {
                paddingLeft = itemInset + 2.5.spacing
                cursor = Cursor.default
            }
        }

        "&[$UNSELECTABLE_ATTRIBUTE]:hover" {
            backgroundColor = Color.transparent
        }
    }

    ".${treeItemClasses.iconContainer}" {
        paddingTop = ICON_TOP_INSET
        paddingBottom = 1.spacing
        paddingLeft = itemInset
        paddingRight = 0.5.spacing
        boxSizing = BoxSizing.contentBox
        alignItems = AlignItems.flexStart
        color = MUTED_TEXT_COLOR
    }

    ".${treeItemClasses.iconContainer}:empty" {
        display = none
    }

    ".${treeItemClasses.checkbox}" {
        paddingTop = ICON_TOP_INSET
        paddingRight = 1.spacing
        alignItems = AlignItems.start
        alignContent = AlignContent.start
    }

    ".${treeItemClasses.iconContainer}:empty + .${treeItemClasses.checkbox}" {
        paddingLeft = itemInset + 2.5.spacing
    }

    ".${treeItemClasses.label}" {
        paddingTop = 1.spacing
        paddingBottom = 1.spacing
        paddingRight = 1.spacing
        fontSize = 14.px
        minWidth = 0.px
        overflowWrap = OverflowWrap.anywhere
        cursor = Cursor.pointer
    }
}
