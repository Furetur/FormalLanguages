package tasks.task2

import java.io.File

fun main() {
    val text = File("src/main/resources/task2/regular_grammar.txt").readText()
    val grammar = parseGrammar(text)
    val automataBuilder = AutomataBuilder(grammar.rules[0].definedNonTerminal.string, listOf("$"))

    for (rule in grammar.rules) {
        val nonTerminal = rule.definedNonTerminal.string.drop(1).dropLast(1)
        for (branch in rule.branches) {
            val destination = branch.nonTerminal?.string?.drop(1)?.dropLast(1) ?: "$"
            automataBuilder.addTransition(nonTerminal, destination, branch.terminal.string)
        }
    }

    println(automataBuilder.automata())
}
