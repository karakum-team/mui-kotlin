package playground.samples.treeview.consumer

import web.cssom.Length
import web.cssom.px

// Local copy of the application's theme `Spacing`: the 8px grid every length in the port is expressed in.
const val SPACING: Double = 8.0

inline val Double.spacing: Length
    get() = (this * SPACING).toInt().px

inline val Int.spacing: Length
    get() = (this * SPACING).px
