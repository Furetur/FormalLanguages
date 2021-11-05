package tasks.task2

fun <State> nfa2dfa(automata: Automata<State>): Automata<Set<State>> {
    // data about previous automata
    val transitionSymbols = automata.transitionSymbols
    // build new automata
    val newInitialState = setOf(automata.initialState)
    val allGeneratedNewStates = mutableSetOf<Set<State>>().also { it.add(newInitialState) }
    val newStatesQueue = ArrayDeque<Set<State>>().also { it.add(newInitialState) }
    val newFinishingStates = mutableListOf<Set<State>>() // it is mutable :(

    val builder = AutomataBuilder(newInitialState, newFinishingStates)
    while (newStatesQueue.isNotEmpty()) {
        val currentNewState = newStatesQueue.removeLast()
        for (symbol in transitionSymbols) {
            val nextNewState = automata.performAllTransitions(currentNewState, symbol)
            if (nextNewState.isNotEmpty()) {
                builder.addTransition(currentNewState, nextNewState, symbol)
                if (nextNewState !in allGeneratedNewStates) {
                    newStatesQueue.add(nextNewState)
                    allGeneratedNewStates.add(nextNewState)
                }
            }
        }
    }
    allGeneratedNewStates.forEach { newState ->
        val newStateIncludesFinishingState = automata.finishingStates.any { it in newState }
        if (newStateIncludesFinishingState) {
            newFinishingStates.add(newState)
        }
    }
    return builder.automata()
}


private fun <State> Automata<State>.performAllTransitions(newState: Set<State>, symbol: String): Set<State> =
    newState.flatMap { state -> allDestinations(state, symbol) }.toSet()