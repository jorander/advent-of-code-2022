fun main() {

    val day = "Day04"

    fun assignmentsAsIntRanges(assignments: String) = assignments.split(",")
        .map { it.split("-") }
        .map { (firstSection, lastSection) -> firstSection.toInt()..lastSection.toInt() }

    fun part1(input: List<String>): Int {
        return input.map(::assignmentsAsIntRanges)
            .count { (firstAssignment, secondAssignment) ->
                (firstAssignment - secondAssignment).isEmpty() || (secondAssignment - firstAssignment).isEmpty()
            }
    }

    fun part2(input: List<String>): Int {
        return input.map(::assignmentsAsIntRanges)
            .count { (firstAssignment, secondAssignment) ->
                (firstAssignment intersect secondAssignment).isNotEmpty()
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 2)
    val result1 = part1(input)
    println(result1)
    check(result1 == 518)

    check(part2(testInput) == 4)
    val result2 = part2(input)
    println(result2)
    check(result2 == 909)
}
