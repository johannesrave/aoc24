package codes.jrave.aoc2025

import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    val day09ATest = Day09A("input/2025/input09-test.txt")
    val day09ATestResult = day09ATest.solve()
    println("Day09A: result: $day09ATestResult, expected result: 50, matches: ${day09ATestResult == 50L}")

    val day09A = Day09A("input/2025/input09.txt")
    val duration09A = measureTimeMillis {
        val solution = day09A.solve()
        println("Day09A: result: $solution, expected result: 4767418746, matches: ${solution == 4767418746L}")
    }

    println("Solution took $duration09A milliseconds")

//    val day09BTest = Day09B("input/2025/input09-test.txt")
//    val day09BTestResult = day09BTest.solve()
//    println("Day09B: result: $day09BTestResult, expected result: 25272, matches: ${day09BTestResult == 25272L}")
//
//    val day09B = Day09B("input/2025/input09.txt")
//    val duration09B = measureTimeMillis {
//        val solution = day09B.solve()
//        println("Day09B: result: $solution, expected result: 9259958565, matches: ${solution == 9259958565L}")
//    }
//
//    println("Solution took $duration09B milliseconds")
}

data class Day09A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {

        val redTiles = input.lines().map {
            val (x, y) = it.split(',').map { coord -> coord.toLong() }
            Pair(x, y)
        }

        val squares = findAllRelations(redTiles) { a, b ->
            val xLength = (a.first - b.first).absoluteValue + 1
            val yLength = (a.second - b.second).absoluteValue + 1
            Math.multiplyExact(xLength, yLength)
        }

        return squares.maxBy { it.value }.value
    }
}

data class Day09B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}
