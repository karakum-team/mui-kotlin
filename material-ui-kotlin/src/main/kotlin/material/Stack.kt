// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/Stack")
@file:JsNonModule

package material

external interface StackProps : react.RProps {
    var ref: dynamic

    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Defines the `flex-direction` style property.
     * It is applied for all screen sizes.
     * @default 'column'
     */
    var direction: dynamic

    /**
     * Defines the space between immediate children.
     * @default 0
     */
    var spacing: dynamic

    /**
     * Add an element between each child.
     */
    var divider: react.ReactNode

    /**
     * The system prop, which allows defining system overrides as well as additional CSS styles.
     */
    var sx: dynamic
}

/**
 *
 * Demos:
 *
 * - [Stack](https://material-ui.com/components/stack/)
 *
 * API:
 *
 * - [Stack API](https://material-ui.com/api/stack/)
 */
@JsName("default")
external val Stack: react.FC<StackProps>
