package karakum.mui

import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BaseUiAliasesTest {

    @Test
    fun resolvesTwoLevelNamespaceAliasesAcrossModules() {
        val tempDir = createTempDirectory("base-ui-aliases-test").toFile()
        try {
            val fieldControlFile = tempDir.resolve("FieldControl.d.ts").apply {
                writeText(
                    """
                    export declare namespace FieldControl {
                      type Props = FieldControlProps;
                      type State = FieldControlState;
                      type ChangeEventDetails = FieldControlChangeEventDetails;
                      type ChangeEventReason = FieldControlChangeEventReason;
                    }
                    """.trimIndent()
                )
            }

            val modules = listOf(
                BaseUiModule(
                    id = "field",
                    namespace = "Field",
                    parts = listOf(
                        BaseUiPart(
                            alias = "Control",
                            declaredName = "FieldControl",
                            file = fieldControlFile,
                        )
                    )
                )
            )

            val aliases = buildBaseUiAliases(listOf(fieldControlFile), modules)

            // Direct per-part namespace aliases
            assertEquals("FieldControlProps", aliases["FieldControl.Props"])
            assertEquals("FieldControlState", aliases["FieldControl.State"])
            assertEquals("FieldControlChangeEventDetails", aliases["FieldControl.ChangeEventDetails"])
            assertEquals("FieldControlChangeEventReason", aliases["FieldControl.ChangeEventReason"])

            // Two-level cross-module namespace aliases
            assertEquals("FieldControlProps", aliases["Field.Control.Props"])
            assertEquals("FieldControlState", aliases["Field.Control.State"])
            assertEquals("FieldControlChangeEventDetails", aliases["Field.Control.ChangeEventDetails"])
            assertEquals("FieldControlChangeEventReason", aliases["Field.Control.ChangeEventReason"])

            // Bare names are not aliased (to avoid corrupting doc comments)
            assertFalse(aliases.containsKey("Field.Control"))
        } finally {
            tempDir.deleteRecursively()
        }
    }
}
