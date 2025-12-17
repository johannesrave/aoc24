package codes.jrave.aoc2025

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day08ATest = Day08A("input/2025/input08-test.txt")
    val day08ATestResult = day08ATest.solve()
    println("Day08A: result: $day08ATestResult, expected result: 40, matches: ${day08ATestResult == 40L}")

    val day08A = Day08A("input/2025/input08.txt")
    val duration08A = measureTimeMillis {
        val solution = day08A.solve()
        println("Day08A: result: $solution, expected result: 1687, matches: ${solution == 1687L}")
    }

    println("Solution took $duration08A milliseconds")

//    val day08BTest = Day08B("input/2025/input08-test.txt")
//    val day08BTestResult = day08BTest.solve()
//    println("Day08B: result: $day08BTestResult, expected result: 40, matches: ${day08BTestResult == 40L}")
//
//    val day08B = Day08B("input/2025/input08.txt")
//    val duration08B = measureTimeMillis {
//        val solution = day08B.solve()
//        println("Day08B: result: $solution, expected result: 390684413472684, matches: ${solution == 390684413472684L}")
//    }
//
//    println("Solution took $duration08B milliseconds")
}

data class Day08A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}

data class Day08B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}
