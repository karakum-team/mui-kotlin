// Automatically generated - do not modify!

package mui.system

import react.PropsWithChildren
import react.ReactNode

external interface ThemeProviderProps : PropsWithChildren {
    /**
     * Your component tree.
     */
    override var children: ReactNode?

    /**
     * The design system's unique id for getting the corresponded theme when there are multiple design systems.
     */
    var themeId: String?

    /**
     * A theme object. You can provide a function to extend the outer theme.
     */
    var theme: Any? /* Partial<Theme> | ((outerTheme: Theme) => Theme) */
}
