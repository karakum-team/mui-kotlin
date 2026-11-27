// Automatically generated - do not modify!

@file:JsModule("@base-ui/react/csp-provider")

@file:Suppress(
    "VIRTUAL_MEMBER_HIDDEN",
    "VAR_TYPE_MISMATCH_ON_OVERRIDE",
)

package baseui

import react.FC
import react.PropsWithChildren
import react.ReactNode

external interface CSPProviderProps : PropsWithChildren {
    override var children: ReactNode?

    /**
     * The nonce value to apply to inline `<style>` and `<script>` tags.
     */
    var nonce: String?

    /**
     * Whether inline `<style>` elements created by Base UI components should not be rendered. Instead, components must specify the CSS styles via custom class names or other methods.
     * @default false
     */
    var disableStyleElements: Boolean?
}

external interface CSPProviderState

/**
 * Provides a default Content Security Policy (CSP) configuration for Base UI components that
 * require inline `<style>` or `<script>` tags.
 *
 * Documentation: [Base UI CSP Provider](https://base-ui.com/react/utils/csp-provider)
 */
external val CSPProvider: FC<CSPProviderProps>
