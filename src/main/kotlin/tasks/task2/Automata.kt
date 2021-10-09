package tasks.task2

import org.jetbrains.dataframe.*


typealias State = String

data class Automata(
    val initialState: State,
    val finishingStates: List<State>,
    val transitions: Map<State, List<AutomataTransition>>
) {

    fun table(): DataFrame<Any?> {
        val symbols = transitions.entries.flatMap { it.value }.map { it.symbol }.distinct()
        val rows = transitions.mapValues { mapEntry ->
            val groupedTransitions = mapEntry.value.groupBy { it.symbol }
            listOf(mapEntry.key) + symbols.map { groupedTransitions.getOrDefault(it, emptyList()).map { it.destinationState } }
        }
        var df = dataFrameOf(listOf("state") + symbols).fill(0, "")
        for (row in rows) {
            val dfRow = row.value.map { it.toString() }
            df = df.append(*dfRow.toTypedArray())
        }
        return df
    }

    override fun toString(): String {
        return """
            Automata: Initial State = $initialState, Finishing States = $finishingStates
            ${table()}
        """.trimIndent()
    }
}

data class AutomataTransition(val symbol: String, val destinationState: State)
