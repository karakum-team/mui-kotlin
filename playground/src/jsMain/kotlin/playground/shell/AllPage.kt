package playground.shell

import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.h1
import react.useEffectOnce
import web.console.console

val AllPage = FC<Props> {
    // Parent effects run after every child effect, so all frames and check lists have reported by now.
    useEffectOnce {
        val (passed, total) = Report.summary()
        console.log("[playground] all: ${samples.size} samples mounted, $passed/$total checks PASS")
    }

    h1 { +"All samples" }

    for (sample in samples) {
        SampleFrame {
            key = Key(sample.key)
            this.sample = sample
            sample.content()
        }
    }
}
