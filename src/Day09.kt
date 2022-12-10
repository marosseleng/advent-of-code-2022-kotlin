fun main() {
    fun part1(input: List<String>): Int {
        return countVisitedByTail(input)
    }

    fun part2(input: List<String>): Int {
        return countVisitedByTail2(input)
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 13)
    val testInput2 = readInput("Day09_test_2")
    check(part2(testInput2) == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}

fun countVisitedByTail(instructions: List<String>): Int {
    var headX = 0
    var headY = 0

    var tailX = 0
    var tailY = 0

    val visitedByTail = mutableSetOf(tailX to tailY)

    fun needsTailUpdate(): Boolean {
        val candidates = listOf(
            headX-1 to headY+1,
            headX to headY+1,
            headX+1 to headY+1,
            headX-1 to headY,
            headX to headY,
            headX+1 to headY,
            headX-1 to headY-1,
            headX to headY-1,
            headX+1 to headY-1,
        )

        return (tailX to tailY) !in candidates
    }

    fun moveTail(direction: Char) {
        if (!needsTailUpdate()) {
            return
        }
        val tailPosition = when (direction) {
            'L' -> (headX+1) to headY
            'R' -> (headX-1) to headY
            'U' -> headX to (headY-1)
            else -> headX to (headY+1)
        }
        tailX = tailPosition.first
        tailY = tailPosition.second

        visitedByTail.add(tailPosition)
    }

    fun moveHead(direction: Char, count: Int, headPositions: MutableList<Pair<Int, Int>>, tailPositions: MutableList<Pair<Int, Int>>) {
        repeat(count) {
            when (direction) {
                'L' -> headX -= 1
                'R' -> headX += 1
                'U' -> headY += 1
                'D' -> headY -= 1
            }
            moveTail(direction)
            headPositions.add(headX to headY)
            tailPositions.add(tailX to tailY)
        }
    }

    instructions.forEach {
        val headPositions = mutableListOf<Pair<Int, Int>>()
        val tailPositions = mutableListOf<Pair<Int, Int>>()
        val match = """(\w) (\d+)""".toRegex().matchEntire(it)?.groupValues.orEmpty()
        moveHead(match[1].first(), match[2].toInt(), headPositions, tailPositions)
    }

    return visitedByTail.size
}

fun countVisitedByTail2(instructions: List<String>, knotCount: Int = 10): Int {
    val positions = mutableMapOf<Int, Pair<Int, Int>>()
    repeat(knotCount) {
        positions[it] = 0 to 0
    }

    val visitedByTail = mutableSetOf(0 to 0)

    fun needsTailUpdate(index: Int): Boolean {
        val (headX, headY) = positions[index - 1] ?: return false
        val (tailX, tailY) = positions[index] ?: return false
        val candidates = listOf(
            headX-1 to headY+1,
            headX to headY+1,
            headX+1 to headY+1,
            headX-1 to headY,
            headX to headY,
            headX+1 to headY,
            headX-1 to headY-1,
            headX to headY-1,
            headX+1 to headY-1,
        )

        return (tailX to tailY) !in candidates
    }

    fun moveTail(index: Int, verbosely: Boolean) {
        if (!needsTailUpdate(index)) {
            return
        }
        val (headX, headY) = positions[index - 1] ?: return
        val (tailX, tailY) = positions[index] ?: return
        val distanceX = headX - tailX
        val distanceY = headY - tailY

        var newTailX: Int
        var newTailY: Int

        when {
            distanceX == 2 -> {
                newTailX = tailX + 1
                newTailY = when (distanceY) {
                    1, -1 -> headY
                    2 -> headY - 1
                    -2 -> headY + 1
                    // 0
                    else -> tailY
                }
            }
            distanceX == -2 -> {
                newTailX = tailX - 1
                newTailY = when (distanceY) {
                    1, -1 -> headY
                    2 -> headY - 1
                    -2 -> headY + 1
                    // 0
                    else -> tailY
                }
            }
            distanceY == 2 -> {
                newTailX = when (distanceX) {
                    1, -1 -> headX
                    // 0
                    else -> tailX
                }
                newTailY = tailY + 1
            }
            else -> {
                // distanceY == -2
                newTailX = when (distanceX) {
                    // WRONG
                    1, -1 -> headX
                    else -> tailX
                }
                newTailY = tailY - 1
            }
        }

        positions[index] = newTailX to newTailY
        if (verbosely) {
            println("Moving [$index] from [$tailX;$tailY] to [$newTailX;$newTailY] because head is on [$headX;$headY]")
        }

        moveTail(index + 1, verbosely)

        if (index == knotCount - 1) {
            visitedByTail.add(positions[index] ?: return)
        }
    }

    fun moveHead(direction: Char, count: Int, verbosely: Boolean) {
        repeat(count) {
            val currentHead = positions[0] ?: return@repeat
            when (direction) {
                'L' -> positions[0] = currentHead.copy(first = currentHead.first - 1)
                'R' -> positions[0] = currentHead.copy(first = currentHead.first + 1)
                'U' -> positions[0] = currentHead.copy(second = currentHead.second + 1)
                'D' -> positions[0] = currentHead.copy(second = currentHead.second - 1)
            }
            if (verbosely) {
                println("Moving [0] from [${currentHead.first};${currentHead.second}] to [${positions[0]?.first};${positions[0]?.second}].")
            }
            moveTail(index = 1, verbosely = verbosely)
        }
    }

    instructions.forEachIndexed { index, str ->
        val verbosely = false
        if (verbosely) {
            println("[$index]=>$str")
        }
        val match = """(\w) (\d+)""".toRegex().matchEntire(str)?.groupValues.orEmpty()
        moveHead(match[1].first(), match[2].toInt(), verbosely = verbosely)
        if (verbosely) {
            positions.forEach { (index, pos) ->
                println("[$index]~>[${pos.first};${pos.second}]")
            }
        }
    }

    return visitedByTail.size
}