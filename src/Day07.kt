import java.util.*
import kotlin.collections.ArrayList

fun main() {

    val day = "Day07"

    data class Directory(val name: String, var size: Int = 0)

    fun parseDirectorySizes(input: List<String>): List<Directory> {

        fun String.toFileSize() = split(" ")[0].toInt()

        data class State(val path: Stack<Directory>, val total: MutableList<Directory>) {

            fun addFile(fileSize: Int) {
                path.peek().size += fileSize
            }

            fun moveInto(name: String) {
                path.push(
                    Directory(
                        if (path.isEmpty()) {
                            name
                        } else {
                            "${path.peek().name.dropLastWhile { it == '/' }}/$name"
                        }
                    )
                )
            }

            fun moveUp() {
                val currentDir = path.pop()
                total.add(currentDir)
                if (path.isNotEmpty()) {
                    path.peek().size += currentDir.size
                }
            }

            fun moveToRoot() {
                while (path.isNotEmpty()) {
                    moveUp()
                }
            }
        }

        return input
            .filter { !it.startsWith("dir") }
            .filter { !it.startsWith("$ ls") }
            .fold(State(Stack(), ArrayList())) { state, s ->
                if (s == "$ cd ..") {
                    state.apply { moveUp() }
                } else if (s.startsWith("$ cd ")) {
                    state.apply {
                        moveInto(s.removePrefix("$ cd "))
                    }
                } else {
                    state.apply {
                        addFile(s.toFileSize())
                    }
                }
            }.apply { moveToRoot() }.total
    }

    fun part1(input: List<String>): Int {
        return parseDirectorySizes(input)
            .filter { it.size <= 100000 }
            .sumOf(Directory::size)
    }

    fun part2(input: List<String>): Int {
        val directories = parseDirectorySizes(input)
        val usedSpace = directories.first { it.name == "/" }.size
        val freeSpace = 70000000 - usedSpace
        val spaceNeeded = 30000000 - freeSpace

        return directories
            .filter { it.size >= spaceNeeded }
            .minBy { it.size }
            .size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("${day}_test")
    val input = readInput(day)

    check(part1(testInput) == 95437)
    val result1 = part1(input)
    println(result1)
    check(result1 == 1908462)

    check(part2(testInput) == 24933642)
    val result2 = part2(input)
    println(result2)
    check(result2 == 3979145)
}
