fun main() {
    fun part1(input: List<String>): Long {
        return parseCommands(input).values.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Long {
        val parsedLines = parseCommands(input)
        val rootSize = parsedLines["/"] ?: return -1
        val availableSpace2 = 70000000 - rootSize
        val requiredSpace = 30000000
        val spaceToDelete = requiredSpace - availableSpace2

        return parsedLines.values.filter { it > spaceToDelete }.min()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

fun parseCommands(commands: List<String>): Map<String, Long> {
    var path: String = ""
    val directoriesSizes = mutableMapOf<String, Long>()

    fun cd(directory: String) {
        path = if (directory == "/" && path.isEmpty()) {
            directory
        } else if (directory == "..") {
            path.dropLastWhile { it != '/' }.dropLast(1)
        } else if (path == "/") {
            "$path$directory"
        } else {
            "$path/$directory"
        }
    }

    fun addFileSize(size: Long, targetPath: String = path) {
        val currentSize = directoriesSizes[targetPath] ?: 0L
        val newSize = currentSize + size
        directoriesSizes[targetPath] = newSize
        // update upper dirs
        if (targetPath != "/") {
            var parent = targetPath.dropLastWhile { it != '/' }.dropLast(1)
            if (parent.isEmpty()) {
                parent = "/"
            }
            addFileSize(size, parent)
        }
    }

    val cdRegex = """\$ cd (.+)""".toRegex()
    val lsRegex = """\$ ls""".toRegex()
    val directoryRegex = """dir (.+)""".toRegex()
    val fileRegex = """(\d+) .+""".toRegex()

    for (line in commands) {
        when {
            cdRegex.matches(line) -> {
                val directoryName = cdRegex.matchEntire(line)?.groupValues?.get(1) ?: continue
                cd(directoryName)
            }
            lsRegex.matches(line) -> { /*nothing*/ }
            directoryRegex.matches(line) -> { /* nothing */ }
            fileRegex.matches(line) -> {
                val fileSize = fileRegex.matchEntire(line)?.groupValues?.get(1)?.toLong() ?: continue
                addFileSize(fileSize)
            }
        }
    }

    return directoriesSizes
}