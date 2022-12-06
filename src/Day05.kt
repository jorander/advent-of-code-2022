import java.util.Stack

fun main() {

    val day = "Day05"

    // Some utility functions for Pair
    fun <A, B, R> Pair<A, B>.mapFirst(operation: (A) -> R) = operation(first) to second
    fun <A, B, R> Pair<A, B>.mapSecond(operation: (B) -> R) = first to operation(second)

    fun <A, B, R> Pair<Iterable<A>, B>.foldFirst(initial: R, operation: (acc: R, A) -> R): Pair<R, B> =
        first.fold(initial, operation) to second

    fun <A, B, R> Pair<A, Iterable<B>>.foldSecond(initial: R, operation: (acc: R, B) -> R): Pair<A, R> =
        first to second.fold(initial, operation)


    fun <E> List<E>.chunkedAsPairs() = chunked(2).map { list -> list[0] to list[1] }

    fun List<String>.parse(): Pair<Array<Stack<Char>>, List<String>> {
        val startingStacksAndRearrangementInstructions = chunked(String::isEmpty).chunkedAsPairs().first()
        return startingStacksAndRearrangementInstructions.mapFirst { startingStacks ->
            val numberOfStacks = startingStacks.last().split(" ").last().toInt()
            val emptyStacks = Array(numberOfStacks) { Stack<Char>() }
            startingStacks.dropLast(1).reversed()
                .fold(emptyStacks) { stacks, crates ->
                    ("$crates ").chunked(4).foldIndexed(stacks) { index, acc, crate ->
                        if (crate.isNotBlank()) {
                            acc[index].push(crate.substring(1..2).first())
                        }
                        acc
                    }
                    stacks
                }
        }
    }

    fun Pair<Array<Stack<Char>>, List<String>>.rearrangeWithMoveStrategy(moveStrategy: (Stack<Char>, Stack<Char>) -> List<Stack<Char>>) =
        foldSecond(first) { stacks, rearrangeInstruction ->
            val (_, numberOf, _, from, _, to) = rearrangeInstruction.split((" "))
            moveStrategy(stacks[from.toInt() - 1], stacks[to.toInt() - 1]).windowed(2)
                .forEach { (from, to) ->
                    repeat(numberOf.toInt()) {
                        to.push(from.pop())
                    }
                }
            stacks
        }.first

    fun Array<Stack<Char>>.collectTopCrates() = map(Stack<Char>::pop).joinToString("")

    fun part1(input: List<String>): String {
        return input.parse()
            .rearrangeWithMoveStrategy { from, to -> listOf(from, to) }
            .collectTopCrates()
    }

    fun part2(input: List<String>): String {
        return input.parse()
            .rearrangeWithMoveStrategy { from, to -> listOf(from, Stack(), to) }
            .collectTopCrates()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == "CMZ")
    val result1 = part1(input)
    println(result1)
    check(result1 == "GFTNRBZPF")

    check(part2(testInput) == "MCD")
    val result2 = part2(input)
    println(result2)
    check(result2 == "VRQWPDSGP")
}

private operator fun <E> List<E>.component6() = this[5]
