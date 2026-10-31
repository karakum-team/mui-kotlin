package playground.shell

import emotion.react.css
import playground.dataSample
import react.FC
import react.PropsWithChildren
import react.RequiredContext
import react.createRequiredContext
import react.dom.html.ReactHTML.a
import react.dom.html.ReactHTML.code
import react.dom.html.ReactHTML.dd
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.dl
import react.dom.html.ReactHTML.dt
import react.dom.html.ReactHTML.h2
import react.dom.html.ReactHTML.header
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.section
import react.useEffectOnce
import web.console.console
import web.cssom.ClassName
import web.cssom.Position
import web.dom.ElementId

val SampleContext: RequiredContext<Sample> =
    createRequiredContext()

external interface SampleFrameProps : PropsWithChildren {
    var sample: Sample
}

val SampleFrame = FC<SampleFrameProps> { props ->
    val sample = props.sample
    val domId = "sample-${sample.library.id}-${sample.id}"

    useEffectOnce {
        console.log("[playground] ${sample.key} mounted")
    }

    SampleContext.Provider(sample) {
        section {
            id = ElementId(domId)
            dataSample = sample.key
            ariaLabelledBy = ElementId("$domId-title")
            className = ClassName("pg-sample")

            header {
                className = ClassName("pg-sample-header")

                h2 {
                    id = ElementId("$domId-title")
                    +sample.title
                }

                dl {
                    className = ClassName("pg-meta")

                    MetaItem {
                        label = "Kotlin package"
                        code { +sample.library.kotlinPackage }
                    }
                    MetaItem {
                        label = "npm"
                        val version = sample.library.version
                        code { +(if (version != null) "${sample.library.npmPackage}@$version" else sample.library.npmPackage) }
                        if (version == null) +" (pinned in mui-kotlin/build.gradle.kts)"
                    }
                    val libraryNote = sample.library.note
                    if (libraryNote != null) {
                        MetaItem {
                            label = "Note"
                            +libraryNote
                        }
                    }
                    MetaItem {
                        label = "Kind"
                        +sample.kind.name
                    }
                    MetaItem {
                        label = "Covers"
                        +sample.covers.joinToString(", ")
                    }
                    if (sample.refs.isNotEmpty()) {
                        MetaItem {
                            label = "Refs"
                            sample.refs.forEachIndexed { index, ref ->
                                if (index > 0) +", "
                                a {
                                    href = ref.href
                                    +ref.label
                                }
                            }
                        }
                    }
                    MetaItem {
                        label = "Source"
                        code { +sample.sourcePath }
                    }
                    MetaItem {
                        label = "Route"
                        a {
                            href = sample.hash
                            +sample.hash
                        }
                    }
                }

                val notes = sample.notes
                if (notes != null) {
                    p {
                        className = ClassName("pg-sample-notes")
                        +notes
                    }
                }
            }

            div {
                // Positioned so absolutely placed sample parts stay inside the frame; no z-index on purpose,
                // so the body never forms a stacking context above its neighbours.
                css(ClassName("pg-sample-body")) {
                    position = Position.relative
                }
                +props.children
            }
        }
    }
}

external interface MetaItemProps : PropsWithChildren {
    var label: String
}

private val MetaItem = FC<MetaItemProps> { props ->
    div {
        className = ClassName("pg-meta-item")
        dt { +props.label }
        dd { +props.children }
    }
}
