private data class Item(val worryLevel: Long) {
    fun relieved(): Item {
        return Item(worryLevel / 3L)
    }
}

private data class TestedItem(val item: Item, val action: (Item, Array<Monkey>) -> Unit)

private data class Monkey(
    val items: List<Item>,
    val operation: (Item) -> Item,
    val testValue: Long,
    val ifTrue: (Item, Array<Monkey>) -> Unit,
    val ifFalse: (Item, Array<Monkey>) -> Unit
) {
    fun takeTurn(afterInspection: (Item) -> Item) = items.size to items
        .map(::inspect)
        .map(afterInspection)
        .map(::testItem)
        .map(::throwItem)

    private fun inspect(item: Item): Item {
        return operation(item)
    }

    private fun testItem(item: Item) =
        TestedItem(
            item,
            if (item.worryLevel % testValue == 0L) {
                ifTrue
            } else {
                ifFalse
            }
        )

    private fun throwItem(testedItem: TestedItem): (Array<Monkey>) -> Unit =
        { monkeys: Array<Monkey> -> testedItem.action(testedItem.item, monkeys) }

    fun receive(item: Item) = this.copy(items = items + item)
    fun leaveItem() = this.copy(items= items.drop(1))
}

private fun List<String>.toMonkeys() = chunked(String::isEmpty)
    .mapIndexed{ index, monkeyConfiguration ->
        Monkey(
            items = monkeyConfiguration[1].removePrefix("  Starting items: ").split(", ").map { Item(it.toLong()) },
            operation = monkeyConfiguration[2].removePrefix("  Operation: new = old ").split(" ").let {
                val (operation, valueStr) = it
                val value = if (valueStr != "old") valueStr.toLong() else 0
                when (operation) {
                    "*" -> { item: Item -> Item(item.worryLevel * if (valueStr == "old") item.worryLevel else value) }
                    "+" -> { item: Item -> Item(item.worryLevel + if (valueStr == "old") item.worryLevel else value) }
                    else -> {
                        throw IllegalArgumentException("Unknown operation $operation")
                    }
                }

            },
            testValue = monkeyConfiguration[3].removePrefix("  Test: divisible by ").toLong(),
            ifTrue = monkeyConfiguration[4].removePrefix("    If true: throw to monkey ").toInt().let {
                { item: Item, monkeys: Array<Monkey> -> monkeys[index] = monkeys[index].leaveItem(); monkeys[it] = monkeys[it].receive(item) }
            },
            ifFalse = monkeyConfiguration[5].removePrefix("    If false: throw to monkey ").toInt().let {
                { item: Item, monkeys: Array<Monkey> -> monkeys[index] = monkeys[index].leaveItem(); monkeys[it] = monkeys[it].receive(item) }
            }
        )
    }.toTypedArray()

fun main() {

    val day = "Day11"

    fun IntRange.run(monkeys: Array<Monkey>, afterInspection: (Item) -> Item): Long {
        val afterExecution = fold(List(monkeys.size) { 0L }) { numberOfItemsInspected, _ ->
            val numberOfItemsInspectedInRound = monkeys
                .map { monkey ->
                    val (numberOfItems, throwsToExecute) = monkey.takeTurn(afterInspection)
                    throwsToExecute.forEach { it(monkeys) }
                    numberOfItems
                }

            numberOfItemsInspected.zip(numberOfItemsInspectedInRound)
                .map { (i1, i2) -> i1 + i2 }
        }

        return afterExecution.sortedDescending().take(2).reduce(Long::times)
    }

    fun part1(input: List<String>): Long {
        val monkeys = input.toMonkeys()
        val rounds = 1..20
        return rounds.run(monkeys,Item::relieved)
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.toMonkeys()
        val rounds = 1..10000
        return rounds.run(monkeys) { item: Item -> Item(item.worryLevel % monkeys.map { it.testValue }.reduce(Long::times)) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 10605L)
    val result1 = part1(input)
    println(result1)
    check(result1 == 121450L)

    check(part2(testInput) == 2713310158L)
    val result2 = part2(input)
    println(result2)
    check(result2 == 28244037010L)
}
