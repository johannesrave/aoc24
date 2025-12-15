package codes.jrave.aoc2025

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day07ATest = Day07A("input/2025/input07-test.txt")
    val day07ATestResult = day07ATest.solve()
    println("Day07A: result: $day07ATestResult, expected result: 21, matches: ${day07ATestResult == 21L}")

    val day07A = Day07A("input/2025/input07.txt")
    val duration07A = measureTimeMillis {
        val solution = day07A.solve()
        println("Day07A: result: $solution, expected result: 7326876294741, matches: ${solution == 7326876294741}")

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
        return 0L
    }
}

data class Day07B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}