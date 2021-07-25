// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Grid")
@file:JsNonModule

package material

external interface GridProps : react.RProps {
    /**
     * Defines the `align-content` style property.
     * It's applied for all screen sizes.
     */
    var alignContent: dynamic

    /**
     * Defines the `align-items` style property.
     * It's applied for all screen sizes.
     */
    var alignItems: dynamic

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * If `true`, the component will have the flex *container* behavior.
     * You should be wrapping *items* with a *container*.
     */
    var container: Boolean

    /**
     * Defines the `flex-direction` style property.
     * It is applied for all screen sizes.
     */
    var direction: dynamic

    /**
     * If `true`, the component will have the flex *item* behavior.
     * You should be wrapping *items* with a *container*.
     */
    var item: Boolean

    /**
     * Defines the `justify-content` style property.
     * It is applied for all screen sizes.
     * @deprecated Use `justifyContent` instead, the prop was renamed
     */
    var justify: dynamic

    /**
     * Defines the `justify-content` style property.
     * It is applied for all screen sizes.
     */
    var justifyContent: dynamic

    /**
     * Defines the number of grids the component is going to use.
     * It's applied for the `lg` breakpoint and wider screens if not overridden.
     */
    var lg: dynamic

    /**
     * Defines the number of grids the component is going to use.
     * It's applied for the `md` breakpoint and wider screens if not overridden.
     */
    var md: dynamic

    /**
     * Defines the number of grids the component is going to use.
     * It's applied for the `sm` breakpoint and wider screens if not overridden.
     */
    var sm: dynamic

    /**
     * Defines the space between the type `item` component.
     * It can only be used on a type `container` component.
     */
    var spacing: dynamic

    /**
     * Defines the `flex-wrap` style property.
     * It's applied for all screen sizes.
     */
    var wrap: dynamic

    /**
     * Defines the number of grids the component is going to use.
     * It's applied for the `xl` breakpoint and wider screens.
     */
    var xl: dynamic

    /**
     * Defines the number of grids the component is going to use.
     * It's applied for all the screen sizes with the lowest priority.
     */
    var xs: dynamic

    /**
     * If `true`, it sets `min-width: 0` on the item.
     * Refer to the limitations section of the documentation to better understand the use case.
     */
    var zeroMinWidth: Boolean
}

/**
 *
 * Demos:
 *
 * - [Grid](https://material-ui.com/components/grid/)
 *
 * API:
 *
 * - [Grid API](https://material-ui.com/api/grid/)
 */
@JsName("default")
external val Grid: react.FC<GridProps>
