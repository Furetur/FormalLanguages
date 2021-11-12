package tasks.task3

import com.github.furetur.Node
import com.github.furetur.OperatorNode
import com.github.furetur.TokenNode
import com.github.furetur.Visitor
import guru.zoroark.lixy.LixyToken

fun Node<LixyToken>.infixString(): String = InfixStringVisitor.visit(this)

object InfixStringVisitor : Visitor<LixyToken, String> {
    override fun visit(node: Node<LixyToken>): String = when(node) {
        is TokenNode -> node.token.string
        is OperatorNode -> "(${visit(node.operands[0])}) ${node.operator.string} (${visit(node.operands[1])})"
        else -> error("Unknown node type")
    }
}
