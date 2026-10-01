package karakum.mui

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class BaseUiDiagnosticsTest {
    @Test
    fun ignoresCommentsLiteralsAndDeclarationNames() {
        val stubs = """
            /** [Unused] and [Generic] are only documentation links. */
            external interface Unused
            sealed external interface Generic<Data>
            external interface FloatingPortalProps
            // external interface CommentedOut
        """.trimIndent()
        val body = """
            /** external interface Unused : Generic<Any> */
            external interface Other {
                // FloatingPortalProps
                @JsName("Unused")
                var value: Any? /* Generic<Any> */
            }
        """.trimIndent()

        assertEquals(
            listOf("Unused", "Generic", "FloatingPortalProps"),
            unusedBaseUiStubs(listOf(body), stubs),
        )
    }

    @Test
    fun countsTypeReferencesFromGeneratedCodeAndOtherStubs() {
        val stubs = """
            external interface Shared
            external interface Consumer : Shared
            external interface Parent
            sealed external interface Callback<Data>
            external interface Prefix
        """.trimIndent()
        val body = """
            external interface Example : Parent {
                var callback: Callback<Any>
                var unrelated: PrefixSuffix
            }
        """.trimIndent()

        assertEquals(listOf("Consumer", "Prefix"), unusedBaseUiStubs(listOf(body), stubs))
    }

    @Test
    fun logsOnlyOmittedExportsWhileKeepingSupportedToastMethods() {
        val module = BaseUiModule(
            id = "toast",
            namespace = "Toast",
            parts = listOf(
                part("Root", "ToastRoot"),
                part("useToastManager"),
                part("createToastManager"),
                part("Unsupported"),
            ),
        )
        val diagnostics = mutableListOf<String>()

        val body = assertNotNull(baseUiNamespaceObject(module, setOf("ToastRootProps"), diagnostics::add))

        assertEquals(
            listOf("Base UI Toast: no generated UnsupportedProps, 'Unsupported' not exposed"),
            diagnostics,
        )
        assertTrue("val Root: react.FC<ToastRootProps>" in body)
        assertTrue("fun <Data : Any> createToastManager(): ToastManager<Data>" in body)
        assertTrue("fun <Data : Any> useToastManager(): UseToastManagerReturnValue<Data>" in body)
        assertTrue("Omitted, having no generated props type: `Unsupported`." in body)
        assertFalse("`createToastManager`" in body)
        assertFalse("`useToastManager`" in body)
    }

    @Test
    fun keepsOmissionDiagnosticsForUnsupportedMenuExports() {
        val module = BaseUiModule(
            id = "menu",
            namespace = "Menu",
            parts = listOf(
                part("Root", "MenuRoot"),
                part("Handle", "MenuHandle"),
                part("createHandle", "createMenuHandle"),
            ),
        )
        val diagnostics = mutableListOf<String>()

        val body = assertNotNull(baseUiNamespaceObject(module, setOf("MenuRootProps"), diagnostics::add))

        assertEquals(
            listOf(
                "Base UI Menu: no generated MenuHandleProps, 'Handle' not exposed",
                "Base UI Menu: no generated createMenuHandleProps, 'createHandle' not exposed",
            ),
            diagnostics,
        )
        assertTrue("Omitted, having no generated props type: `Handle`, `createHandle`." in body)
        assertFalse("val Handle" in body)
        assertFalse("fun createHandle" in body)
    }

    private fun part(alias: String, declaredName: String = alias): BaseUiPart =
        BaseUiPart(alias, declaredName, File("$declaredName.d.ts"))
}
