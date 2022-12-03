fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { getDuplicateSum(it) }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3).sumOf { findBadgePriority(it) }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

fun getPriority(char: Char): Int {
    return when (char) {
        in 'a'..'z' -> char.code - 96
        in 'A'..'Z' -> char.code - 38
        else -> 0
    }
}

fun findBadgePriority(input: List<String>): Int {
    val first = input[0]
    val second = input[1]
    val third = input[2]
    var index = 0
    for (candidate in first) {
        if (candidate in second && candidate in third) {
            return getPriority(candidate)
        }
    }
    return 0
}

fun getDuplicateSum(line: String): Int {
    val maxIndex = line.length / 2
    var prioritySum = 0

    val secondHalf = line.substring(maxIndex)
    val firstHalf = line.dropLast(maxIndex)

    val alreadyUsedDuplicates = mutableSetOf<Char>()

    secondHalf.forEach {
        if (it in firstHalf && alreadyUsedDuplicates.add(it)) {
            prioritySum += getPriority(it)
        }
    }

    return prioritySum
}