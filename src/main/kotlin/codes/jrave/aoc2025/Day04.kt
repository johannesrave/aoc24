package codes.jrave.aoc2025

import codes.jrave.*
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
    assert(day04BTestResult == 43L)

    val day04B = Day04B("input/2025/input4.txt")
    val duration04B = measureTimeMillis {
        val solution = day04B.solve()
        println("Solution for Day04B: $solution")
        assert(solution == 9401L)
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

        val movableBlocks = getMovableBlocks(blocks, board)

        return movableBlocks.size.toLong()
    }
}

data class Day04B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val board = parseBoard(input)

        val blocks = board.findPositions('@')

        val remainingBlocks = blocks.toMutableSet()
        while (true) {
            val movableBlocks = remainingBlocks.filter { block ->
                val neighbours = board.allNeighbours(block)
                val remainingNeighbours = neighbours intersect remainingBlocks
                remainingNeighbours.size < 4
            }
            if (movableBlocks.isEmpty()) break
            remainingBlocks -= movableBlocks.toSet()
        }

        return blocks.size - remainingBlocks.size.toLong()
    }
}

private fun getMovableBlocks(
    blocks: Set<Pos>,
    board: Array<CharArray>
): List<Pos> = blocks.filter { block ->
    val blockedNeighbours = board.allNeighbours(block).filter { neighbour -> board[neighbour] == '@' }
    blockedNeighbours.size < 4
}


