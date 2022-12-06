fun main() {

    val day = "Day06"

    fun String.toNumberOfCharsBeforeUniqeSetOfSize(size: Int) = windowed(size)
        .mapIndexed { index, s -> index + size to s }
        .first { (_, s) -> s.toSet().size == size }.first

    fun part1(input: String): Int {
        return input.toNumberOfCharsBeforeUniqeSetOfSize(4)
    }

    fun part2(input: String): Int {
        return input.toNumberOfCharsBeforeUniqeSetOfSize(14)
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput(day)

    check(part1("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)
    val result1 = part1(input.first())
    println(result1)
    check(result1 == 1779)

    check(part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 19)
    check(part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23)
    check(part2("nppdvjthqldpwncqszvftbrmjlhg") == 23)
    check(part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)
    check(part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26)
    val result2 = part2(input.first())
    println(result2)
    check(result2 == 2635)
}
