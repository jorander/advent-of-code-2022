fun main() {

    val day = "Day03"

    fun List<String>.findCommonChar() =
        this.drop(1).fold(this.first()) { acc, s -> s.filter { acc.contains(it) } }.first()

    fun calculatePriority(badge: Char) =
        if (badge.isLowerCase()) {
            badge - 'a' + 1
        } else {
            badge - 'A' + 27
        }

    fun part1(input: List<String>): Int {
        return input.map { rucksack ->
            rucksack.chunked(rucksack.length / 2)
                .findCommonChar()
        }.map(::calculatePriority).sum()
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .map(List<String>::findCommonChar)
            .map(::calculatePriority).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 157)
    val result1 = part1(input)
    println(result1)
    check(result1 == 7997)

    check(part2(testInput) == 70)
    val result2 = part2(input)
    println(result2)
    check(result2 == 2545)
}
