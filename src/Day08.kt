typealias TreePosition = Position2D
typealias Direction = (TreePosition, List<String>) -> List<TreePosition>

fun main() {

    val day = "Day08"

    fun IntRange.cartesianProduct(r2: IntRange): List<Pair<Int, Int>> {
        return flatMap { x -> r2.map { y -> x to y } }
    }

    val byMultiplication: (Int, Int) -> Int = { i1, i2 -> i1 * i2 }

    val up: Direction =
        { tp: TreePosition, _: List<String> -> (0 until tp.y).map { TreePosition(tp.x, it) }.reversed() }
    val down: Direction =
        { tp: TreePosition, grid: List<String> -> (tp.y + 1 until grid.size).map { TreePosition(tp.x, it) } }
    val left: Direction =
        { tp: TreePosition, _: List<String> -> (0 until tp.x).map { TreePosition(it, tp.y) }.reversed() }
    val right: Direction =
        { tp: TreePosition, grid: List<String> -> (tp.x + 1 until grid[0].length).map { TreePosition(it, tp.y) } }

    val directions = listOf(up, down, left, right)

    fun List<String>.getTreeAt(position: TreePosition) = this[position.y][position.x]

    fun List<String>.treePositions() = (0 until this[0].length).cartesianProduct(indices).map(TreePosition::from)

    fun part1(grid: List<String>): Int {

        fun TreePosition.canBeSeenFrom(direction: Direction) =
            direction(this, grid).none { (grid.getTreeAt(this) <= grid.getTreeAt(it)) }

        fun TreePosition.isVisibleFromAnyDirection() =
            directions.any { this.canBeSeenFrom(it) }

        fun TreePosition.isOnOuterEdge() =
            (x == 0) || (x == grid[0].length - 1) || (y == 0) || (y == grid.size - 1)

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
