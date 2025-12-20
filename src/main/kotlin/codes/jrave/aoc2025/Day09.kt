package codes.jrave.aoc2025

import codes.jrave.Direction.*
import codes.jrave.Pos
import java.io.File
import kotlin.math.absoluteValue
import kotlin.system.measureTimeMillis

fun main() {
    val day09ATest = Day09A("input/2025/input09-test.txt")
    val day09ATestResult = day09ATest.solve()
    println("Day09A: result: $day09ATestResult, expected result: 50, matches: ${day09ATestResult == 50L}")

    val day09A = Day09A("input/2025/input09.txt")
    val duration09A = measureTimeMillis {
        val solution = day09A.solve()
        println("Day09A: result: $solution, expected result: 4767418746, matches: ${solution == 4767418746L}")
    }

    println("Solution took $duration09A milliseconds")

    val day09BTest = Day09B("input/2025/input09-test.txt")
    val day09BTestResult = day09BTest.solve()
    println("Day09B: result: $day09BTestResult, expected result: 24, matches: ${day09BTestResult == 24L}")

    val day09B = Day09B("input/2025/input09.txt")
    val duration09B = measureTimeMillis {
        val solution = day09B.solve()
        println("Day09B: result: $solution, expected result: 1461987144, matches: ${solution == 1461987144L}")
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
            Pos(y, x)
        }

        val shiftedRedTiles = (redTiles + redTiles.first()).drop(1)

        val edges = (shiftedRedTiles zip redTiles)
            .map { (from, to) -> Edge(from, to) }

        val selfIntersects = edges.any { edges.any { other -> it != other && it.crosses(other) } }
        println("shape self-intersects: $selfIntersects")

        val squares = findAllRelations(redTiles) { a, b -> Square(a, b) }.map { it.value }

        val squaresWithoutCornersWithin = squares
            .filter { s -> redTiles.none { corner -> corner.isInside(s.shrinkByOne()) } }
            .filter { s -> edges.none { edge -> edge.center.isInside(s.shrinkByOne()) } }


        val maxArea = squaresWithoutCornersWithin.maxBy { it.area }.area

        return maxArea
    }

}

data class Square(
    val a: Pos,
    val b: Pos,
    val topLeft: Pos = findTopLeft(a, b),
    val bottomRight: Pos = findBottomRight(a, b),
    val area: Long = findArea(topLeft, bottomRight)
) {

    fun shrinkByOne(): Square {
        return Square(topLeft + E + S, bottomRight + N + W)
    }

    companion object {
        fun findTopLeft(a: Pos, b: Pos): Pos {
            val top = minOf(a.y, b.y)
            val left = minOf(a.x, b.x)
            return Pos(top, left)
        }

        fun findBottomRight(a: Pos, b: Pos): Pos {
            val bottom = maxOf(a.y, b.y)
            val right = maxOf(a.x, b.x)
            return Pos(bottom, right)
        }

        fun findArea(a: Pos, b: Pos): Long {
            val xLength = (a.x - b.x).absoluteValue + 1L
            val yLength = (a.y - b.y).absoluteValue + 1L
            return Math.multiplyExact(xLength, yLength)
        }
    }
}

data class Edge(
    val from: Pos,
    val to: Pos,
    val isHorizontal: Boolean = from.x == to.x,
    val isVertical: Boolean = from.y == to.y,
    val center: Pos = Pos((from.y + to.y) / 2, (from.x + to.x) / 2)
) {
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
}

fun Pos.isOn(edge: Edge): Boolean =
    (x == edge.from.x && y in edge.from.y..edge.to.y) ||
        (x == edge.from.x && y in edge.from.x..edge.to.x)

fun Pos.isInside(s: Square): Boolean {
    return (y in s.topLeft.y..s.bottomRight.y) && (x in s.topLeft.x..s.bottomRight.x)
}