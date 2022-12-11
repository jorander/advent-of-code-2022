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
 * Creates pairs with all combinations of elements in the two collections.
 */
infix fun <E> Collection<E>.cartesianProduct(r2: Collection<E>): List<Pair<E, E>> {
    return flatMap { x -> r2.map { y -> x to y } }
}

/**
 * Some classes and methods to handle positions and movements in a 2D-space.
 */
data class Position2D(val x: Int, val y: Int) {
    infix operator fun plus(movement: Movement2D) = Position2D(this.x + movement.dx, this.y + movement.dy)

    infix operator fun minus(other: Position2D) = Movement2D(this.x - other.x, this.y - other.y)

    fun isOnOuterEdgeIn(grid: Grid2D<*>) =
        (x == 0) || (x == grid.width - 1) || (y == 0) || (y == grid.height - 1)

    companion object {
        fun from(p: Pair<Int, Int>) = Position2D(p.first, p.second)
    }
}

/**
 * A zero-indexed 2D-space where (0,0) is in the top left corner.
 */
interface Grid2D<out E> {
    val height: Int
    val width: Int

    val allPositions: List<Position2D>

    companion object {

        @JvmName("from1")
        fun from(data: List<CharSequence>): Grid2D<Char> = ListCharSequenceGrid2D(data)

        @JvmName("from2")
        fun <E> from(data: List<List<E>>): Grid2D<E> = ListGrid2D(data)
    }

    operator fun get(position: Position2D): E

}

data class ListGrid2D<out E>(private val data: List<List<E>>) : Grid2D<E> {
    override val height: Int
        get() = data.size
    override val width: Int
        get() = data[0].size

    override val allPositions: List<Position2D>
        get() = ((data[0].indices).toList() cartesianProduct data.indices.toList()).map(Position2D::from)

    override fun get(position: Position2D): E = data[position.y][position.x]
}

data class ListCharSequenceGrid2D(private val data: List<CharSequence>) : Grid2D<Char> {
    override val height: Int
        get() = data.size
    override val width: Int
        get() = data[0].length

    override val allPositions: List<Position2D>
        get() = ((0 until data[0].length).toList() cartesianProduct (data.indices.toList())).map(Position2D::from)

    override fun get(position: Position2D) = data[position.y][position.x]
}

data class Movement2D(val dx: Int, val dy: Int)
