// Automatically generated - do not modify!

@file:JsModule("@material-ui/core/CardActions")
@file:JsNonModule

package material

external interface CardActionsProps : react.RProps {
    /**
     * The content of the component.
     */
    var children: react.ReactNode

    /**
     * Override or extend the styles applied to the component.
     */
    var classes: CardActionsClasses

    /**
     * The system prop that allows defining system overrides as well as additional CSS styles.
     */
    var sx: SxProps<Theme>

    /**
     * If `true`, the actions do not have additional margin.
     * @default false
     */
    var disableSpacing: Boolean
}

/**
 *
 * Demos:
 *
 * - [Cards](https://material-ui.com/components/cards/)
 *
 * API:
 *
 * - [CardActions API](https://material-ui.com/api/card-actions/)
 */
@JsName("default")
external val CardActions: react.FC<CardActionsProps>
