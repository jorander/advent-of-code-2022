typealias Trees = Grid2D<Char>
typealias TreePosition = Position2D
typealias Direction = (TreePosition, Trees) -> List<TreePosition>

fun main() {

    val day = "Day08"

    val byMultiplication: (Int, Int) -> Int = { i1, i2 -> i1 * i2 }

    val up: Direction =
        { tp: TreePosition, _: Trees -> (0 until tp.y).map { TreePosition(tp.x, it) }.reversed() }
    val down: Direction =
        { tp: TreePosition, trees: Trees -> (tp.y + 1 until trees.height).map { TreePosition(tp.x, it) } }
    val left: Direction =
        { tp: TreePosition, _: Trees -> (0 until tp.x).map { TreePosition(it, tp.y) }.reversed() }
    val right: Direction =
        { tp: TreePosition, trees: Trees -> (tp.x + 1 until trees.width).map { TreePosition(it, tp.y) } }

    val directions = listOf(up, down, left, right)

    fun part1(input: List<String>): Int {
        val trees = Trees.from(input)

        fun TreePosition.canBeSeenFrom(direction: Direction) =
            direction(this, trees).none { (trees[this]) <= trees[it] }

        fun TreePosition.isVisibleFromAnyDirection() =
            directions.any { this.canBeSeenFrom(it) }

        fun TreePosition.isOnOuterEdge() = isOnOuterEdgeIn(trees)

        fun Trees.numberOfTreesOnOuterEdge() = allPositions.filter { it.isOnOuterEdge() }.size

        return (trees.allPositions
            .filter { !it.isOnOuterEdge() }
            .count { it.isVisibleFromAnyDirection() } + trees.numberOfTreesOnOuterEdge())
    }

    fun part2(input: List<String>): Int {
        val trees = Trees.from(input)

        fun List<TreePosition>.thatCanBeSeenFrom(treePosition: TreePosition): Int {

            fun addViewOfTreeThatCantBeSeenPassed(numberOfLowerTreesCounted: Int) =
                if (isNotEmpty() && numberOfLowerTreesCounted < size) 1 else 0

            return takeWhile { (trees[treePosition] > trees[it]) }
                .count().let { it + addViewOfTreeThatCantBeSeenPassed(it) }
        }

        fun TreePosition.numberOfTreesInViewPerDirection() =
            directions.map { direction ->
                val treesInDirection = direction(this, trees)
                treesInDirection.thatCanBeSeenFrom(this)
            }

        fun TreePosition.scenicValue() =
            numberOfTreesInViewPerDirection()
                .reduce(byMultiplication)

        return trees.allPositions
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
