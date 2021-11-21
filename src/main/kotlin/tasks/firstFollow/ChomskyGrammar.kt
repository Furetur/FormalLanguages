package tasks.firstFollow

sealed class ChomskyRule {
    abstract val def: NonTerm
}

data class ChomskyNonTermRule(override val def: NonTerm, val first: NonTerm, val second: NonTerm) : ChomskyRule() {
    override fun toString(): String = "$def := $first $second"
}

data class ChomskyTermRule(override val def: NonTerm, val term: Term) : ChomskyRule() {
    override fun toString(): String = "$def := $term"
}

data class ChomskyGrammar(val mainNonTerm: NonTerm, val rules: Map<NonTerm, Set<ChomskyRule>>) {
    val definedNonTerminals = rules.keys
    val allRules = rules.flatMap { it.value }
}

class ChomskyGrammarBuilder {
    private val rules = mutableMapOf<NonTerm, MutableSet<ChomskyRule>>()
    var mainNonTerm: NonTerm? = null

    fun addRule(def: NonTerm, term: Term) {
        rules.getOrPut(def) { mutableSetOf() }.add(ChomskyTermRule(def, term))
    }

    fun addRule(def: NonTerm, first: NonTerm, second: NonTerm) {
        rules.getOrPut(def) { mutableSetOf() }.add(ChomskyNonTermRule(def, first, second))
    }

    fun build(): ChomskyGrammar {
        return ChomskyGrammar(mainNonTerm ?: error("Main non term not set"), rules)
    }
}
