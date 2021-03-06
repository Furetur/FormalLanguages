package tasks.task1

import common.lexer.Lexeme
import java.io.File

fun String.withNumberedLines() = split("\n").withIndex().map { "${it.index + 1}: ${it.value}" }.joinToString("\n")

fun main() {
    val grammar = File("src/main/resources/task1/expression.txt").readText()
    val lexemes = tokenizeGrammar(grammar).toList()
    val table = getTable(lexemes)

    println("\n============ Table ============\n")
    println(table.entries.sortedBy { it.value }.joinToString("\n") { "${it.value} : ${it.key}" })
    println("\n============ Code ============\n")
    println(
        lexemes.map { table[it.text] ?: it.text }.joinToString(" ").withNumberedLines()
    )
}

fun getNonTerminals(lexemes: List<Lexeme>): List<String> = lexemes.filter { it.tokenType != "WS" }.zipWithNext()
    .filter { it.first.tokenType == "ID" && it.second.tokenType == "COLON" }.map { it.first.text }.distinct()

fun getTerminals(lexemes: List<Lexeme>, nonTerminals: List<String>): List<String> =
    lexemes.filter { it.tokenType == "STRING" }.map { it.text }.distinct() +
            (lexemes.filter { it.tokenType == "ID" }.map { it.text }.toSet() - nonTerminals.toSet()).toList()

fun getSemantics(lexemes: List<Lexeme>) = lexemes.filter { it.tokenType == "SEMANTIC" }.map { it.text }.distinct()

fun getTable(lexemes: List<Lexeme>): Map<String, Int> {
    val table = mutableMapOf<String, Int>(
        ":" to 1,
        "(" to 2,
        ")" to 3,
        "." to 4,
        "*" to 5,
        ";" to 6,
        "," to 7,
        "#" to 8,
        "[" to 9,
        "]" to 10,
        "Eofgram" to 1000
    )

    var currentNonTerminalIndex = 11
    val nonTerminals = getNonTerminals(lexemes)
    nonTerminals.forEach { nonTerminal -> table[nonTerminal] = currentNonTerminalIndex++ }

    var currentTerminalIndex = 51
    getTerminals(lexemes, nonTerminals).forEach { terminal -> table[terminal] = currentTerminalIndex++ }

    var currentSemanticIndex = 101
    getSemantics(lexemes).forEach { semantic -> table[semantic] = currentSemanticIndex++ }

    return table
}


