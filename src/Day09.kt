import kotlin.math.abs

typealias Knot = Position2D

private fun Knot.isCloseTo(other: Knot) = (this - other).run { abs(dx) <= 1 && abs(dy) <= 1 }

fun main() {

    val day = "Day09"

    data class Simulation(
        val numberOfExtraKnots: Int = 0,
        val head: Knot = Knot(0, 0),
        val knots: List<Knot> = List(numberOfExtraKnots) { _ -> head },
        val tail: Knot = head,
        val placesVisitedByTail: Set<Knot> = setOf(tail)
    ) {
        fun moveHead(direction: String): Simulation {
            val newHeadPosition = calculateNewHeadPosition(direction)
            val newKnotPositions = calculateKnotPositions(newHeadPosition)
            val newTailPosition =
                tail.calculateNewPosition(if (numberOfExtraKnots > 0) newKnotPositions.last() else newHeadPosition)

            return this.copy(
                head = newHeadPosition,
                knots = newKnotPositions,
                tail = newTailPosition,
                placesVisitedByTail = placesVisitedByTail + newTailPosition
            )
        }

        private fun calculateNewHeadPosition(direction: String) =
            head + when (direction) {
                "U" -> Movement2D(0, 1)
                "L" -> Movement2D(-1, 0)
                "D" -> Movement2D(0, -1)
                "R" -> Movement2D(1, 0)
                else -> {
                    throw IllegalArgumentException("Unknown input: $direction")
                }
            }

        private fun calculateKnotPositions(head: Knot) =
            knots.runningFold(head) { prev, tail -> tail.calculateNewPosition(prev) }.drop(1)

        private fun Knot.calculateNewPosition(head: Knot): Knot {
            val tail = this
            return if (tail.isCloseTo(head)) {
                tail
            } else {
                tail + Movement2D((head.x - tail.x).coerceIn(-1, 1), (head.y - tail.y).coerceIn(-1, 1))
            }
        }
    }

    fun List<String>.simulate(simulation: Simulation) =
        this.flatMap {
            val (direction, steps) = it.split(" ")
            List(steps.toInt()) { _ -> direction }
        }.fold(simulation) { runningSimulation, direction -> runningSimulation.moveHead(direction) }

    fun part1(input: List<String>): Int {
        return input.simulate(Simulation())
            .placesVisitedByTail.size
    }

    fun part2(input: List<String>): Int {
        return input.simulate(Simulation(numberOfExtraKnots = 8))
            .placesVisitedByTail.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val testInput2 = readInput("${day}_test2")
    val input = readInput(day)

    check(part1(testInput) == 13)
    val result1 = part1(input)
    println(result1)
    check(result1 == 6090)

    check(part2(testInput) == 1)
    check(part2(testInput2) == 36)
    val result2 = part2(input)
    println(result2)
    check(result2 == 2566)
}
