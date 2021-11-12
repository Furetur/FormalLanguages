package tasks.task3

import com.github.furetur.Node
import com.github.furetur.OperatorNode
import com.github.furetur.TokenNode
import com.github.furetur.Visitor
import guru.zoroark.lixy.LixyToken

fun compileGrammar(grammarNode: GrammarNode): List<String> {
    val codeBuilder = CodeBuilder()
    for (rule in grammarNode.rules) {
        rule.compile(codeBuilder)
    }
    return codeBuilder.build()
}

private fun GrammarRuleNode.compile(codeBuilder: CodeBuilder) {
    // register label with thw name of this non-terminal
    codeBuilder.putLabel(Label(lvalue.string))
    // compile the rvalue
    CompilerVisitor(codeBuilder).visit(rvalue)
    // put dot
    codeBuilder.addInstruction(DotInstruction(lvalue.string))
}

private class CompilerVisitor(private val codeBuilder: CodeBuilder) : Visitor<LixyToken, Unit> {
    override fun visit(node: Node<LixyToken>) {
        when (node) {
            is TokenNode -> node.compile()
            is OperatorNode -> node.compile()
            else -> error("Unknown node in ast")
        }
    }

    private fun TokenNode<LixyToken>.compile() {
        when (token.tokenType) {
            TokenType.TERMINAL -> {
                val terminalString = token.string
                if (terminalString != "epsilon") {
                    codeBuilder.addInstruction(TerminalInstruction(token.string))
                }
            }
            TokenType.NONTERMINAL -> {
                val labelToNonTerminal = Label(token.string)
                codeBuilder.addInstruction(CallInstruction(labelToNonTerminal))
            }
            else -> error("Unknown token type of token node")
        }
    }

    private fun OperatorNode<LixyToken>.compile() {
        when (operator.tokenType) {
            TokenType.COMA -> {
                for (operand in operands) {
                    visit(operand)
                }
            }
            // a;b is a OR b
            TokenType.SEMI -> {
                // compiling a;b
                val bLabel = Label()
                val endLabel = Label()
                // condGoto b
                codeBuilder.addInstruction(CondGotoInstruction(bLabel))
                // a
                visit(operands[0])
                codeBuilder.addInstruction(GotoInstruction(endLabel)) // jump to end
                // b
                codeBuilder.putLabel(bLabel)
                visit(operands[1])
                // end
                codeBuilder.putLabel(endLabel)
            }
            // a#b is a or aba or ababa or ....
            TokenType.HASH -> {
                // compiling a#b
                val aLabel = Label()
                val endLabel = Label()
                // a
                codeBuilder.putLabel(aLabel)
                visit(operands[0])
                // condgoto end
                codeBuilder.addInstruction(CondGotoInstruction(endLabel))
                // b
                visit(operands[1])
                codeBuilder.addInstruction(GotoInstruction(aLabel))
                // end
                codeBuilder.putLabel(endLabel)
            }
        }
    }
}

