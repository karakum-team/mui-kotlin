package playground.samples.treeview.consumer

import react.Props

// Local copy of the application's `PropsWithValue` / `PropsWithChangeableValue`.
external interface PropsWithValue<V> : Props {
    var value: V
}

external interface PropsWithChangeableValue<V> : PropsWithValue<V> {
    var onChange: ((V) -> Unit)?
}
