package tasks.task3

import de.m3y.kformat.Table
import de.m3y.kformat.table
import java.io.File
import java.lang.StringBuilder

fun main() {
    val text = File("src/main/resources/task3/grammar.txt").readText()
    val tokens = text.tokenize()
    val grammar = parseGrammar(tokens)
    println("Grammar\n$grammar")

    val renderedTable = table {
        header("", *(0..9).map { it.toString() }.toTypedArray() )

        compileGrammar(grammar).chunked(10).withIndex().forEach { (rowIndex, tenInstructions) ->
            row(rowIndex.toString(), *tenInstructions.toTypedArray())
        }

        hints {
            borderStyle = Table.BorderStyle.SINGLE_LINE // or NONE
        }
    }.render().toString()

    println("\nTable\n$renderedTable")

//    println("Compiled\n${compileGrammar(grammar).withIndex().joinToString("\n") { "${it.index}: ${it.value}" }}")
}