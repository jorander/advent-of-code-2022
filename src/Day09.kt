import kotlin.math.abs

typealias Position = Pair<Int, Int>

private fun Position.isCloseTo(other: Position): Boolean {
    val (thisX, thisY) = this
    val (otherX, otherY) = other

    return abs(thisX - otherX) <= 1 && abs(thisY - otherY) <= 1
}

private fun Position.directlyLeftOrRightFrom(other: Position): Boolean {
    val (_, thisY) = this
    val (_, otherY) = other
    return thisY == otherY
}

private fun Position.directlyUpOrDownFrom(other: Position): Boolean {
    val (thisX, _) = this
    val (otherX, _) = other
    return thisX == otherX
}

enum class MoveDirection(
    val moveRequirement: (head: Position, tail: Position) -> Boolean,
    val move: (p: Position) -> Position
) {
    U({ head, tail -> tail.directlyUpOrDownFrom(head) }, { (x, y) -> x to y + 1 }),
    L({ head, tail -> tail.directlyLeftOrRightFrom(head) }, { (x, y) -> x - 1 to y }),
    D({ head, tail -> tail.directlyUpOrDownFrom(head) }, { (x, y) -> x to y - 1 }),
    R({ head, tail -> tail.directlyLeftOrRightFrom(head) }, { (x, y) -> x + 1 to y }),
    UR({ _, _ -> true }, { (x, y) -> x + 1 to y + 1 }),
    UL({ _, _ -> true }, { (x, y) -> x - 1 to y + 1 }),
    DL({ _, _ -> true }, { (x, y) -> x - 1 to y - 1 }),
    DR({ _, _ -> true }, { (x, y) -> x + 1 to y - 1 });
}

fun main() {

    val day = "Day09"

    data class Simulation(
        val numberOfExtraKnots: Int = 0,
        val head: Position = 0 to 0,
        val knots: List<Position> = List(numberOfExtraKnots) { _ -> 0 to 0 },
        val tail: Position = 0 to 0,
        val placesVisitedByTail: Set<Position> = setOf(tail)
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

        private fun calculateNewHeadPosition(direction: String): Position =
            MoveDirection.valueOf(direction).move(head)

        private fun calculateKnotPositions(head: Position): List<Position> =
            knots.runningFold(head) { prev, tail -> tail.calculateNewPosition(prev) }.drop(1)

        private fun Position.calculateNewPosition(head: Position): Position {
            val tail = this
            return if (tail.isCloseTo(head)) {
                tail
            } else {
                MoveDirection.values()
                    .filter { it.moveRequirement(head, tail) }
                    .map { it.move(tail) }
                    .first { newTail -> newTail.isCloseTo(head) }
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
