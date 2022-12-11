fun main() {
    fun part1(input: List<String>): Long {
        val monkeys = parseMonkeys(input, manageWorryLevel = true)

        play(monkeys, rounds = 20)

        return monkeys
            .sortedByDescending { it.inspects }
            .take(2)
            .fold(1) { acc, i -> acc * i.inspects }
    }

    fun part2(input: List<String>): Long {
        val monkeys = parseMonkeys(input, manageWorryLevel = false)

        play(monkeys, rounds = 10000)

        return monkeys
            .sortedByDescending { it.inspects }
            .take(2)
            .fold(1) { acc, i -> acc * i.inspects }
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

fun play(monkeys: List<AbstractMonkey>, rounds: Int) {
    val commonMultiple = monkeys.fold(1L) { a, b -> a * b.divisor }
    repeat(rounds) {
        monkeys.forEach { monkey ->
            monkey.inspectItems(commonMultiple).forEach { (item, monkey) ->
                monkeys[monkey].addItem(item)
            }
        }
    }
}

fun parseMonkeys(input: List<String>, manageWorryLevel: Boolean): List<AbstractMonkey> = input.chunked(7) { monkeyData ->
    //Monkey 1:
    val monkeyPosition = monkeyData[0].split(" ")[1].substringBefore(':').toInt()
    //  Starting items: 79, 60, 97
    val startingItems = monkeyData[1].substringAfter("Starting items: ").split(", ").map { it.toLong() }
    //  Operation: new = old * 19
    val operation: (Long, Long) -> Long
    val operationMatched = """(.) (.+)""".toRegex()
        .matchEntire(monkeyData[2].substringAfter("Operation: new = old "))
        ?.groupValues.orEmpty()
    val sign = operationMatched[1].first()
    val number = operationMatched[2].toLongOrNull()
    when (sign) {
        '+' -> {
            operation = { a, b -> a + b }
        }

        '*' -> {
            operation = { a, b -> a * b }
        }

        '-' -> {
            operation = { a, b -> a - b }
        }

        '/' -> {
            operation = { a, b -> a / b }
        }

        else -> {
            operation = { a, _ -> a }
        }
    }
    //  Test: divisible by 23
    val divisor = monkeyData[3].substringAfter("Test: divisible by ").toLong()
    //    If true: throw to monkey 2
    val trueBranch = monkeyData[4].substringAfter("If true: throw to monkey ").toInt()
    //    If false: throw to monkey 3
    val falseBranch = monkeyData[5].substringAfter("If false: throw to monkey ").toInt()

    if (manageWorryLevel) {
        Monkey(
            index = monkeyPosition,
            startingItems = startingItems,
            operation = { operation(it, number ?: it) },
            divisor = divisor,
            trueIndex = trueBranch,
            falseIndex = falseBranch
        )
    } else {
        CongruenceMonkey(
            index = monkeyPosition,
            startingItems = startingItems,
            operation = operation,
            operationArgument = number,
            divisor = divisor,
            trueIndex = trueBranch,
            falseIndex = falseBranch
        )
    }
}

abstract class AbstractMonkey(
    val index: Int,
    startingItems: List<Long>,
    val divisor: Long,
    private val trueIndex: Int,
    private val falseIndex: Int,
) {
    private val items = mutableListOf<Long>()
    var inspects = 0L
        private set

    init {
        items.addAll(startingItems)
    }

    protected fun getNextMonkey(value: Long): Int = if (value % divisor == 0L) trueIndex else falseIndex

    abstract fun inspectSingleItem(inspectedItem: Long, maxCommonMultiple: Long): Pair<Long, Int>

    fun addItem(item: Long) {
        items.add(item)
    }

    fun inspectItems(maxCommonMultiple: Long): List<Pair<Long, Int>> {
        val result = mutableListOf<Pair<Long, Int>>()

        while (items.isNotEmpty()) {
            val inspectedItem = items.removeFirst()
            inspects++
            result.add(inspectSingleItem(inspectedItem, maxCommonMultiple))
        }

        return result
    }

    override fun toString(): String {
        return "Monkey[$index]: inspects:$inspects, items=${items.joinToString(prefix = "[", separator = ", ", postfix = "]")}"
    }
}

class CongruenceMonkey(
    index: Int,
    startingItems: List<Long>,
    val operation: (Long, Long) -> Long,
    val operationArgument: Long?,
    divisor: Long,
    trueIndex: Int,
    falseIndex: Int,
) : AbstractMonkey(index, startingItems, divisor, trueIndex, falseIndex) {

    override fun inspectSingleItem(inspectedItem: Long, maxCommonMultiple: Long): Pair<Long, Int> {
        val argument = operationArgument ?: inspectedItem

        val preInspectionLevel = operation(inspectedItem, argument)
            .mod(maxCommonMultiple)

        return preInspectionLevel to getNextMonkey(preInspectionLevel)
    }
}

class Monkey(
    index: Int,
    startingItems: List<Long>,
    val operation: (Long) -> Long,
    divisor: Long,
    trueIndex: Int,
    falseIndex: Int,
) : AbstractMonkey(index, startingItems, divisor, trueIndex, falseIndex) {

    override fun inspectSingleItem(inspectedItem: Long, maxCommonMultiple: Long): Pair<Long, Int> {
        val postInspectionLevel = operation(inspectedItem) / 3.toLong()

        return postInspectionLevel to getNextMonkey(postInspectionLevel)
    }
}