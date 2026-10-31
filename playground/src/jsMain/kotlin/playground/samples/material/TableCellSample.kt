package playground.samples.material

import mui.material.TableCell
import react.Fragment
import react.create
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.th

// Phase 3: promote to a rendered sample.
fun cell() {
    Fragment.create {
        TableCell {
            component = td
            component = th
        }
    }
}
