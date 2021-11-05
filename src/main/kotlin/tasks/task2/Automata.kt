package tasks.task2

import de.m3y.kformat.Table
import java.lang.StringBuilder


typealias State = String

data class Automata(
    val initialState: State,
    val finishingStates: List<State>,
    val transitions: Map<State, List<AutomataTransition>>
) {

    fun table(): String {
        val symbols = transitions.entries.flatMap { it.value }.map { it.symbol }.distinct()
        val rows = transitions.mapValues { mapEntry ->
            val groupedTransitions = mapEntry.value.groupBy { it.symbol }
            listOf(mapEntry.key) + symbols.map { groupedTransitions.getOrDefault(it, emptyList()).map { it.destinationState } }
        }
        return de.m3y.kformat.table {
            header("state", *symbols.toTypedArray())

            for (row in rows) {
                row(*row.value.map { it.toString() }.toTypedArray())
            }

            hints {
                borderStyle = Table.BorderStyle.SINGLE_LINE // or NONE
            }
        }.render(StringBuilder()).toString()
    }

    override fun toString(): String {
        return "Automata: Initial State = $initialState, Finishing States = $finishingStates\n${table()}"
    }
}

data class AutomataTransition(val symbol: String, val destinationState: State)
