package tasks.task3

@JvmInline
value class Label(val id: String = generateRandomString()) {
    fun resolvePosition(codeBuilder: CodeBuilder) = codeBuilder.getLabelPosition(this)
}

class CodeBuilder {
    private val instructions = mutableListOf<Instruction>()
    private var nextPosition = 1
    private val labels = mutableMapOf<Label, Int /* position */>()

    fun addInstruction(instruction: Instruction) {
        instructions.add(instruction)
        nextPosition += instruction.size
    }

    fun putLabel(label: Label) {
        labels[label] = nextPosition
    }

    fun getLabelPosition(label: Label): Int = labels[label] ?: error("Unknown label ${label.id}")

    fun build(): List<String> = listOf(":") + instructions.flatMap { it.compile(this) }
}

private fun generateRandomString(): String = List(6) { ('a'..'z').random() }.joinToString("")
