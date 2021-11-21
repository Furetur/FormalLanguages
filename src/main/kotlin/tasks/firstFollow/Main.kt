package tasks.firstFollow

import tasks.task3.parseGrammar
import java.io.File

fun main() {
    val text = File("src/main/resources/firstFollow/grammar.txt").readText()
    val grammar = parseChomsky(text)
    val first = FirstTable(grammar, k = 3)
    println("FIRST:")
    for (nonTerm in grammar.rules.keys) {
        println("$nonTerm --> ${first.first(nonTerm).filter { it.isNotEmpty() }.map { it.joinToString("") }}")
    }
    val followTable = FollowTable(grammar, first)
    println("FOLLOW:")
    for (nonTerm in grammar.rules.keys) {
        println("$nonTerm --> ${followTable.follow(nonTerm).map { it.joinToString("") }}")
    }
}