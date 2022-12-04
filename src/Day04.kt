fun main() {
    fun part1(input: List<String>): Int {
        return input.count {
            val (first, second) = it.split(',')
            val (firstStart, firstEnd) = first.split('-')
            val (secondStart, secondEnd) = second.split('-')
            val firstRange = firstStart.toInt()..firstEnd.toInt()
            val secondRange = secondStart.toInt()..secondEnd.toInt()

            (firstRange.first >= secondRange.first && firstRange.last <= secondRange.last) ||
                    (secondRange.first >= firstRange.first && secondRange.last <= firstRange.last)
        }
    }

    fun part2(input: List<String>): Int {
        return input.count {
            val (first, second) = it.split(',')
            val (firstStart, firstEnd) = first.split('-')
            val (secondStart, secondEnd) = second.split('-')
            val firstRange = firstStart.toInt()..firstEnd.toInt()
            val secondRange = secondStart.toInt()..secondEnd.toInt()

            firstRange.intersect(secondRange).isNotEmpty()
        }
    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
