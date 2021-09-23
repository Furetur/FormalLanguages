package tasks.task1

import common.lexer.Lexeme

class TokenTable {
    private var nextNonTerminalIndex = 11
    private var nextTerminalIndex = 51
    private var nextSemanticIndex = 101

    private val table = mutableMapOf(
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

    fun putNonTerminal(text: String) {
        if (text in table) error("Internal error: Lexeme $text already in table")
        table[text] = nextNonTerminalIndex++
    }
    fun putTerminal(text: String) {
        if (text in table) error("Internal error: Lexeme $text already in table")
        table[text] = nextTerminalIndex++
    }
    fun putSemantic(text: String) {
        if (text in table) error("Internal error: Lexeme $text already in table")
        table[text] = nextSemanticIndex++
    }

    fun get(text: String) = table[text]
}