fun main() {

    fun <E> List<E>.splitBy(predicate: (E) -> Boolean): List<List<E>> {
        tailrec fun <E> List<E>.accumulatedSplitBy(acc: List<List<E>>, predicate: (E) -> Boolean): List<List<E>> =
            if (this.isEmpty()) {
                acc
            } else {
                val firstList = this.takeWhile { !predicate(it) }
                val rest = this.dropWhile { !predicate(it) }.dropWhile(predicate)
                rest.accumulatedSplitBy(acc + listOf(firstList), predicate)
            }

        return this.accumulatedSplitBy(emptyList(), predicate)
    }

    fun part1(input: List<String>): Int {
        return input.splitBy(String::isEmpty)
            .map { it.map(String::toInt).sum() }
            .max()
    }

    fun part2(input: List<String>): Int {
        return input.splitBy(String::isEmpty)
            .map { it.map(String::toInt).sum() }
            .sortedDescending()
            .take(3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
