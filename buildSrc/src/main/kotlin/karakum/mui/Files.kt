package karakum.mui

import java.io.File

internal fun File.existed(
    vararg names: String,
): Sequence<File> =
    listFiles { file -> file.name in names }!!
        .asSequence()
