package tasks.firstFollow

import ChomskyGrammarBaseVisitor
import ChomskyGrammarLexer
import ChomskyGrammarParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token

fun parseChomsky(text: String): ChomskyGrammar {
    val charStream = CharStreams.fromString(text + "\n")
    val lexer = ChomskyGrammarLexer(charStream)
    lexer.removeErrorListeners()
    val parser = ChomskyGrammarParser(CommonTokenStream(lexer))
    return ChomskyVisitor().buildGrammar(parser.gram()!!)
}

private class ChomskyVisitor : ChomskyGrammarBaseVisitor<Unit>() {
    private val grammarBuilder = ChomskyGrammarBuilder()

    fun buildGrammar(ctx: ChomskyGrammarParser.GramContext?): ChomskyGrammar {
        visitGram(ctx)
        return grammarBuilder.build()
    }

    override fun visitGram(ctx: ChomskyGrammarParser.GramContext?) {
        val grammarRules = ctx?.grammarRule() ?: emptyList()

        for (rule in grammarRules) {
            visit(rule)
        }
    }

    override fun visitNonTermRule(ctx: ChomskyGrammarParser.NonTermRuleContext?) {
        val nonTerm = ctx?.def?.asNonTerm() ?: return
        val first = ctx.first?.asNonTerm() ?: return
        val second = ctx.second?.asNonTerm() ?: return
        grammarBuilder.addRule(nonTerm, first, second)

        if (grammarBuilder.mainNonTerm == null) grammarBuilder.mainNonTerm = nonTerm
    }

    override fun visitTermRule(ctx: ChomskyGrammarParser.TermRuleContext?) {
        val nonTerm = ctx?.def?.asNonTerm() ?: return
        val term = ctx.TERM()?.symbol?.asTerm() ?: return
        grammarBuilder.addRule(nonTerm, term)

        if (grammarBuilder.mainNonTerm == null) grammarBuilder.mainNonTerm = nonTerm
    }

    private fun Token.asNonTerm(): NonTerm = if (text.uppercase() == text) {
        NonTerm(text)
    } else {
        error("Tried to convert $text to NonTerm")
    }

    private fun Token.asTerm(): Term = if (text.lowercase() == text) {
        Term(text)
    } else {
        error("Tried to convert $text to Term")
    }
}