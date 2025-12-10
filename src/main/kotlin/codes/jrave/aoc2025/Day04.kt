package codes.jrave.aoc2025

import codes.jrave.allNeighbours
import codes.jrave.findPositions
import codes.jrave.get
import codes.jrave.parseBoard
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day04ATest = Day04A("input/2025/input4-test.txt")
    val day04ATestResult = day04ATest.solve()
    println("Test result for Day04A: $day04ATestResult")
    assert(day04ATestResult == 13L)

    val day04A = Day04A("input/2025/input4.txt")
    val durationA = measureTimeMillis {
        val solution = day04A.solve()
        println("Solution for Day04A: $solution")
        assert(solution == 1564L)
    }
    println("Solution took $durationA milliseconds")

    val day04BTest = Day04B("input/2025/input4-test.txt")
    val day04BTestResult = day04BTest.solve()
    println("Test result for Day04B: $day04BTestResult")
    assert(day04BTestResult == 154115708116294L)

    val day04B = Day04B("input/2025/input4.txt")
    val duration04B = measureTimeMillis {
        val solution = day04B.solve()
        println("Solution for Day04B: $solution")
        assert(solution < 240995207157572)
        assert(solution > 96275232654780)
    }
    println("Solution took $duration04B milliseconds")
}

data class Day04A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val board = parseBoard(input)

        val blocks = board.findPositions('@')

        val movableBlocks = blocks.filter { pos ->
            val blockedNeighbours = board.allNeighbours(pos).filter { neighbour -> board[neighbour] == '@' }
            blockedNeighbours.size < 4
        }

        return movableBlocks.size.toLong()
    }
}

data class Day04B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }
}


