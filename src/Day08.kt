typealias Direction = (Pair<Int, Int>, List<String>) -> List<Pair<Int, Int>>
typealias TreePosition = Pair<Int, Int>

fun main() {

    val day = "Day08"

    fun IntRange.cartesianProduct(r2: IntRange): List<Pair<Int, Int>> {
        return flatMap { x -> r2.map { y -> x to y } }
    }

    val byMultiplication: (Int, Int) -> Int = { i1, i2 -> i1 * i2 }

    val up: Direction = { tp: TreePosition, _: List<String> -> val (x, y) = tp; (0 until y).map { x to it }.reversed() }
    val down: Direction =
        { tp: TreePosition, grid: List<String> -> val (x, y) = tp; (y + 1 until grid.size).map { x to it } }
    val left: Direction =
        { tp: TreePosition, _: List<String> -> val (x, y) = tp; (0 until x).map { it to y }.reversed() }
    val right: Direction =
        { tp: TreePosition, grid: List<String> -> val (x, y) = tp; (x + 1 until grid[0].length).map { it to y } }

    val directions = listOf(up, down, left, right)

    fun List<String>.getTreeAt(position: TreePosition): Char {
        val (x, y) = position
        return this[y][x]
    }

    fun List<String>.treePositions() = (0 until this[0].length).cartesianProduct(indices)

    fun part1(grid: List<String>): Int {

        fun TreePosition.canBeSeenFrom(direction: Direction) =
            direction(this, grid).none { (grid.getTreeAt(this) <= grid.getTreeAt(it)) }

        fun TreePosition.isVisibleFromAnyDirection() =
            directions.any { this.canBeSeenFrom(it) }

        fun TreePosition.isOnOuterEdge(): Boolean {
            val (x, y) = this
            return (x == 0) || (x == grid[0].length - 1) || (y == 0) || (y == grid.size - 1)
        }

        fun List<String>.numberOfTreesOnOuterEdge() = treePositions().filter { it.isOnOuterEdge() }.size

        return grid.treePositions()
            .filter { !it.isOnOuterEdge() }
            .count { it.isVisibleFromAnyDirection() } + grid.numberOfTreesOnOuterEdge()
    }

    fun part2(grid: List<String>): Int {

        fun List<TreePosition>.thatCanBeSeenFrom(treePosition: TreePosition): Int {

            fun addViewOfTreeThatCantBeSeenPassed(numberOfLowerTreesCounted: Int) =
                if (isNotEmpty() && numberOfLowerTreesCounted < size) 1 else 0

            return takeWhile { (grid.getTreeAt(treePosition) > grid.getTreeAt(it)) }
                .count().let { it + addViewOfTreeThatCantBeSeenPassed(it) }
        }

        fun TreePosition.numberOfTreesInViewPerDirection() =
            directions.map { direction ->
                val treesInDirection = direction(this, grid)
                treesInDirection.thatCanBeSeenFrom(this)
            }

        fun TreePosition.scenicValue() =
            numberOfTreesInViewPerDirection()
                .reduce(byMultiplication)

        return grid.treePositions()
            .maxOf { it.scenicValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 21)
    val result1 = part1(input)
    println(result1)
    check(result1 == 1717)

    check(part2(testInput) == 8)
    val result2 = part2(input)
    println(result2)
    check(result2 == 321975)
}
