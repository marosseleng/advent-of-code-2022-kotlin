fun main() {
    fun part1(input: List<String>): Int {
        return countVisibleTrees(constructMap(input))
    }

    fun part2(input: List<String>): Int {
        return getMaxScenicScore(constructMap(input))
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}

fun constructMap(input: List<String>): List<List<Int>> {
    val result = mutableListOf<List<Int>>()
    input.mapTo(result) { row -> row.map { it.toString().toInt() } }
    return result
}

fun countVisibleTrees(map: List<List<Int>>): Int {
    val visibleTrees = mutableSetOf<Pair<Int, Int>>()

    fun checkRows() {
        map.forEachIndexed { rowIndex, row ->
            var highestFromStart = -1
            var highestFromEnd = -1
            for (treeIndex in (0..row.lastIndex)) {
                val fromStart = row[treeIndex]
                if (fromStart > highestFromStart) {
                    val position = rowIndex to treeIndex
                    highestFromStart = fromStart
                    visibleTrees.add(position)
                }
                val endIndex = row.lastIndex - treeIndex
                val fromEnd = row[endIndex]
                if (fromEnd > highestFromEnd) {
                    val position = rowIndex to endIndex
                    highestFromEnd = fromEnd
                    visibleTrees.add(position)
                }
            }
        }
    }

    fun checkColumns() {
        val columnCount = map.first().size

        for (treeIndex in 0 until columnCount) {
            var highestFromStart = -1
            var highestFromEnd = -1

            for (rowIndex in 0..map.lastIndex) {
                val fromStart = map[rowIndex][treeIndex]
                if (fromStart > highestFromStart) {
                    val position = rowIndex to treeIndex
                    highestFromStart = fromStart
                    visibleTrees.add(position)
                }
                val endIndex = map.size - 1 - rowIndex
                val fromEnd = map[endIndex][treeIndex]
                if (fromEnd > highestFromEnd) {
                    val position = endIndex to treeIndex
                    highestFromEnd = fromEnd
                    visibleTrees.add(position)
                }
            }
        }
    }

    checkRows()
    checkColumns()

    return visibleTrees.size
}

fun getMaxScenicScore(map: List<List<Int>>): Int {
    var scenicScore = -1
    val maxX = map.first().lastIndex
    val maxY = map.lastIndex
    for ((y, row) in map.withIndex()) {
        for ((x, tree) in row.withIndex()) {
            val score = getScenicScore(tree, x, y, maxX, maxY, map)
            if (score > scenicScore) {
                scenicScore = score
            }
        }
    }
    return scenicScore
}

fun getScenicScore(myself: Int, x: Int, y: Int, maxX: Int, maxY: Int, map: List<List<Int>>): Int {
    if (x == 0 || y == 0 || x == maxX || y == maxY) {
        // edge trees
        return 0
    }
    // same y, x in (0 until x)
    var start = 0
    for (startX in ((x - 1) downTo 0)) {
        start++
        if (map[y][startX] >= myself) {
            break
        }
    }
    // same x, y in (0 until x)
    var top = 0
    for (topY in (y - 1) downTo 0) {
        top++
        if (map[topY][x] >= myself) {
            break
        }
    }
    // same y, x in (x+1 until maxX)
    var end = 0
    for (endX in (x + 1)..maxX) {
        end++
        if (map[y][endX] >= myself) {
            break
        }
    }
    // same x, y in (y+1 until maxY)
    var bottom = 0
    for (bottomY in (y + 1)..maxY) {
        bottom++
        if (map[bottomY][x] >= myself) {
            break
        }
    }

    return start * top * end * bottom
}