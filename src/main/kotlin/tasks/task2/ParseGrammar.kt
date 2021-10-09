package tasks.task2

import guru.zoroark.lixy.LixyToken
import guru.zoroark.lixy.LixyTokenType
import guru.zoroark.lixy.lixy
import guru.zoroark.lixy.matchers.anyOf
import guru.zoroark.lixy.matchers.matches

private val nonTerminalRegex = "<[A-Z]+>".toRegex()
private val terminalRegex = "[a-z+\\-.]+".toRegex()

enum class TokenType : LixyTokenType {
    NON_TERMINAL, TERMINAL, WHITESPACE
}

private val lexer = lixy {
    state {
        matches(nonTerminalRegex.pattern) isToken TokenType.NON_TERMINAL
        matches(terminalRegex.pattern) isToken TokenType.TERMINAL
        anyOf(" ", "\n", "\t") isToken TokenType.WHITESPACE
    }
}

fun parseGrammar(text: String): Grammar {
    val rules = text.lineSequence().map {
        val rule = it.split("::=").map(String::trim)
        assert(rule.size == 2)
        val leftSide = lexer.tokenize(rule[0])
        assert(leftSide.size == 1 && leftSide.first().tokenType == TokenType.NON_TERMINAL)
        val branches = rule[1].split("|").map(String::trim).map(::parseBranch)
        Rule(leftSide.first(), branches)
    }.toList()
    return Grammar(rules)
}

private fun parseBranch(text: String): RuleBranch {
    val tokens = lexer.tokenize(text).filter { it.tokenType != TokenType.WHITESPACE }
    return when (tokens.size) {
        2 -> {
            val terminal = tokens[0]
            val nonTerminal = tokens[1]
            assert(terminal.tokenType == TokenType.TERMINAL && nonTerminal.tokenType == TokenType.NON_TERMINAL)
            RuleBranch(terminal, nonTerminal)
        }
        1 -> {
            val terminal = tokens[0]
            assert(terminal.tokenType == TokenType.TERMINAL)
            RuleBranch(terminal)
        }
        else -> error("Illegal branch $text")
    }
}

data class Grammar(val rules: List<Rule>)
data class Rule(val definedNonTerminal: LixyToken, val branches: List<RuleBranch>)
data class RuleBranch(val terminal: LixyToken, val nonTerminal: LixyToken? = null)
