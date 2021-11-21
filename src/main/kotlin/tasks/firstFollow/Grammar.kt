package tasks.firstFollow

sealed class SyntaxSymbol

const val EPSILON = "epsilon"

data class Term(val value: String) : SyntaxSymbol() {
    override fun toString(): String = value

    val isEpsilon = value == EPSILON
}

data class NonTerm(val value: String) : SyntaxSymbol() {
    override fun toString(): String = value
}

typealias SententialForm = List<SyntaxSymbol>

typealias TermString = List<Term>

data class Grammar(val mainNonTerm: NonTerm, val rules: Map<NonTerm, Set<SententialForm>>)
