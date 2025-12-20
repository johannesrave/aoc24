package codes.jrave.aoc2025

import codes.jrave.Direction.*
import codes.jrave.Pos
import codes.jrave.buildCombinations
import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    val day10ATest = Day10A("input/2025/input10-test.txt")
    val day10ATestResult = day10ATest.solve()
    println("Day10A: result: $day10ATestResult, expected result: 50, matches: ${day10ATestResult == 50L}")

    val day10A = Day10A("input/2025/input10.txt")
    val duration10A = measureTimeMillis {
        val solution = day10A.solve()
        println("Day10A: result: $solution, expected result: 4767418746, matches: ${solution == 4767418746L}")
    }

    println("Solution took $duration10A milliseconds")

//    val day10BTest = Day10B("input/2025/input10-test.txt")
//    val day10BTestResult = day10BTest.solve()
//    println("Day10B: result: $day10BTestResult, expected result: 24, matches: ${day10BTestResult == 24L}")
//
//    val day10B = Day10B("input/2025/input10.txt")
//    val duration10B = measureTimeMillis {
//        val solution = day10B.solve()
//        println("Day10B: result: $solution, expected result: 1461987144, matches: ${solution == 1461987144L}")
//    }
//
//    println("Solution took $duration10B milliseconds")
}

data class Day10A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {

        val combos = buildCombinations(3, listOf(1,2,5))
        return 0L
    }
}

data class Day10B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }

}
