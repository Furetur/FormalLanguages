package tasks.task1

import common.lexer.Lexeme
import common.lexer.Lexer
import common.lexer.LexerRule

private val rules = listOf(
    LexerRule("EOFGRAM", "Eofgram".toRegex()),
    LexerRule("STAR", """\*""".toRegex()),
    LexerRule("HASH", "#".toRegex()),
    LexerRule("COMMA", ",".toRegex()),
    LexerRule("SEMI", ";".toRegex()),
    LexerRule("COLON", ":".toRegex()),
    LexerRule("DOT", """\.""".toRegex()),
    LexerRule("LPAREN", """\(""".toRegex()),
    LexerRule("RPAREN", """\)""".toRegex()),
    LexerRule("LBRACKET", """\[""".toRegex()),
    LexerRule("RBRACKET", """]""".toRegex()),
    LexerRule("WS", """\s+""".toRegex()),
    LexerRule("STRING", """'[^']*'""".toRegex()),
    LexerRule("SEMANTIC", """\$[a-zA-Z0-9]+""".toRegex()),
    LexerRule("ID", """[a-zA-Z0-9]+""".toRegex())
)

private val grammarLexer = Lexer(rules)

fun tokenizeGrammar(text: String) = grammarLexer.tokenize(text)

fun Lexeme.isWhitespace(): Boolean = tokenType == "WS"
