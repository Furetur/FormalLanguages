package tasks.firstFollow

import java.util.*

class FollowTable(private val grammar: ChomskyGrammar, private val firstTable: FirstTable) {
    val k = firstTable.k
    private val table = mutableMapOf<NonTerm, MutableSet<TermString>>()
    private val nonTermCallStack = Stack<NonTerm>()
    private var didSomethingUpdate = true

    init {
        // add "$" to the main non term
        addTermString(grammar.mainNonTerm, listOf(Term("$")))

        while (didSomethingUpdate) {
            didSomethingUpdate = false
            for (nonTerm in grammar.definedNonTerminals) {
                processNonTerm(nonTerm)
            }
        }
    }

    private fun processNonTerm(nonTerm: NonTerm): Set<TermString> {
        if (nonTermCallStack.count { it == nonTerm } > k + 1) {
            return table[nonTerm] ?: emptySet()
        }
        nonTermCallStack.push(nonTerm)

        val allRulesThatHaveThisNonTerm = grammar.allRules.filterIsInstance<ChomskyNonTermRule>()
            .filter { it.first == nonTerm || it.second == nonTerm }
        for (rule in allRulesThatHaveThisNonTerm) {
            if (rule.first == nonTerm) {
                addTermStrings(
                    nonTerm,
                    kProduct(firstTable.first(rule.second), processNonTerm(rule.def), k)
                )
            }
            if (rule.second == nonTerm) {
                addTermStrings(nonTerm, processNonTerm(rule.def))
            }
        }

        nonTermCallStack.pop()
        return table[nonTerm] ?: emptySet()
    }

    fun follow(nonTerm: NonTerm): Set<TermString> {
        return table[nonTerm] ?: emptySet()
    }

    private fun addTermStrings(nonTerm: NonTerm, termStrings: Iterable<TermString>) {
        termStrings.forEach { addTermString(nonTerm, it) }
    }

    private fun addTermString(nonTerm: NonTerm, termString: TermString) {
        val addedNewString = table.getOrPut(nonTerm) { mutableSetOf() }.add(termString)
        if (addedNewString) {
            didSomethingUpdate = true
        }
    }
}