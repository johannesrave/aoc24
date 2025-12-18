package codes.jrave.aoc2025

import codes.jrave.Pos
import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    //    val day09ATest = Day09A("input/2025/input09-test.txt")
    //    val day09ATestResult = day09ATest.solve()
    //    println("Day09A: result: $day09ATestResult, expected result: 50, matches: ${day09ATestResult == 50L}")
    //
    //    val day09A = Day09A("input/2025/input09.txt")
    //    val duration09A = measureTimeMillis {
    //        val solution = day09A.solve()
    //        println("Day09A: result: $solution, expected result: 4767418746, matches: ${solution == 4767418746L}")
    //    }
    //
    //    println("Solution took $duration09A milliseconds")

    val day09BTest = Day09B("input/2025/input09-test.txt")
    val day09BTestResult = day09BTest.solve()
    println("Day09B: result: $day09BTestResult, expected result: 24, matches: ${day09BTestResult == 24L}")

    val day09B = Day09B("input/2025/input09.txt")
    val duration09B = measureTimeMillis {
        val solution = day09B.solve()
        println("Day09B: result: $solution, expected result: 9259958565, matches: ${solution == 9259958565L}")
    }

    println("Solution took $duration09B milliseconds")
}

data class Day09A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {

        val redTiles = input.lines().map {
            val (x, y) = it.split(',').map { coord -> coord.toInt() }
            Pos(x, y)
        }

        val squares = findAllRelations(redTiles) { a, b ->
            val xLength = (a.x - b.x).absoluteValue + 1L
            val yLength = (a.y - b.y).absoluteValue + 1L
            Math.multiplyExact(xLength, yLength)
        }

        return squares.maxBy { it.value }.value
    }
}

data class Day09B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {

        val redTiles = input.lines().map {
            val (x, y) = it.split(',').map { coord -> coord.toInt() }
            Pos(x, y)
        }

        assert(Pos(7, 5).isInside(Pos(2, 3) to Pos(11, 7)))
        assert(!Edge(from = Pos(y = 7, x = 1), to = Pos(y = 11, x = 1)).crosses(Pos(2, 5) to Pos(9, 7)))

        val edges = (redTiles.dropLast(1) zip redTiles.drop(1))
            .map { (from, to) -> Edge(from, to) }

        val selfIntersects = edges.any { edges.any { other -> it != other && it.crosses(other) } }
        println("shape self-intersects: $selfIntersects")

        val squares = findAllRelations(redTiles) { a, b ->
            val xLength = (a.x - b.x).absoluteValue + 1L
            val yLength = (a.y - b.y).absoluteValue + 1L
            Math.multiplyExact(xLength, yLength)
        }

        val squaresWithoutCornersWithin = squares
            .filter { (a, b, s) ->
                redTiles.none { corner ->
                    corner != a && corner != b && corner.isInside(a to b)
                }
            }.filter { (a, b, s) ->
                edges.none { edge ->
                    edge.crosses(a to b)
                }
            }

        val maxArea = squaresWithoutCornersWithin.maxBy { it.value }.value
        return maxArea
    }

}

data class Edge(val from: Pos, val to: Pos) {
    fun crosses(other: Edge): Boolean {
        val noPointOnOther = !from.isOn(other) && !to.isOn(other)
        val noOtherPointOnThis = !other.from.isOn(this) && !other.to.isOn(this)
        val thisXWithinOtherX = from.x in other.from.x..other.to.x
        val otherYWithinThisY = other.from.y in from.y..to.y
        val thisYWithinOtherY = from.y in other.from.y..other.to.y
        val otherXWithinThisX = other.from.x in from.x..to.x
        return noPointOnOther && noOtherPointOnThis &&
            (thisXWithinOtherX && otherYWithinThisY || thisYWithinOtherY && otherXWithinThisX)
    }

    fun crosses(square: Pair<Pos, Pos>): Boolean {
        return this.samplePoints().any { it.isInside(square) }
    }

    fun samplePoints(): List<Pos> {
        val (a, b) = this
        val top = minOf(a.y, b.y) + 1
        val bottom = maxOf(a.y, b.y) - 1
        val left = minOf(a.x, b.x) + 1
        val right = maxOf(a.x, b.x) - 1
        return listOf(Pos(top, left), Pos(bottom, right))
    }

    fun toPoints(): List<Pos> {
        val (a, b) = this
        val top = minOf(a.y, b.y) + 1
        val bottom = maxOf(a.y, b.y) - 1
        val left = minOf(a.x, b.x) + 1
        val right = maxOf(a.x, b.x) - 1
        return (top..bottom).flatMap { y -> (left..right).map { x -> Pos(y, x) } }
    }
}

fun Pos.isOn(edge: Edge): Boolean =
    (x == edge.from.x && y in edge.from.y..edge.to.y) ||
        (x == edge.from.x && y in edge.from.x..edge.to.x)

fun Pos.isInside(square: Pair<Pos, Pos>): Boolean {
    val (a, b) = square
    val top = minOf(a.y, b.y) + 1
    val bottom = maxOf(a.y, b.y) - 1
    val left = minOf(a.x, b.x) + 1
    val right = maxOf(a.x, b.x) - 1
    return (y in top..bottom) && (x in left..right)
}