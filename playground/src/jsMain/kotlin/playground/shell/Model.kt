package playground.shell

import react.FC
import react.Props

enum class Kind {
    Showcase,
    Check,
    ImportSmoke,
    ClassesCoverage,
}

data class Ref(
    val label: String,
    val href: String,
)

data class Check(
    val label: String,
    val passed: Boolean,
)

class Library(
    val id: String,
    val title: String,
    val kotlinPackage: String,
    val npmPackage: String,
    val note: String? = null,
) {
    // Same lookup rule as `npmv()` in mui-kotlin/build.gradle.kts.
    val versionProperty: String
        get() = npmPackage.removePrefix("@").replace("/", "-")

    val version: String?
        get() = Versions.of(versionProperty)
}

class Sample(
    val id: String,
    val library: Library,
    val title: String,
    val kind: Kind,
    val covers: List<String>,
    val fileName: String,
    val refs: List<Ref> = emptyList(),
    val notes: String? = null,
    val content: FC<Props>,
) {
    val key: String
        get() = "${library.id}/$id"

    val hash: String
        get() = "#/$key"

    val sourcePath: String
        get() = "playground/src/jsMain/kotlin/playground/samples/${library.id}/$fileName"
}
