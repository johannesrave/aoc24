package codes.jrave.aoc2025

import codes.jrave.contains
import codes.jrave.overlaps
import codes.jrave.union
import java.io.File
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
//    val day05ATest = Day05A("input/2025/input5-test.txt")
//    val day05ATestResult = day05ATest.solve()
//    println("Test result for Day05A: $day05ATestResult")
//    assert(day05ATestResult == 3)
//
//    val day05A = Day05A("input/2025/input5.txt")
//    val durationA = measureTimeMillis {
//        val solution = day05A.solve()
//        println("Solution for Day05A: $solution")
//        assert(solution == 674)
//    }
//    println("Solution took $durationA milliseconds")

    val day05BTest = Day05B("input/2025/input5-test.txt")
    val day05BTestResult = day05BTest.solve()
    println("Day5B: result: $day05BTestResult, expected result: 14, matches: ${day05BTestResult == 14.toBigInteger()}")

    val day05B = Day05B("input/2025/input5.txt")
    val duration05B = measureTimeMillis {
        val solution = day05B.solve()
        println(Long.MAX_VALUE)
        println(solution > 279822644469933.toBigInteger())
        println(solution > 279822644470035.toBigInteger())
        println("Day5B: result: $solution, expected result: -, matches: ${solution == null}")

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
    fun solve(input: String = this.input): BigInteger {
        val ranges = input.split("\n\n").first().split("\n")
            .map { line ->
                val (start, end) = line.split("-").map { limit -> limit.toLong() }
                start..end
            }

        val combinedRanges = ranges.fold(emptyList<LongRange>()) { acc, range ->
            val overlappingRanges = acc.filter { range.overlaps(it) }.toSet()
            if (overlappingRanges.isEmpty()) {
                return@fold acc + listOf(range)
            }

            val combinedRange = overlappingRanges.union()
            (acc - overlappingRanges) + listOf(combinedRange)
        }

        val overlaps = combinedRanges.none { combinedRanges.none { other -> it.overlaps(other) } }
        val contains = ranges.filter { range -> combinedRanges.none { other -> range contains other } }

        val longs = combinedRanges.sortedBy { it.first }.map { it to (it.last - it.first + 1) }

        var sum = BigInteger.ZERO
        for (range in combinedRanges) {
            sum = sum.plus((range.last - range.first + 1).toBigInteger())
        }

        return sum
    }
}


