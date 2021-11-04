package tasks.task3

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.matches
import tasks.task3.TokenType.*

enum class TokenType : LixyTokenType {
    WHITESPACE,
    NONTERMINAL, TERMINAL, // names
    COLON, DOT, LPAREN, RPAREN, // punctuation
    COMA, SEMI, HASH, // operators
}

private val lexer = lixy {
    state {
        matches("""\s+""") isToken WHITESPACE
        // names
        matches("[a-z]+") isToken TERMINAL
        matches("[A-Z]+") isToken NONTERMINAL
        // punctuation
        ":" isToken COLON
        "." isToken DOT
        "(" isToken LPAREN
        ")" isToken RPAREN
        // operators
        "," isToken COMA
        ";" isToken SEMI
        "#" isToken HASH
    }
}

fun String.tokenize(): List<LixyToken> = lexer.tokenize(this)
