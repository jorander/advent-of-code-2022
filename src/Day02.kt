import Outcome.*
import Outcome.Companion.outcome
import Shape.Companion.myShape
import Shape.Companion.opponentShape

enum class Shape(val opponentCode: String, val myCode: String, val score: Int, val winsOver: Int) {
    ROCK("A", "X", 1, 2),
    PAPER("B", "Y", 2, 0),
    SCISSORS("C", "Z", 3, 1);

    companion object {
        fun opponentShape(opponentCode: String) = values().find { it.opponentCode == opponentCode }!!
        fun myShape(myCode: String) = values().find { it.myCode == myCode }!!
        fun winsOver(shape: Shape) = values().find { it.winsOver == shape.ordinal }!!
        fun loosesTo(shape: Shape) = values().find { it.ordinal == shape.winsOver }!!
    }
}

enum class Outcome(val code: String) {
    WIN("Z"),
    DRAW("Y"),
    LOOSE("X");

    companion object {
        fun outcome(code: String) = values().find { it.code == code }!!
    }
}

fun main() {

    val day = "Day02"

    fun calculateScore(opponentShape: Shape, myShape: Shape) =
        myShape.score + when {
            myShape.winsOver == opponentShape.ordinal -> 6
            myShape == opponentShape -> 3
            else -> 0
        }

    fun part1(input: List<String>): Int {
        return input.map { inputRow ->
            val (opponentCode, myCode) = inputRow.split(" ")
            opponentShape(opponentCode) to myShape(myCode)
        }.sumOf { (opponentShape, myShape) -> calculateScore(opponentShape, myShape) }
    }

    fun part2(input: List<String>): Int {

        fun findMyShape(outcome: Outcome, opponentShape: Shape) = when (outcome) {
            WIN -> Shape.winsOver(opponentShape)
            DRAW -> opponentShape
            LOOSE -> Shape.loosesTo(opponentShape)
        }

        return input.map { inputRow ->
            val (opponentCode, outcomeCode) = inputRow.split(" ")
            opponentShape(opponentCode) to outcome(outcomeCode)
        }
            .map { (opponentShape, outcome) -> opponentShape to findMyShape(outcome, opponentShape) }
            .sumOf { (opponentShape, myShape) -> calculateScore(opponentShape, myShape) }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 15)
    val result1 = part1(input)
    println(result1)
    check(result1 == 14264)

    check(part2(testInput) == 12)
    val result2 = part2(input)
    println(result2)
    check(result2 == 12382)
}

