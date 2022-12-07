import java.util.ArrayDeque
import java.util.Deque

fun main() {
    fun part1(input: List<String>): String {
        val stacks = parseStacks(input.takeWhile { it.isNotBlank() })

        val instructions = input.dropWhile { it.isNotBlank() }.drop(1)
        applyInstructions(instructions) { howMany, from, to ->
            move(stacks, howMany, from, to, inBatch = false)
        }

        return stacks.joinToString(separator = "") {
            try {
                it.pop()
            } catch (e: NoSuchElementException) {
                ""
            }
        }
    }

    fun part2(input: List<String>): String {
        val stacks = parseStacks(input.takeWhile { it.isNotBlank() })

        val instructions = input.dropWhile { it.isNotBlank() }.drop(1)
        applyInstructions(instructions) { howMany, from, to ->
            move(stacks, howMany, from, to, inBatch = true)
        }

        return stacks.joinToString(separator = "") {
            try {
                it.pop()
            } catch (e: NoSuchElementException) {
                ""
            }
        }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

fun parseStacks(stacks: List<String>): MutableList<Deque<String>> {
    val stacksFromBottom = stacks.reversed()

    val stackCount = stacksFromBottom.first().filter { it.isDigit() }.map { it.toString().toInt() }.max()
    val onlyStacks = stacksFromBottom.drop(1)

    val result = mutableListOf<Deque<String>>()

    repeat(stackCount) {
        result.add(ArrayDeque())
    }

    onlyStacks.forEach { stack ->
        for (i in 0 until stackCount) {
            val characterIndex = 4 * i + 1
            val number = stack.getOrNull(characterIndex)?.toString()?.takeUnless { it.isBlank() } ?: continue

            result[i].push(number)
        }
    }

    return result
}

fun applyInstructions(instructions: List<String>, callback: (howMany: Int, from: Int, to: Int) -> Unit) {
    instructions.forEachIndexed { index, instruction ->
        val values = """move (\d+) from (\d+) to (\d+)""".toRegex().matchEntire(instruction)?.groupValues ?: return@forEachIndexed

        callback(values[1].toInt(), values[2].toInt(), values[3].toInt())
    }
}

fun move(stacks: MutableList<Deque<String>>, howMany: Int, from: Int, to: Int, inBatch: Boolean) {
    if (!inBatch) {
        repeat(howMany) {
            stacks[to - 1].push(stacks[from - 1].pop())
        }
    } else {
        (0 until howMany)
            .map { stacks[from - 1].pop() }
            .reversed()
            .forEach {
                stacks[to - 1].push(it)
            }
    }
}