// Automatically generated - do not modify!

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import mui.system.Union
import react.CSSProperties

external interface UseAnchorPositioningSharedParameters {
    /**
     * An element to position the popup against.
     * By default, the popup will be positioned against the trigger.
     */
    var anchor: Any? /* Element | null | VirtualElement | React.RefObject<Element | null> | (() => Element | VirtualElement | null) */

    /**
     * Determines which CSS `position` property to use.
     * @default 'absolute'
     */
    var positionMethod: Union? /* 'absolute' | 'fixed' */

    /**
     * Which side of the anchor element to align the popup against.
     * May automatically change to avoid collisions.
     * @default 'bottom'
     */
    var side: Side?

    /**
     * Distance between the anchor and the popup in pixels.
     * Also accepts a function that returns the distance to read the dimensions of the anchor
     * and positioner elements, along with its side and alignment.
     *
     * The function takes a `data` object parameter with the following properties:
     * - `data.anchor`: the dimensions of the anchor element with properties `width` and `height`.
     * - `data.positioner`: the dimensions of the positioner element with properties `width` and `height`.
     * - `data.side`: which side of the anchor element the positioner is aligned against.
     * - `data.align`: how the positioner is aligned relative to the specified side.
     *
     * @example
     * ```jsx
     * <Positioner
     *   sideOffset={({ side, align, anchor, positioner }) => {
     *     return side === 'top' || side === 'bottom'
     *       ? anchor.height
     *       : anchor.width;
     *   }}
     * />
     * ```
     *
     * @default 0
     */
    var sideOffset: Any? /* number | OffsetFunction */

    /**
     * How to align the popup relative to the specified side.
     * @default 'center'
     */
    var align: Align?

    /**
     * Additional offset along the alignment axis in pixels.
     * Also accepts a function that returns the offset to read the dimensions of the anchor
     * and positioner elements, along with its side and alignment.
     *
     * The function takes a `data` object parameter with the following properties:
     * - `data.anchor`: the dimensions of the anchor element with properties `width` and `height`.
     * - `data.positioner`: the dimensions of the positioner element with properties `width` and `height`.
     * - `data.side`: which side of the anchor element the positioner is aligned against.
     * - `data.align`: how the positioner is aligned relative to the specified side.
     *
     * @example
     * ```jsx
     * <Positioner
     *   alignOffset={({ side, align, anchor, positioner }) => {
     *     return side === 'top' || side === 'bottom'
     *       ? anchor.width
     *       : anchor.height;
     *   }}
     * />
     * ```
     *
     * @default 0
     */
    var alignOffset: Any? /* number | OffsetFunction */

    /**
     * An element or a rectangle that delimits the area that the popup is confined to.
     * @default 'clipping-ancestors'
     */
    var collisionBoundary: Any? /* Boundary */

    /**
     * Additional space to maintain from the edge of the collision boundary.
     * @default 5
     */
    var collisionPadding: Any? /* number | Partial<SideObject> */

    /**
     * Whether to maintain the popup in the viewport after
     * the anchor element was scrolled out of view.
     * @default false
     */
    var sticky: Boolean?

    /**
     * Minimum distance to maintain between the arrow and the edges of the popup.
     *
     * Use it to prevent the arrow element from hanging out of the rounded corners of a popup.
     * @default 5
     */
    var arrowPadding: Number?

    /**
     * Whether to disable the popup from tracking any layout shift of its positioning anchor.
     * @default false
     */
    var disableAnchorTracking: Boolean?

    /**
     * Determines how to handle collisions when positioning the popup.
     *
     * `side` controls overflow on the preferred placement axis (`top`/`bottom` or `left`/`right`):
     * - `'flip'`: keep the requested side when it fits; otherwise try the opposite side
     *   (`top` and `bottom`, or `left` and `right`).
     * - `'shift'`: never change side; keep the requested side and move the popup within
     *   the clipping boundary so it stays visible.
     * - `'none'`: do not correct side-axis overflow.
     *
     * `align` controls overflow on the alignment axis (`start`/`center`/`end`):
     * - `'flip'`: keep side, but swap `start` and `end` when the requested alignment overflows.
     * - `'shift'`: keep side and requested alignment, then nudge the popup along the
     *   alignment axis to fit.
     * - `'none'`: do not correct alignment-axis overflow.
     *
     * `fallbackAxisSide` controls fallback behavior on the perpendicular axis when the
     * preferred axis cannot fit:
     * - `'start'`: allow perpendicular fallback and try the logical start side first
     *   (`top` before `bottom`, or `left` before `right` in LTR).
     * - `'end'`: allow perpendicular fallback and try the logical end side first
     *   (`bottom` before `top`, or `right` before `left` in LTR).
     * - `'none'`: do not fallback to the perpendicular axis.
     *
     * When `side` is `'shift'`, explicitly setting `align` only supports `'shift'` or `'none'`.
     * If `align` is omitted, it defaults to `'flip'`.
     *
     * @example
     * ```jsx
     * <Positioner
     *   collisionAvoidance={{
     *     side: 'shift',
     *     align: 'shift',
     *     fallbackAxisSide: 'none',
     *   }}
     * />
     * ```
     *
     */
    var collisionAvoidance: Any? /* CollisionAvoidance */
}

external interface UseAnchorPositioningParameters : UseAnchorPositioningSharedParameters {
    var keepMounted: Boolean?

    var floatingRootContext: Any? /* FloatingRootContext */

    var mounted: Boolean

    var disableAnchorTracking: Boolean

    var nodeId: String?

    var adaptiveOrigin: Any? /* Middleware */

    var collisionAvoidance: Any? /* CollisionAvoidance */

    var shiftCrossAxis: Boolean?

    var lazyFlip: Boolean?

    var externalTree: Any? /* FloatingTreeStore */

    /**
     * Optional middleware that can replace the measured reference rect before offsets and collision
     * middleware run. Used by Preview Card to position against a specific inline line box.
     */
    var inline: Any? /* Middleware */
}

external interface UseAnchorPositioningReturnValue {
    var positionerStyles: CSSProperties

    var arrowStyles: CSSProperties

    var arrowRef: Any? /* React.RefObject<Element | null> */

    var arrowUncentered: Boolean

    var side: Side

    var align: Align

    var physicalSide: Any? /* PhysicalSide */

    var anchorHidden: Boolean

    var refs: Any /* ReturnType<typeof useFloating>['refs'] */

    var context: Any? /* FloatingContext */

    var isPositioned: Boolean

    var update: () -> Unit
}
