import java.util.ArrayDeque
import java.util.Deque
import java.util.NoSuchElementException
fun main() {
    fun part1(input: List<String>): Int {
        return findMarker(input.first(), distinctCharacters = 4)
    }

    fun part2(input: List<String>): Int {
        return findMarker(input.first(), distinctCharacters = 14)
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 26)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

fun findMarker(packet: String, distinctCharacters: Int): Int {
    for (i in distinctCharacters..packet.length) {
        if (packet.substring(i - distinctCharacters, i).toSet().size == distinctCharacters) {
            return i
        }
    }

    return -1
}