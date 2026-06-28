package karakum.mui.adapters

// MUI types some callbacks with the "bivariance hack" — an object method indexed back out to its
// function type: `{ bivarianceHack(args): ret }['bivarianceHack']` (e.g. ModalProps `onClose`). The
// inline object would be eaten by `dropMemberValueObjects` (→ `any['bivarianceHack']` → `Any?`), losing
// the callback shape. Rewrite it to the equivalent plain function type up front so it converts normally.
// Must run BEFORE `dropInlineIntersections`/`dropMemberValueObjects`.
//
// Arg normalization: the param types here are `event: {}` and `reason: '…' | '…'`, neither of which the
// function-type converter reduces on its own (a bare `{}` and a standalone string-literal union would
// leak). Map `{}` → `any` and string-literal unions → `string` so we land on `(event: Any, reason: String)`
// (the v6 shape).
fun String.adaptBivarianceHack(): String = replace(
    Regex("""\{\s*bivarianceHack\(([^)]*)\):\s*(\w+);\s*}\['bivarianceHack']"""),
) { match ->
    val args = match.groupValues[1]
        .replace(": {}", ": any")
        .replace(Regex("""'[^']*'(?:\s*\|\s*'[^']*')+"""), "string")
    "(${args}) => ${match.groupValues[2]}"
}
