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

//    val day07BTest = Day07B("input/2025/input6-test.txt")
//    val day07BTestResult = day07BTest.solve()
//    println("Day07B: result: $day07BTestResult, expected result: 3263827, matches: ${day07BTestResult == 3263827L}")
//
//    val day07B = Day07B("input/2025/input6.txt")
//    val duration07B = measureTimeMillis {
//        val solution = day07B.solve()
//        println("Day07B: result: $solution, expected result: 10756007415204, matches: ${solution == 10756007415204L}")
//
//    }
//    println("Solution took $duration07B milliseconds")
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
        return 0L
    }
}