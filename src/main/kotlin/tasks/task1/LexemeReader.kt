package tasks.task1

import common.lexer.Lexeme

class LexemeReader(private val lexemes: List<Lexeme>) : Iterable<Lexeme> {
    var nextLexemeIndex: Int = 0

    fun advance() = lexemes.getOrNull(nextLexemeIndex++)
    fun putMark(): Int = nextLexemeIndex
    fun gotoMark(mark: Int) {
        nextLexemeIndex = mark
    }

    override fun iterator(): Iterator<Lexeme> = sequence<Lexeme> {
        while (true) {
            val lexeme = advance() ?: return@sequence
            yield(lexeme)
        }
    }.iterator()
}
