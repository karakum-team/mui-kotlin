package playground.samples.treeview.consumer

import emotion.css.ClassName
import web.cssom.*
import web.cssom.None.Companion.none

// Mirrors the consumer component's `TreeView.styles.kt`. The application's theme tokens (`Colors`, `Fonts`,
// `Filters`, `SurfaceStyles` and its `@container` query) are replaced by literal values; every selector string,
// the hand-written CSS custom properties and the `var(depth) * var(indentation)` inset are kept as they are.

private val ICON_TOP_INSET: Length = 1.25.spacing

// W11: these custom properties are set by MUI at runtime (`useTreeViewRootProps`, `useTreeItem`) but declared
// nowhere in the wrappers.
private val ITEM_CHILDREN_INDENTATION: CustomPropertyName<Length> = CustomPropertyName("--TreeView-itemChildrenIndentation")
private val ITEM_DEPTH: CustomPropertyName<Double> = CustomPropertyName("--TreeView-itemDepth")

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

// W11: class names as strings; `richTreeViewClasses` / `treeItemClasses` cannot be used because the generated
// `RichTreeViewClasses` keeps only `itemLoader` (the `Omit<TreeViewClasses, …>` parent is dropped).
internal fun treeViewStyle(): ClassName = ClassName {
    fontSize = 14.px
    lineHeight = number(1.4)
    color = TEXT_COLOR

    val itemInset = 1.spacing + `var`(ITEM_DEPTH) * `var`(ITEM_CHILDREN_INDENTATION)

    "&.MuiRichTreeView-root" {
        margin = -1.spacing
    }

    ".MuiTreeItem-content" {
        padding = 0.px
        gap = 0.px
        alignItems = AlignItems.stretch
        borderRadius = 0.5.spacing

        set(ITEM_HOVER_BACKGROUND, HOVER_FILL)

        hover {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[data-focused]" {
            backgroundColor = Color.transparent
        }

        "&[data-focused]:hover" {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[data-selected]" {
            backgroundColor = Color.transparent

            "&[data-focused]" {
                backgroundColor = Color.transparent
            }
        }

        "&[data-selected]:hover" {
            backgroundColor = `var`(ITEM_HOVER_BACKGROUND)
        }

        "&[data-disabled]" {
            opacity = number(1.0)
            filter = GRAY_FILTER
            cursor = Cursor.default

            "*" {
                pointerEvents = none
            }
        }

        "&[data-disabled]:hover" {
            backgroundColor = Color.transparent
        }

        "&[$UNSELECTABLE_ATTRIBUTE]" {
            cursor = Cursor.default

            ".MuiTreeItem-label" {
                paddingLeft = itemInset + 2.5.spacing
                cursor = Cursor.default
            }
        }

        "&[$UNSELECTABLE_ATTRIBUTE]:hover" {
            backgroundColor = Color.transparent
        }
    }

    ".MuiTreeItem-iconContainer" {
        paddingTop = ICON_TOP_INSET
        paddingBottom = 1.spacing
        paddingLeft = itemInset
        paddingRight = 0.5.spacing
        boxSizing = BoxSizing.contentBox
        alignItems = AlignItems.flexStart
        color = MUTED_TEXT_COLOR
    }

    ".MuiTreeItem-iconContainer:empty" {
        display = none
    }

    ".MuiTreeItem-checkbox" {
        paddingTop = ICON_TOP_INSET
        paddingRight = 1.spacing
        alignItems = AlignItems.start
        alignContent = AlignContent.start
    }

    ".MuiTreeItem-iconContainer:empty + .MuiTreeItem-checkbox" {
        paddingLeft = itemInset + 2.5.spacing
    }

    ".MuiTreeItem-label" {
        paddingTop = 1.spacing
        paddingBottom = 1.spacing
        paddingRight = 1.spacing
        fontSize = 14.px
        minWidth = 0.px
        overflowWrap = OverflowWrap.anywhere
        cursor = Cursor.pointer
    }
}
