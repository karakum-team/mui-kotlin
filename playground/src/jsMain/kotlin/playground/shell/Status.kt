package playground.shell

import react.ChildrenBuilder
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.use
import react.useEffect
import web.console.console
import web.cssom.ClassName
import web.dom.ElementId

/**
 * Renders `<p id=… data-status=… class="pg-status …">k=v k=v …</p>`.
 */
fun ChildrenBuilder.statusLine(
    id: String,
    vararg fields: Pair<String, Any?>,
    className: ClassName? = null,
) {
    p {
        this.id = ElementId(id)
        asDynamic()["data-status"] = id
        this.className = if (className != null) ClassName("pg-status $className") else ClassName("pg-status")
        +fields.joinToString(" ") { (key, value) -> "$key=$value" }
    }
}

/**
 * Renders a PASS/FAIL list and reports it to [Report] and the console.
 * Uses hooks, so call it unconditionally at the top level of a sample's render.
 */
fun ChildrenBuilder.checkList(
    id: String,
    checks: List<Check>,
) {
    val sampleKey = use(SampleContext)?.key ?: "unframed"
    val passed = checks.count { it.passed }
    val total = checks.size
    val signature = checks.joinToString("|") { "${it.label}=${it.passed}" }

    useEffect(sampleKey, id, signature) {
        Report.record(sampleKey, id, checks)
        val line = "[playground] $sampleKey $id: $passed/$total PASS"
        if (passed == total) console.log(line) else console.error(line)
    }

    ul {
        this.id = ElementId(id)
        asDynamic()["data-checks"] = id
        className = ClassName("pg-checks")

        for (check in checks) {
            li {
                asDynamic()["data-check"] = if (check.passed) "pass" else "fail"
                +"${check.label}: ${if (check.passed) "PASS" else "FAIL"}"
            }
        }
    }
    p {
        className = ClassName("pg-checks-summary")
        +"$passed/$total PASS"
    }
}

object Report {
    private val results = mutableMapOf<String, List<Check>>()

    fun record(sampleKey: String, id: String, checks: List<Check>) {
        results["$sampleKey $id"] = checks
    }

    /** `passed to total` over every check list mounted so far. */
    fun summary(): Pair<Int, Int> {
        val all = results.values.flatten()
        return all.count { it.passed } to all.size
    }
}
