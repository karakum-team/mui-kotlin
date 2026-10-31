package playground.shell

import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.h1
import react.useEffect
import react.useEffectOnce
import react.useState
import web.console.console

val AllPage = FC<Props> {
    // Bumped on every `Report.record`, so check lists that register after the first commit are counted too.
    val (version, setVersion) = useState(0)

    useEffectOnce {
        val unsubscribe = Report.subscribe { setVersion { it + 1 } }
        // Child effects ran before this one, so their records predate the subscription: re-read them once.
        setVersion { it + 1 }
        try {
            awaitCancellation()
        } finally {
            unsubscribe()
        }
    }

    val (passed, total) = Report.summary()

    // Restarted (and the pending log cancelled) on every bump, so a burst of late registrations logs one line.
    useEffect(version) {
        delay(200)
        val (settledPassed, settledTotal) = Report.summary()
        console.log("[playground] all: ${samples.size} samples mounted, $settledPassed/$settledTotal checks PASS")
    }

    h1 { +"All samples" }

    StatusLine {
        id = "all-summary"
        fields = listOf(
            "samples" to samples.size,
            "checks" to "$passed/$total",
        )
    }

    for (sample in samples) {
        SampleFrame {
            key = Key(sample.key)
            this.sample = sample
            sample.content()
        }
    }
}
