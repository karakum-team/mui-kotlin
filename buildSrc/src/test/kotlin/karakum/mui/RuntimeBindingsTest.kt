package karakum.mui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RuntimeBindingsTest {
    @Test
    fun resolvesMultilineGenericFunctionWithoutDroppingItsModule() {
        val source = """
            @file:JsModule("@mui/base/useSelect")

            package mui.base

            @JsName("default")
            external fun <
                OptionValue,
                Multiple,
            > useSelect(props: Options<OptionValue, Multiple>): Result<OptionValue,
                Multiple>
        """.trimIndent()
        assertEquals("useSelect", runtimeDeclarations(source).single().name)
        val result = renderRuntimeBindings(source, listOf(ResolvedRuntimeBinding("@mui/base/useSelect", "useSelect")))
        assertTrue("@file:JsModule(\"@mui/base/useSelect\")" in result)
        assertFalse("@JsName" in result)
        assertTrue("Result<OptionValue,\n    Multiple>" in result)
    }

    @Test
    fun preservesSignaturesAndTypesWhenRebindingDefault() {
        val source = """
            @file:JsModule("@mui/material/styles/useTheme")

            package mui.material.styles

            external interface Theme

            @JsName("default")
            external fun <T : Theme> useTheme(): T
        """.trimIndent()
        val result = renderRuntimeBindings(source, listOf(ResolvedRuntimeBinding("@mui/material/styles", "useTheme")))
        assertTrue("@file:JsModule(\"@mui/material/styles\")" in result)
        assertTrue("external interface Theme" in result)
        assertTrue("external fun <T : Theme> useTheme(): T" in result)
        assertFalse("@JsName" in result)
    }

    @Test
    fun excludesOnlyValueAndItsDocumentation() {
        val source = """
            @file:JsModule("@mui/material/styles/createMotion")

            package mui.material.styles

            /** Still a public type. */
            external interface Motion {
                @JsName("some-name")
                var someName: String
            }

            /** Private factory. */
            @JsName("default")
            external fun createMotion(
                options: Any? = definedExternally,
            ): Motion
        """.trimIndent()
        val result = renderRuntimeBindings(source, listOf(ResolvedRuntimeBinding(null, null)))
        assertFalse("JsModule" in result)
        assertFalse("Private factory" in result)
        assertFalse("external fun" in result)
        assertTrue("Still a public type" in result)
        assertTrue("@JsName(\"some-name\")" in result)
    }

    @Test
    fun readsGenericNamespaceMethodsWithoutEatingTheNextDeclaration() {
        val source = """
            external object Toast {
                val Root: Component<Props>
                fun <Data : Any> createToastManager(): ToastManager<Data>
                fun <Data : Any> useToastManager(): UseToastManagerReturnValue<Data>
            }
        """.trimIndent()
        assertEquals(listOf("Root", "createToastManager", "useToastManager"), runtimeDeclarations(source).single().members)
    }

    @Test
    fun separatesModulesWithoutChangingKotlinNamesOrOverloads() {
        val source = """
            @file:JsModule("old")

            package example

            import other.Options

            external interface Value

            @JsName("default")
            external fun useValue(): Value

            @JsName("default")
            external fun useValue(options: Options): Value

            /** Shared classes. */
            external val classes: Classes
        """.trimIndent()
        val result = renderRuntimeFiles(source, listOf(
            ResolvedRuntimeBinding("hooks", "unstable_useValue"),
            ResolvedRuntimeBinding("hooks", "unstable_useValue"),
            ResolvedRuntimeBinding("styles", "classes"),
        ))
        assertEquals(2, result.size)
        assertTrue("@file:JsModule(\"hooks\")" in result[0])
        assertTrue("external interface Value" in result[0])
        assertEquals(2, Regex("@JsName\\(\"unstable_useValue\"\\)").findAll(result[0]).count())
        assertTrue("external fun useValue(options: Options): Value" in result[0])
        assertFalse("Shared classes" in result[0])
        assertTrue("@file:JsModule(\"styles\")" in result[1])
        assertTrue("import other.Options" in result[1])
        assertTrue("/** Shared classes. */\nexternal val classes" in result[1])
        assertFalse("external interface Value" in result[1])
    }
}
