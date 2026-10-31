package playground.shell

import playground.dataCheck
import playground.dataChecks
import playground.dataStatus
import react.FC
import react.Key
import react.Props
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.p
import react.dom.html.ReactHTML.ul
import react.useEffect
import react.useRequired
import web.console.console
import web.cssom.ClassName
import web.dom.ElementId

external interface StatusLineProps : Props {
    var id: String
    var fields: List<Pair<String, Any?>>
    var className: ClassName?
}

/**
 * Renders `<p id=… data-status=… class="pg-status …">k=v k=v …</p>`.
 */
val StatusLine = FC<StatusLineProps> { props ->
    val extraClassName = props.className

    p {
        id = ElementId(props.id)
        dataStatus = props.id
        className = if (extraClassName != null) ClassName("pg-status $extraClassName") else ClassName("pg-status")
        +props.fields.joinToString(" ") { (key, value) -> "$key=$value" }
    }
}

external interface CheckListProps : Props {
    var id: String
    var checks: List<Check>
}

/**
 * Renders a PASS/FAIL list and reports it to [Report] and the console. Must be mounted inside a [SampleFrame].
 */
val CheckList = FC<CheckListProps> { props ->
    val sampleKey = useRequired(SampleContext).key
    val checks = props.checks
    val passed = checks.count { it.passed }
    val total = checks.size
    val signature = checks.joinToString("|") { "${it.label}=${it.passed}" }

    useEffect(sampleKey, props.id, signature) {
        Report.record(sampleKey, props.id, checks)
        val line = "[playground] $sampleKey ${props.id}: $passed/$total PASS"
        if (passed == total) console.log(line) else console.error(line)
    }

    ul {
        id = ElementId(props.id)
        dataChecks = props.id
        className = ClassName("pg-checks")

        for (check in checks) {
            li {
                key = Key(check.label)
                dataCheck = if (check.passed) "pass" else "fail"
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
