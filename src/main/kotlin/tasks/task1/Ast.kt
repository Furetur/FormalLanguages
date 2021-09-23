package tasks.task1

import common.lexer.Lexeme

abstract class Node {
    abstract val children: List<Node>

    override fun toString(): String = children.joinToString("")
}

class UserGrammarNode(override val children: List<Node>) : Node() {
    val grammarRules: List<UserGrammarRuleNode>
        get() = children.filterIsInstance<UserGrammarRuleNode>()
}

class UserGrammarRuleNode(val left: List<Node>, val colon: LexemeNode, val right: List<Node>) : Node() {
    override val children: List<Node>
        get() = left + listOf(colon) + right

    val leftNonTerminal = left.filterIsInstance<LexemeNode>().filter{ it.lexeme.tokenType == "ID" }.first()
}

class LexemeNode(val lexeme: Lexeme) : Node() {
    override val children: List<Node> = emptyList()
    override fun toString(): String = lexeme.text
}
