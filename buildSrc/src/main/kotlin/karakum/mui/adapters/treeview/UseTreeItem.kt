package karakum.mui.adapters.treeview

import java.io.File

/**
 * Merges the `declare module '@mui/x-tree-view/useTreeItem'` augmentations of the selection and label-editing item
 * plugins into `useTreeItem/useTreeItem.types.d.ts` before it is converted.
 *
 * `useTreeItem.types.d.ts` declares the slot own-props as near-empty interfaces (`UseTreeItemCheckboxSlotOwnProps`
 * has only `ref` and `aria-hidden`). The members a slot actually receives — `visible`, `checked`, `indeterminate`,
 * `disabled`, `tabIndex` and `onChange` for the checkbox, `aria-checked` / `aria-selected` for the root, `editable`
 * for the label, the whole `TreeItemLabelInputProps` for the label input — arrive by TypeScript declaration
 * merging from `internals/plugins/selection/itemPlugin.d.ts` and `internals/plugins/labelEditing/itemPlugin.d.ts`.
 * The augmenting interfaces are not exported and the merge is invisible to a text converter, so their members are
 * spliced into the target interfaces here: a non-empty augmentation lands in the target's body; an empty one that
 * only `extends` another interface turns the empty target into `extends` of that interface, which
 * `KEEP_EMPTY_BODY_PARENTS` (Converter.kt) then preserves.
 *
 * Fails when a plugin file or one of its declarations goes missing: a silent fallback would reopen the gap.
 */
internal fun adaptUseTreeItemAugmentations(
    content: String,
    typesDir: File,
): String {
    var result = content
    for (relativePath in PLUGIN_FILES) {
        val file = typesDir.resolve(relativePath)
        if (!file.exists()) error("Expected the x-tree-view item plugin declarations at ${file.path}")
        val plugin = file.readText().replace("\r\n", "\n")

        val interfaces = PLUGIN_INTERFACE.findAll(plugin).associate { match ->
            match.groupValues[1] to PluginInterface(match.groupValues[2].ifEmpty { null }, match.groupValues[3])
        }
        val augmentations = AUGMENTATION.findAll(plugin).toList()
        if (augmentations.isEmpty()) error("No `declare module` augmentation found in ${file.path}")

        for (augmentation in augmentations) {
            val (target, source) = augmentation.destructured
            val spliced = interfaces[source] ?: error("`$source` is not declared in ${file.path}")
            result = when {
                spliced.body.isNotBlank() -> result.addMembers(target, spliced.body)
                spliced.parents != null -> result.addParents(target, spliced.parents)
                else -> error("`$source` in ${file.path} has neither members nor parents to merge")
            }
        }
    }
    return result
}

private class PluginInterface(
    val parents: String?,
    val body: String,
)

private val PLUGIN_FILES = listOf(
    "internals/plugins/selection/itemPlugin.d.ts",
    "internals/plugins/labelEditing/itemPlugin.d.ts",
)

// A top-level (non-exported, non-indented) `interface X [extends Y] { … }` of the plugin file.
private val PLUGIN_INTERFACE = Regex("""(?m)^interface (\w+)(?: extends ([\w, ]+?))? \{(.*?)^?\}$""", RegexOption.DOT_MATCHES_ALL)

// `interface Target extends Source {}` inside the `declare module '@mui/x-tree-view/useTreeItem'` block.
private val AUGMENTATION = Regex("""(?m)^ {2}interface (\w+) extends (\w+) \{\}$""")

private fun String.addMembers(
    target: String,
    body: String,
): String {
    val declaration = Regex("""(?m)^export interface ${Regex.escape(target)}\b[^{]*\{""").find(this)
        ?: error("`export interface $target` is not declared in useTreeItem.types.d.ts")
    val members = body.trim('\n')
    val afterBrace = declaration.range.last + 1

    return if (startsWith("}", afterBrace)) {
        // `{}` — the whole body is the merged one.
        replaceRange(declaration.range.last, afterBrace + 1, "{\n$members\n}")
    } else {
        val closing = indexOf("\n}", afterBrace)
        check(closing >= 0) { "`export interface $target` is never closed" }
        replaceRange(closing, closing, "\n$members")
    }
}

private fun String.addParents(
    target: String,
    parents: String,
): String {
    val declaration = Regex("""(?m)^export interface ${Regex.escape(target)}(?: extends ([^{]+?))? \{\}$""").find(this)
        ?: error("`export interface $target {}` is not declared in useTreeItem.types.d.ts")
    val existing = declaration.groupValues[1].takeIf { it.isNotEmpty() }
    val merged = listOfNotNull(existing, parents).joinToString(", ")

    return replaceRange(declaration.range, "export interface $target extends $merged {}")
}
