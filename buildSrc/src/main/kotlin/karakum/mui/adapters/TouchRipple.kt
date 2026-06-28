package karakum.mui.adapters

// v7 declares TouchRipple's props as a type alias intersecting StandardProps with an inline object:
//   export type TouchRippleProps = StandardProps<React.HTMLAttributes<HTMLElement>> & { center?; classes? };
// As a `type` alias the enum/union path truncates the body at the first `;` (losing the members), and
// `dropInlineIntersections` would otherwise strip the ` & { … }` outright — either way `TouchRippleProps`
// never materializes and `FC<TouchRippleProps>` is unresolved. Rewrite it into a plain interface so the
// regular interface converter emits it with its parent and members intact.
//
// Must run BEFORE `dropInlineIntersections`, so the ` & {` becomes an interface body (` {`) first.
fun String.adaptTouchRipple(): String = replace(
    "export type TouchRippleProps = StandardProps<React.HTMLAttributes<HTMLElement>> & {",
    "export interface TouchRippleProps extends StandardProps<React.HTMLAttributes<HTMLElement>> {",
)
