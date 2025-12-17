package codes.jrave.aoc2025

import codes.jrave.Direction.*
import codes.jrave.Pos
import codes.jrave.findFirstPosition
import codes.jrave.findPositions
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day07ATest = Day07A("input/2025/input07-test.txt")
    val day07ATestResult = day07ATest.solve()
    println("Day07A: result: $day07ATestResult, expected result: 21, matches: ${day07ATestResult == 21L}")

    val day07A = Day07A("input/2025/input07.txt")
    val duration07A = measureTimeMillis {
        val solution = day07A.solve()
        println("Day07A: result: $solution, expected result: 1687, matches: ${solution == 1687L}")

    }
    println("Solution took $duration07A milliseconds")

    val day07BTest = Day07B("input/2025/input07-test.txt")
    val day07BTestResult = day07BTest.solve()
    println("Day07B: result: $day07BTestResult, expected result: 40, matches: ${day07BTestResult == 40L}")

    val day07B = Day07B("input/2025/input07.txt")
    val duration07B = measureTimeMillis {
        val solution = day07B.solve()
        println("Day07B: result: $solution, expected result: 390684413472684, matches: ${solution == 390684413472684L}")

    }
    println("Solution took $duration07B milliseconds")
}

data class Day07A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val board = input.lines().map { it.toCharArray() }.toTypedArray()
        val endY = board.lastIndex

        val start = board.findFirstPosition('S')
        val splitters = board.findPositions('^').toMutableSet()
        val splitterSize = splitters.size.toLong()
        var beams = mutableSetOf(start)

        while (beams.none { it.y == endY }) {
            val newBeams = mutableSetOf<Pos>()
            for (pos in beams) {
                val posDown = pos + S
                if (posDown in splitters) {
                    splitters.remove(posDown)
                    newBeams.add(posDown + W)
                    newBeams.add(posDown + E)
                } else {
                    newBeams.add(posDown)
                }
            }
            beams = newBeams
        }
        val unusedSplitterSize = splitters.size


        return splitterSize - unusedSplitterSize
    }
}

data class Day07B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val initialBoard = input.lines().map { it.toCharArray() }
        val targetRow = initialBoard.last().map { 'x' }.toCharArray()

        val board = (initialBoard + targetRow).toTypedArray()

        val start = SplitterNode(board.findFirstPosition('S'))

        val splittings = board.findPositions('^')
            .map { SplitterNode(it) }

        val root = splittings.filter { it.pos.x == start.pos.x }.minBy { it.pos.y }

        val endNodes = board.findPositions('x')
            .map { SplitterNode(it) }

        val queue = (splittings).toMutableList()
        val targets = (splittings + endNodes)

        do {
            val cur = queue.removeLast()
            val leftTarget = targets
                .filter { it.pos.x == cur.pos.x - 1 && it.pos.y > cur.pos.y }
                .minBy { it.pos.y }

            val rightTarget = targets
                .filter { it.pos.x == cur.pos.x + 1 && it.pos.y > cur.pos.y }
                .minBy { it.pos.y }

            leftTarget.parents.add(cur)
            rightTarget.parents.add(cur)

        } while (queue.isNotEmpty())

        val sum = endNodes.sumOf { it.findPaths(root) }

        return sum
    }

    data class SplitterNode(
        val pos: Pos,
        val parents: MutableSet<SplitterNode> = emptySet<SplitterNode>().toMutableSet(),
        var paths: Long? = null
    ) {
        fun findPaths(root: SplitterNode): Long {
            if (this == root) {
                return 1L
            }

            if (paths == null) {
                val parentPaths = parents.sumOf { it.findPaths(root) }
                paths = parentPaths
                return parentPaths
            }

            return paths!!
        }
    }
}
