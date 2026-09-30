package karakum.mui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class BaseUiReasonsTest {
    private val constants = """
        export declare const incrementPress: 'increment-press';
        export declare const keyboard: 'keyboard';
    """.trimIndent()

    @Test
    fun resolvesUpstreamConstantsWithoutChangingOtherAliases() {
        val source = """
            export type DirectionalChangeReason = typeof REASONS.incrementPress | typeof REASONS.keyboard;
            export type OtherReason = typeof REASONS.keyboard;
        """.trimIndent()
        assertEquals(
            """
                export type DirectionalChangeReason = 'increment-press' | 'keyboard';
                export type OtherReason = typeof REASONS.keyboard;
            """.trimIndent(),
            resolveBaseUiDirectionalReasons(source, constants),
        )
    }

    @Test
    fun retainsLegacyLiteralUnion() {
        val source = "export type DirectionalChangeReason = 'increment-press' | 'keyboard';"
        assertEquals(source, resolveBaseUiDirectionalReasons(source, constants))
    }

    @Test
    fun rejectsUnknownConstantsInsteadOfDroppingUnionArms() {
        assertFailsWith<IllegalArgumentException> {
            resolveBaseUiDirectionalReasons(
                "export type DirectionalChangeReason = typeof REASONS.missing;",
                constants,
            )
        }
    }
}
