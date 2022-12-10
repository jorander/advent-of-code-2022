const val CRT_WIDTH = 40

fun main() {

    val day = "Day10"

    fun addx(x: Int) = { register: Int -> register + x }
    fun noop() = { register: Int -> register }

    fun List<String>.toRegisterXValueDuringZeroIndexedCycle() = flatMap {
        if (it.startsWith("addx ")) {
            listOf(noop(), addx(it.split(" ")[1].toInt()))
        } else {
            listOf(noop())
        }
    }.runningFold(1) { prevReg, instruction -> instruction(prevReg) }.dropLast(1)

    fun part1(input: List<String>): Int {
        val registerXValueDuringCycle = input.toRegisterXValueDuringZeroIndexedCycle()
        return listOf(20, 60, 100, 140, 180, 220).map { registerXValueDuringCycle[it - 1] to it }
            .sumOf { (register, cycle) -> register * cycle }
    }

    fun part2(input: List<String>): String {

        fun List<Int>.renderSprite(): List<String> =
            mapIndexed { outputIndex, x -> if ((x - 1..x + 1).contains(outputIndex % CRT_WIDTH)) "#" else "." }

        return input.toRegisterXValueDuringZeroIndexedCycle()
            .renderSprite()
            .chunked(CRT_WIDTH)
            .joinToString("\n") { it.joinToString("") }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 13140)
    val result1 = part1(input)
    println(result1)
    check(result1 == 14860)

    val expectedTestOutputPart2 = """|##..##..##..##..##..##..##..##..##..##..
                                      |###...###...###...###...###...###...###.
                                      |####....####....####....####....####....
                                      |#####.....#####.....#####.....#####.....
                                      |######......######......######......####
                                      |#######.......#######.......#######.....""".trimMargin()
    check(part2(testInput) == expectedTestOutputPart2)
    val result2 = part2(input)
    println(result2)
    val expectedResultPart2 = """|###...##..####.####.#..#.#..#.###..#..#.
                                  |#..#.#..#....#.#....#..#.#..#.#..#.#.#..
                                  |#..#.#......#..###..####.#..#.#..#.##...
                                  |###..#.##..#...#....#..#.#..#.###..#.#..
                                  |#.#..#..#.#....#....#..#.#..#.#.#..#.#..
                                  |#..#..###.####.####.#..#..##..#..#.#..#.""".trimMargin()
    check(result2 == expectedResultPart2)
}
