package tasks.task1

import common.lexer.Lexeme

fun parseGrammar(lexemes: Sequence<Lexeme>) = parseGrammar(LexemeReader(lexemes.toList()))

fun parseGrammar(lexemeReader: LexemeReader): UserGrammarNode {
    val nodes = mutableListOf<Node>()

    while (true) {
        val before = lexemeReader.putMark()
        val lexeme = lexemeReader.advance() ?: error("Unexpected end of file")
        when (lexeme.tokenType) {
            "EOFGRAM" -> {
                nodes.add(LexemeNode(lexeme))
                return UserGrammarNode(nodes)
            }
            "ID" -> {
                lexemeReader.gotoMark(before)
                nodes.add(parseUserGrammarRule(lexemeReader))
            }
            "WS" -> {
                nodes.add(LexemeNode(lexeme))
            }
            else -> error("Unexpected lexeme $lexeme while parsing grammar")
        }
    }
}

fun parseUserGrammarRule(lexemeReader: LexemeReader): UserGrammarRuleNode {
    val left = mutableListOf<Lexeme>()
    var colon: Lexeme? = null
    val right = mutableListOf<Lexeme>()

    for (lexeme in lexemeReader) {
        when (lexeme.tokenType) {
            "COLON" -> {
                colon = lexeme
            }
            "DOT" -> {
                requireNotNull(colon) { "Unexpected dot before right side of the rule" }
                right.add(lexeme)
                require(left.filter { it.tokenType != "WS" }.size == 1) { "Left side can only have 1 non-terminal" }
                return UserGrammarRuleNode(left.map { LexemeNode(it) }, LexemeNode(colon), right.map { LexemeNode(it) })
            }
            "EOFGRAM" -> {
                error("Unexpected Eofgram while parsing grammar rule")
            }
            else -> {
                if (colon == null) {
                    left.add(lexeme)
                } else {
                    right.add(lexeme)
                }
            }
        }
    }
    error("Unexpected end of tokens while parsing grammar rule")
}
