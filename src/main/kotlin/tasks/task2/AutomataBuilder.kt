package tasks.task2

class AutomataBuilder<State>(private val initialState: State, private val finishingStates: List<State>) {
    private val transitions = mutableMapOf<State, MutableList<AutomataTransition<State>>>()

    fun addTransition(fromState: State, toState: State, symbol: String) {
        transitions.getOrPut(fromState) { mutableListOf() }.add(AutomataTransition(symbol, toState))
    }

    fun automata(): Automata<State> = Automata(initialState, finishingStates, transitions)
}
