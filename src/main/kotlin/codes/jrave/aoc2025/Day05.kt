package codes.jrave.aoc2025

import java.io.File

fun main() {
    val day05ATest = Day05A("input/2025/input4-test.txt")
    val day05ATestResult = day05ATest.solve()
    println("Test result for Day05A: $day05ATestResult")
    assert(day05ATestResult == 3L)

//    val day05A = Day05A("input/2025/input4.txt")
//    val durationA = measureTimeMillis {
//        val solution = day05A.solve()
//        println("Solution for Day05A: $solution")
//        assert(solution == 1564L)
//    }
//    println("Solution took $durationA milliseconds")

//    val day05BTest = Day05B("input/2025/input4-test.txt")
//    val day05BTestResult = day05BTest.solve()
//    println("Test result for Day05B: $day05BTestResult")
//    assert(day05BTestResult == 43L)
//
//    val day05B = Day05B("input/2025/input4.txt")
//    val duration05B = measureTimeMillis {
//        val solution = day05B.solve()
//        println("Solution for Day05B: $solution")
//        assert(solution == 9401L)
//    }
//    println("Solution took $duration05B milliseconds")
}

data class Day05A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}

data class Day05B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}

