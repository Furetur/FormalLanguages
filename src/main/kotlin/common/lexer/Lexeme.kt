package common.lexer

data class Lexeme(val tokenType: String, val text: String, val indexRange: IntRange) {
    override fun toString(): String = tokenType
}
