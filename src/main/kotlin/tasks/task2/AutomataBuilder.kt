package tasks.task2

class AutomataBuilder(private val initialState: State, private val finishingStates: List<State>) {
    private val transitions = mutableMapOf<State, MutableList<AutomataTransition>>()

    fun addTransition(fromState: State, toState: State, symbol: String) {

        transitions.getOrPut(fromState) { mutableListOf() }.add(AutomataTransition(symbol, toState))
    }

    fun automata(): Automata = Automata(initialState, finishingStates, transitions)
}
