package tasks.task3

sealed class Instruction(val size: Int) {
    protected abstract fun compileInternal(codeBuilder: CodeBuilder): List<String>

    fun compile(codeBuilder: CodeBuilder) = compileInternal(codeBuilder).also { require(it.size == size) }
}

data class TerminalInstruction(val terminal: String) : Instruction(1) {
    override fun compileInternal(codeBuilder: CodeBuilder): List<String> = listOf(terminal)
}

data class GotoInstruction(val label: Label) : Instruction(2) {
    override fun compileInternal(codeBuilder: CodeBuilder): List<String> =
        listOf("↓", label.resolvePosition(codeBuilder).toString())
}

data class CondGotoInstruction(val label: Label) : Instruction(2) {
    override fun compileInternal(codeBuilder: CodeBuilder): List<String> =
        listOf("<", label.resolvePosition(codeBuilder).toString())
}

data class CallInstruction(val label: Label) : Instruction(2) {
    override fun compileInternal(codeBuilder: CodeBuilder): List<String> =
        listOf("* (${label.id})", label.resolvePosition(codeBuilder).toString())
}

data class DotInstruction(val nonTerminal: String) : Instruction(1) {
    override fun compileInternal(codeBuilder: CodeBuilder): List<String> = listOf(". ($nonTerminal)")
}
