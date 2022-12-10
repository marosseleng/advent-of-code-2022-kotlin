fun main() {
    fun part1(input: List<String>): Int {
        return countSignals(input, draw = false)
    }

    fun part2(input: List<String>): Int {
        return countSignals(input, draw = true)
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
//    check(part2(testInput) == 13140)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

fun countSignals(instructions: List<String>, draw: Boolean): Int {
    var cycleCount = 0
    var registerXValue = 1
    val remainingInstructions = instructions.toMutableList()

    val desiredCycles = listOf(20, 60, 100, 140, 180, 220)
    var sum = 0

    val drawingResult = mutableListOf<Char>()

    var currentInstruction: String? = null
    var currentInstructionStartCycle: Int = 0

    fun cycleStart(): Boolean {
        if (currentInstruction != null) {
            println("--START->performing other instruction.")
            return true
        }
        val instruction = remainingInstructions.removeFirstOrNull() ?: return false

        currentInstruction = instruction
        currentInstructionStartCycle = cycleCount
        println("--START->Starting [$instruction] @ $cycleCount.")

        return true
    }

    fun draw() {
        val drawingPosition = cycleCount - 1 // 0-239
        val normalizedPosition = drawingPosition % 40
        val character = if (normalizedPosition in ((registerXValue - 1)..(registerXValue + 1))) {//        val character = if (drawingPosition in ((registerXValue - 1)..(registerXValue + 1))) {
            '#'
        } else {
            '.'
        }
        println("Adding '$character' to drawing result[$drawingPosition].")
        drawingResult.add(drawingPosition, character)
    }

    fun cycleEnd() {
        val instruction = currentInstruction ?: return
        var shouldResetInstruction = false
        when {
            instruction == "noop" -> {
                println("--END->Ending [$instruction].")
                shouldResetInstruction = true
            }

            instruction.startsWith("addx") -> {
                if (cycleCount - currentInstructionStartCycle == 1) {
                    registerXValue += instruction.split(" ")[1].toInt()
                    println("--END->Ending [$instruction]->registerXValue=$registerXValue.")
                    shouldResetInstruction = true
                } else {
                    println("--END->Processing [$instruction].")
                    shouldResetInstruction = false
                }
            }
        }
        if (shouldResetInstruction) {
            println("--END->Resetting instruction.")
            currentInstruction = null
            currentInstructionStartCycle = 0
        }
    }

    while (true) {
        cycleCount++
        println("*** TICK: $cycleCount ***")

        if (!cycleStart()) {
            println("!!! NO INSTRUCTION.")
            break
        }

        if (draw) {
            draw()
        }

        if (cycleCount in desiredCycles) {
            sum += cycleCount * registerXValue
            println("!!! Updating sum = $sum.")
        }

        cycleEnd()
    }

    return sum.also {
        println("====$it====")
        if (draw) {
            println(
                drawingResult.chunked(40)
                    .joinToString(separator = "\n") {
                        it.joinToString(separator = "")
                    }
            )
        }
    }
}