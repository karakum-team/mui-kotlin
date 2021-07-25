// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Breadcrumbs")
@file:JsNonModule

package material

external interface BreadcrumbsProps : react.RProps {
    /**
     * The breadcrumb children.
     */
    var children: react.ReactNode

    /**
     * Override the default label for the expand button.
     *
     * For localization purposes, you can use the provided [translations](/guides/localization/).
     */
    var expandText: String

    /**
     * If max items is exceeded, the number of items to show after the ellipsis.
     */
    var itemsAfterCollapse: Number

    /**
     * If max items is exceeded, the number of items to show before the ellipsis.
     */
    var itemsBeforeCollapse: Number

    /**
     * Specifies the maximum number of breadcrumbs to display. When there are more
     * than the maximum number, only the first `itemsBeforeCollapse` and last `itemsAfterCollapse`
     * will be shown, with an ellipsis in between.
     */
    var maxItems: Number

    /**
     * Custom separator node.
     */
    var separator: react.ReactNode
}

/**
 *
 * Demos:
 *
 * - [Breadcrumbs](https://material-ui.com/components/breadcrumbs/)
 *
 * API:
 *
 * - [Breadcrumbs API](https://material-ui.com/api/breadcrumbs/)
 */
@JsName("default")
external val Breadcrumbs: react.FC<BreadcrumbsProps>
