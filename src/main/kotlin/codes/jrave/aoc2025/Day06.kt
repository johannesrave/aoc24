package codes.jrave.aoc2025

import codes.jrave.transpose
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day06ATest = Day06A("input/2025/input6-test.txt")
    val day06ATestResult = day06ATest.solve()
    println("Day06A: result: $day06ATestResult, expected result: 4277556, matches: ${day06ATestResult == 4277556L}")

    val day06A = Day06A("input/2025/input6.txt")
    val duration06A = measureTimeMillis {
        val solution = day06A.solve()
        println("Day06A: result: $solution, expected result: 7326876294741, matches: ${solution == 7326876294741}")

    }
    println("Solution took $duration06A milliseconds")

    val day06BTest = Day06B("input/2025/input6-test.txt")
    val day06BTestResult = day06BTest.solve()
    println("Day06B: result: $day06BTestResult, expected result: 3263827, matches: ${day06BTestResult == 3263827L}")

    val day06B = Day06B("input/2025/input6.txt")
    val duration06B = measureTimeMillis {
        val solution = day06B.solve()
        println("Day06B: result: $solution, expected result: 10756006415204, matches: ${solution == 10756006415204L}")

    }
    println("Solution took $duration06B milliseconds")
}

data class Day06A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val lines = input.split("\n")
        val rows = lines.dropLast(1)
            .filter { it.isNotBlank() }
            .map { line ->
                line.trim().split("\\s+".toRegex()).map { n -> n.toLong() }.toLongArray()
            }.toTypedArray()
            .transpose()


        val operators = lines.last().trim().split("\\s".toRegex()).filter { it.isNotBlank() }.toTypedArray()

        val sum = operators.mapIndexed { i, operator ->
            val operands = rows[i]
            when (operator) {
                "+" -> operands.reduce { result, n -> result + n }
                "*" -> operands.reduce { result, n -> result * n }
                else -> throw IllegalArgumentException(operator)
            }
        }.sum()

        return sum
    }
}

data class Day06B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val lines = input.split("\n")
        val rows = lines.dropLast(1)
            .filter { it.isNotBlank() }

        val indices = rows.maxBy { it.length }.indices
        val paddedRows = rows.map { it.padEnd(indices.count(), ' ') }

        val blocksOfOperands = paddedRows
            // turn into array of chararrays to transpose
            .map { row -> row.toCharArray() }.toTypedArray()
            // transpose to bring vertical numbers into horizontal lines
            .transpose()
            // join the characters into numbers, one on each line
            .map { it.joinToString("").trim() }
            // join the whole thing to split on double-newlines and separate into blocks
            .joinToString("\n")
            .split("\n\n")
            // split each block back up into single lines and parse those as longs
            .map {
                it.split("\n")
                    .map { it.toLong() }
            }

        val operators = lines.last().trim().split("\\s".toRegex()).filter { it.isNotBlank() }.toTypedArray()

        val results = operators.mapIndexed { i, operator ->
            val operands = blocksOfOperands[i]
            when (operator) {
                "+" -> operands.reduce { result, n -> result + n }
                "*" -> operands.reduce { result, n -> result * n }
                else -> throw IllegalArgumentException(operator)
            }
        }

        return results.sum()
    }
}