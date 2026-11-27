package karakum.mui

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BaseUiDirectExportsTest {

    @Test
    fun resolvesOuterExportRenamesForDirectionProvider() {
        val tempDir = createTempDirectory("base-ui-renames-test").toFile()
        try {
            val indexFile = tempDir.resolve("index.d.ts").apply {
                writeText(
                    """
                    export { Provider as DirectionProvider, useDirection, type TextDirection } from "./index.parts.js";
                    export type { DirectionProviderProps } from "./DirectionProvider.js";
                    """.trimIndent()
                )
            }

            val parts = listOf(
                BaseUiPart(
                    alias = "Provider",
                    declaredName = "DirectionProvider",
                    file = tempDir.resolve("DirectionProvider.d.ts"),
                )
            )

            val resolved = resolveOuterExportRenames(parts, indexFile)

            assertEquals(1, resolved.size)
            assertEquals("DirectionProvider", resolved[0].alias)
            assertEquals("DirectionProvider", resolved[0].declaredName)
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun preservesAliasWhenNoOuterRenameSpecified() {
        val tempDir = createTempDirectory("base-ui-renames-test").toFile()
        try {
            val indexFile = tempDir.resolve("index.d.ts").apply {
                writeText(
                    """
                    export { CSPProvider } from "./index.parts.js";
                    export type * from "./CSPProvider.js";
                    """.trimIndent()
                )
            }

            val parts = listOf(
                BaseUiPart(
                    alias = "CSPProvider",
                    declaredName = "CSPProvider",
                    file = tempDir.resolve("CSPProvider.d.ts"),
                )
            )

            val resolved = resolveOuterExportRenames(parts, indexFile)

            assertEquals(1, resolved.size)
            assertEquals("CSPProvider", resolved[0].alias)
            assertEquals("CSPProvider", resolved[0].declaredName)
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun extractsComponentKDocFromConstAndFunctionDeclarations() {
        val tempDir = createTempDirectory("base-ui-kdoc-test").toFile()
        try {
            val buttonFile = tempDir.resolve("Button.d.ts").apply {
                writeText(
                    """
                    import * as React from 'react';
                    /**
                     * A button component that can be used to trigger actions.
                     * Renders a `<button>` element.
                     *
                     * Documentation: [Base UI Button](https://base-ui.com/react/components/button)
                     */
                    export declare const Button: React.ForwardRefExoticComponent<any>;
                    """.trimIndent()
                )
            }

            val cspFile = tempDir.resolve("CSPProvider.d.ts").apply {
                writeText(
                    """
                    import * as React from 'react';
                    /**
                     * Provides a Content Security Policy nonce.
                     */
                    export declare function CSPProvider(props: CSPProviderProps): React.JSX.Element;
                    """.trimIndent()
                )
            }

            val buttonKdoc = extractComponentKDoc(buttonFile, "Button")
            assertNotNull(buttonKdoc)
            assertTrue("A button component that can be used to trigger actions." in buttonKdoc)
            assertTrue("Documentation: [Base UI Button]" in buttonKdoc)

            val cspKdoc = extractComponentKDoc(cspFile, "CSPProvider")
            assertNotNull(cspKdoc)
            assertTrue("Provides a Content Security Policy nonce." in cspKdoc)

            assertNull(extractComponentKDoc(buttonFile, "NonExistent"))
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun generatesDirectComponentDeclarationWithKDoc() {
        val tempDir = createTempDirectory("base-ui-decl-test").toFile()
        try {
            val file = tempDir.resolve("Button.d.ts").apply {
                writeText(
                    """
                    /**
                     * Button component doc.
                     */
                    export declare const Button: any;
                    """.trimIndent()
                )
            }

            val part = BaseUiPart("Button", "Button", file)
            val decl = baseUiDirectComponentDeclaration(part)

            assertEquals(
                """
                /**
                 * Button component doc.
                 */
                external val Button: react.FC<ButtonProps>
                """.trimIndent(),
                decl,
            )
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun directExportsHandlesComponentsAndDiagnosticsForUnexposed() {
        val tempDir = createTempDirectory("base-ui-direct-exports-test").toFile()
        try {
            val buttonFile = tempDir.resolve("Button.d.ts")
            val unsupportedFile = tempDir.resolve("Unsupported.d.ts")

            val module = BaseUiModule(
                id = "button",
                namespace = null,
                parts = listOf(
                    BaseUiPart("Button", "Button", buttonFile),
                    BaseUiPart("Unsupported", "Unsupported", unsupportedFile),
                ),
            )

            val diagnostics = mutableListOf<String>()
            val exports = baseUiDirectExports(
                module = module,
                declaredTypes = setOf("ButtonProps"),
                reportDiagnostic = diagnostics::add,
            )

            assertEquals(1, exports.size)
            assertEquals("Button", exports[0].part.alias)
            assertTrue("external val Button: react.FC<ButtonProps>" in exports[0].declaration)

            assertEquals(
                listOf("Base UI button: no generated UnsupportedProps, 'Unsupported' not exposed"),
                diagnostics,
            )
        } finally {
            tempDir.deleteRecursively()
        }
    }

    @Test
    fun returnsEmptyDirectExportsForNamespaceModules() {
        val module = BaseUiModule(
            id = "menu",
            namespace = "Menu",
            parts = listOf(
                BaseUiPart("Root", "MenuRoot", File("MenuRoot.d.ts")),
            ),
        )

        val exports = baseUiDirectExports(module, setOf("MenuRootProps"))
        assertTrue(exports.isEmpty())
    }

    @Test
    fun discoversDirectionProviderOuterRenameFromInstalledPackage() {
        val reactDir = File("build/js/node_modules/@base-ui/react")
        if (!reactDir.exists()) return

        val modules = baseUiModules(reactDir, setOf("direction-provider", "csp-provider", "button", "separator"))
        val dirProvider = modules.firstOrNull { it.id == "direction-provider" }
        assertNotNull(dirProvider)
        assertNull(dirProvider.namespace)
        assertEquals(1, dirProvider.parts.size)
        assertEquals("DirectionProvider", dirProvider.parts[0].alias)
        assertEquals("DirectionProvider", dirProvider.parts[0].declaredName)

        val csp = modules.firstOrNull { it.id == "csp-provider" }
        assertNotNull(csp)
        assertNull(csp.namespace)
        assertEquals(1, csp.parts.size)
        assertEquals("CSPProvider", csp.parts[0].alias)

        val btn = modules.firstOrNull { it.id == "button" }
        assertNotNull(btn)
        assertNull(btn.namespace)
        assertEquals(1, btn.parts.size)
        assertEquals("Button", btn.parts[0].alias)

        val sep = modules.firstOrNull { it.id == "separator" }
        assertNotNull(sep)
        assertNull(sep.namespace)
        assertEquals(1, sep.parts.size)
        assertEquals("Separator", sep.parts[0].alias)
    }
}
