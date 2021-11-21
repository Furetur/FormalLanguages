package tasks.firstFollow

import java.util.*

class FirstTable(private val grammar: ChomskyGrammar, val k: Int) {
    private val table = mutableMapOf<NonTerm, MutableSet<TermString>>()
    private val nonTermCallStack = Stack<NonTerm>()
    private var didSomethingUpdate = true

    init {
        require(k >= 1) { "k must be >= 1" }

        while (didSomethingUpdate) {
            didSomethingUpdate = false
            for (nonTerm in grammar.definedNonTerminals) {
                processNonTerm(nonTerm)
            }
        }
    }

//    fun first(sententialForm: SententialForm): Set<TermString> {
//        if (sententialForm.isEmpty()) return emptySet()
//
//        val firstForFirstSymbol: Set<TermString> = when (val firstSymbol = sententialForm[0]) {
//            is Term -> setOf(listOf(firstSymbol))
//            is NonTerm -> first(firstSymbol)
//        }
//        val needToCalculateNext = firstForFirstSymbol.any { string -> string.size < k }
//        val firstForRest = first(sententialForm.drop(1))
//        return kProduct(firstForFirstSymbol, firstForRest)
//    }

    fun first(nonTerm: NonTerm): Set<TermString> {
        return table[nonTerm] ?: error("Non term ${nonTerm.value} is not defined in grammar or is useless.")
    }

    /**
     * Takes recursion into account
     * If non term is processed recursively then it returns what already is calculated
     */
    private fun processNonTerm(nonTerm: NonTerm): Set<TermString> {
        // if we are recursing then we return result prematurely
        // but we allow recursion depth of 2
        if (nonTermCallStack.count { it == nonTerm } > k + 1) {
            return table[nonTerm] ?: emptySet()
        }
        nonTermCallStack.push(nonTerm)
        val rules = grammar.rules[nonTerm] ?: error("Non term ${nonTerm.value} not defined in grammar")
        rules.filterIsInstance<ChomskyTermRule>().forEach { addRule(it) }
        rules.filterIsInstance<ChomskyNonTermRule>().forEach { addRule(it) }
        nonTermCallStack.pop()
        return table[nonTerm] ?: error("Non term ${nonTerm.value} did not have any rules")
    }

    private fun addRule(termRule: ChomskyTermRule) {
        if (!termRule.term.isEpsilon) {
            addTermString(termRule.def, listOf(termRule.term))
        } else {
            addTermString(termRule.def, emptyList())
        }
    }

    private fun addRule(nonTermRule: ChomskyNonTermRule) {
        // left recursion is banned
        val firstForLeft = processNonTerm(nonTermRule.first)
        val needToCalculateRight = firstForLeft.any { string -> string.size < k }
        if (!needToCalculateRight) {
            for (termString in firstForLeft) {
                addTermString(nonTermRule.def, termString)
            }
        } else {
            val firstForRight = processNonTerm(nonTermRule.second)
            for (string in kProduct(firstForLeft, firstForRight, k)) {
                addTermString(nonTermRule.def, string)
            }
        }
    }

    private fun addTermString(nonTerm: NonTerm, termString: TermString) {
        val addedNewString = table.getOrPut(nonTerm) { mutableSetOf() }.add(termString)
        if (addedNewString) {
            didSomethingUpdate = true
        }
    }
}