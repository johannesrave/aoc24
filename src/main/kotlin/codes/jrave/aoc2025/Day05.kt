package codes.jrave.aoc2025

import codes.jrave.groupIntoUnions
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day05ATest = Day05A("input/2025/input5-test.txt")
    val day05ATestResult = day05ATest.solve()
    println("Test result for Day05A: $day05ATestResult")
    assert(day05ATestResult == 3)

    val day05A = Day05A("input/2025/input5.txt")
    val durationA = measureTimeMillis {
        val solution = day05A.solve()
        println("Solution for Day05A: $solution")
        assert(solution == 674)
    }
    println("Solution took $durationA milliseconds")

    val day05BTest = Day05B("input/2025/input5-test.txt")
    val day05BTestResult = day05BTest.solve()
    println("Day5B: result: $day05BTestResult, expected result: 14, matches: ${day05BTestResult == 14L}")

    val day05B = Day05B("input/2025/input5.txt")
    val duration05B = measureTimeMillis {
        val solution = day05B.solve()
        println("Day5B: result: $solution, expected result: 352509891817881, matches: ${solution == 352509891817881L}")

    }
    println("Solution took $duration05B milliseconds")
}

data class Day05A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Int {
        val ranges = input.split("\n\n").first().split("\n")
            .map { line ->
                val (start, end) = line.split("-").map { limit -> limit.toLong() }
                start..end
            }
        val ids = input.split("\n\n")[1].split("\n").map { it.toLong() }
        return ids.filter { id -> ranges.any { id in it } }.size
    }
}

data class Day05B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val ranges = input.split("\n\n").first().split("\n")
            .map { line ->
                val (start, end) = line.split("-").map { limit -> limit.toLong() }
                start..end
            }

        val combinedRanges = ranges.groupIntoUnions()
        return combinedRanges.sumOf { range -> range.last - range.first + 1 }
    }
}
