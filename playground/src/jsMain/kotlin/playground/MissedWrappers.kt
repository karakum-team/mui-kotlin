package playground

import react.dom.html.HTMLAttributes

// kotlin-react-dom exposes only `data-testid`; the playground needs its own data-* hooks for automation.

// `data-sample="<lib>/<id>"` on every sample frame.
inline var HTMLAttributes<*>.dataSample: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-sample"] = value
    }

// `data-status="<id>"` on every status line.
inline var HTMLAttributes<*>.dataStatus: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-status"] = value
    }

// `data-checks="<id>"` on every check list.
inline var HTMLAttributes<*>.dataChecks: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-checks"] = value
    }

// `data-check="pass|fail"` on every check list item.
inline var HTMLAttributes<*>.dataCheck: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-check"] = value
    }

// `data-index-row="<lib>/<id>"` on every index table row.
inline var HTMLAttributes<*>.dataIndexRow: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-index-row"] = value
    }

// `data-not-found="<hash>"` on the not-found message.
inline var HTMLAttributes<*>.dataNotFound: String
    get() = TODO("Prop is write-only!")
    set(value) {
        asDynamic()["data-not-found"] = value
    }
