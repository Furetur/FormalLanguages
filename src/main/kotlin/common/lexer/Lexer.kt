package common.lexer

import common.filterNotNullValues

data class LexerRule(val tokenType: String, val rule: Regex) {
    /**
     * @return match if token at [currentPosition] satisties the rule, otherwise null
     */
    fun detectToken(text: String, currentPosition: Int): MatchResult? {
        val match = rule.find(text, currentPosition) ?: return null
        return if (match.range.first == currentPosition) {
            match
        } else {
            null
        }
    }

    override fun toString(): String = tokenType
}

class Lexer(private val lexerRules: List<LexerRule>) {
    init {
        require(lexerRules.isNotEmpty()) { "List of lexer rules must not be empty!" }
    }

    fun tokenize(text: String): Sequence<Lexeme> = sequence {
        var currentPosition = 0 // current index in [text]

        while (currentPosition < text.length) {
            val currentLexeme = extractLexeme(text, currentPosition)
            yield(currentLexeme)
            currentPosition = currentLexeme.indexRange.last + 1
        }
    }

    private fun extractLexeme(text: String, currentPosition: Int): Lexeme {
        val matches = lexerRules.associateWith { it.detectToken(text, currentPosition) }.filterNotNullValues()
        return if (matches.isNotEmpty()) {
            val (rule, match) = matches.entries.first()
            Lexeme(rule.tokenType, match.value, match.range)
        } else {
            throw NoApplicableLexerRuleException(text, currentPosition)
        }
    }
}

sealed class LexingException(msg: String) : IllegalArgumentException(msg)

class NoApplicableLexerRuleException(text: String, currentPosition: Int, lookahead: Int = 10) : LexingException(
    "No lexer rule can be applied at $currentPosition: ${
        text.substring(currentPosition).take(lookahead)
    }..."
)
