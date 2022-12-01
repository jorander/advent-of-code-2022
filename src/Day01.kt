fun main() {

    val day = "Day01"

    fun <E> List<E>.chunked(predicate: (E) -> Boolean): List<List<E>> {
        tailrec fun <E> List<E>.accumulatedChunked(acc: List<List<E>>, predicate: (E) -> Boolean): List<List<E>> =
            if (this.isEmpty()) {
                acc
            } else {
                val firstList = this.takeWhile { !predicate(it) }
                val rest = this.dropWhile { !predicate(it) }.dropWhile(predicate)
                rest.accumulatedChunked(acc + listOf(firstList), predicate)
            }

        return this.accumulatedChunked(emptyList(), predicate)
    }

    fun part1(input: List<String>): Int {
        return input.chunked(String::isEmpty)
            .map { it.map(String::toInt).sum() }
            .max()
    }

    fun part2(input: List<String>): Int {
        return input.chunked(String::isEmpty)
            .map { it.map(String::toInt).sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 24000)
    println(part1(input))

    check(part2(testInput) == 45000)
    println(part2(input))
}
