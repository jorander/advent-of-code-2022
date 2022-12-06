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