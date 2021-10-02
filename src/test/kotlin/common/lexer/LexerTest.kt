package common.lexer

import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

internal class LexerTest {
    @Test
    fun `should correctly lex abc`() {
        val rules = listOf(
            LexerRule("A", "a".toRegex()),
            LexerRule("B", "b".toRegex()),
            LexerRule("C", "c".toRegex())
        )
        val lexer = Lexer(rules)

        expectThat(lexer.tokenize("abc").toList()).isEqualTo(listOf(
            Lexeme("A", "a", 0 until 1),
            Lexeme("B", "b", 1 until 2),
            Lexeme("C", "c", 2 until 3)
        ))
    }

    @Test
    fun `should correctly lex a      b with single whitespace`() {
        val rules = listOf(
            LexerRule("A", "a".toRegex()),
            LexerRule("B", "b".toRegex()),
            LexerRule("WS", """\s+""".toRegex())
        )
        val lexer = Lexer(rules)

        expectThat(lexer.tokenize("a      b").toList()).isEqualTo(listOf(
            Lexeme("A", "a", 0 until 1),
            Lexeme("WS", "      ", 1 until 7),
            Lexeme("B", "b", 7 until 8)
        ))
    }
}