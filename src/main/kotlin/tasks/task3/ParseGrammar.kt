package tasks.task3

import com.github.furetur.*
import com.github.furetur.cursor.ListTokenCursor
import com.github.furetur.cursor.TokenCursor
import com.github.furetur.dsl.pratt
import guru.zoroark.lixy.LixyToken
import tasks.task3.TokenType.*

data class GrammarNode(val rules: List<GrammarRuleNode>) {
    override fun toString(): String = rules.joinToString("\n")
}

data class GrammarRuleNode(val lvalue: LixyToken, val rvalue: Node<LixyToken>) {
    override fun toString(): String = "${lvalue.string} := ${rvalue.infixString()}."
}

private val nonTerminalDefinitionParser = pratt<LixyToken, TokenType> {
    getTokenType = { token -> token.tokenType as TokenType }

    atomic(NONTERMINAL, TERMINAL)

    grouping(LPAREN to RPAREN)

    infix(COMA) precedence 3
    infix(SEMI) precedence 2
    infix(HASH) precedence 1
}

private class MyTokenCursor(data: List<LixyToken>) : TokenCursor<LixyToken> by ListTokenCursor(data) {
    fun expectNext(tokenType: TokenType): LixyToken {
        val nextToken = next() ?: throw ParsingException("Expected $tokenType but was EOF")
        if (nextToken.tokenType != tokenType) {
            throw ParsingException("At ${nextToken.startsAt} expected $tokenType but received ${nextToken.tokenType}")
        }
        return nextToken
    }
}


fun parseGrammar(tokens: List<LixyToken>): GrammarNode {
    val tokensWithoutWs = tokens.filter { it.tokenType != WHITESPACE }
    val tokenCursor = MyTokenCursor(tokensWithoutWs)

    val rules = mutableListOf<GrammarRuleNode>()
    while (!tokenCursor.isDone) {
        rules.add(parseRule(tokenCursor))
    }

    return GrammarNode(rules)
}

private fun parseRule(tokenCursor: MyTokenCursor): GrammarRuleNode {
    val nonTerminal = tokenCursor.expectNext(NONTERMINAL)
    tokenCursor.expectNext(COLON)
    val rvalue = nonTerminalDefinitionParser.parse(tokenCursor)
    tokenCursor.expectNext(DOT)
    return GrammarRuleNode(nonTerminal, rvalue)
}

