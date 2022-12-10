import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

/**
 * From Day 1 solution.
 * Chunks a list by the entry that matches the given predicate.
 */
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

/**
 * Some classes and methods to handle positions and movements i a 2D-space.
 */
data class Position2D(val x: Int, val y: Int) {
    infix operator fun plus(movement: Movement2D) = Position2D(this.x + movement.dx, this.y + movement.dy)
    infix operator fun minus(other: Position2D) = Movement2D(this.x - other.x, this.y - other.y)

    companion object {
        fun from(p: Pair<Int, Int>) = Position2D(p.first, p.second)
    }
}

data class Movement2D(val dx: Int, val dy: Int)
