fun main() {
    fun part1(input: List<String>): Long {
        var currentMax = 0L

        var intermediateMax = 0L

        for (line in input) {
            if (line.isBlank()) {
                if (intermediateMax > currentMax) {
                    currentMax = intermediateMax
                }
                intermediateMax = 0
            } else {
                intermediateMax += line.toLong()
            }
        }


        return currentMax
    }

    fun part2(input: List<String>): Long {
        var first = 0L
        var second = 0L
        var third = 0L

        fun applyValue(value: Long) {
            if (value > first) {
                val oldFirst = first
                first = value
                applyValue(oldFirst)
            } else if (value > second) {
                val oldSecond = second
                second = value
                applyValue(oldSecond)
            } else if (value > third) {
                val oldThird = third
                third = value
                applyValue(oldThird)
            }
        }

        var intermediateMax = 0L

        for (line in input) {
            if (line.isBlank()) {
                applyValue(intermediateMax)
                intermediateMax = 0
            } else {
                intermediateMax += line.toLong()
            }
        }


        return first+second+third
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day01")
//    println(part1(input))
    println(part2(input))
}
