fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { getMyPoints(it[0], it[2]) }
    }

    fun part2(input: List<String>): Int {

        return input.sumOf { getMyPoints2(it[0], it[2]) }
    }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

fun getMyPoints(opponent: Char, myself: Char): Int {
    return when (opponent) {
        'A' -> {
            when (myself) {
                'X' -> {
                    // rock == 1 + draw
                    1 + 3
                }
                'Y' -> {
                    // paper == 2 + win
                    2 + 6
                }
                else -> {
                    // scissors == 3 + loss
                    3 + 0
                }
            }
        }
        'B' -> {
            when (myself) {
                'X' -> {
                    // rock == 1 + loss
                    1 + 0
                }
                'Y' -> {
                    // paper == 2 + draw
                    2 + 3
                }
                else -> {
                    // scissors == 3 + win
                    3 + 6
                }
            }
        }
        else -> {
            when (myself) {
                'X' -> {
                    // rock == 1 + win
                    1 + 6
                }
                'Y' -> {
                    // paper == 2 + loss
                    2 + 0
                }
                else -> {
                    // scissors == 3 + draw
                    3 + 3
                }
            }
        }
    }
}

fun getMyPoints2(opponent: Char, myself: Char): Int {
    return when (opponent) {
        'A' -> {
            // rock
            when (myself) {
                'X' -> {
                    // lose -> scissors
                    0 + 3
                }
                'Y' -> {
                    // draw -> rock
                    3 + 1
                }
                else -> {
                    // win -> paper
                    6 + 2
                }
            }
        }
        'B' -> {
            // paper
            when (myself) {
                'X' -> {
                    // lose -> rock
                    0 + 1
                }
                'Y' -> {
                    // draw -> paper
                    3 + 2
                }
                else -> {
                    // win -> scissors
                    6 + 3
                }
            }
        }
        else -> {
            // scissors
            when (myself) {
                'X' -> {
                    // lose -> paper
                    0 + 2
                }
                'Y' -> {
                    // draw -> scissors
                    3 + 3
                }
                else -> {
                    // win -> rock
                    6 + 1
                }
            }
        }
    }
}
