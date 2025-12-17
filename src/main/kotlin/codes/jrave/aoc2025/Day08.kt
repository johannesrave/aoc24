package codes.jrave.aoc2025

import java.io.File
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.system.measureTimeMillis

fun main() {
    val day08ATest = Day08A("input/2025/input08-test.txt")
    val day08ATestResult = day08ATest.solve(maxConnections = 10)
    println("Day08A: result: $day08ATestResult, expected result: 40, matches: ${day08ATestResult == 40L}")

    val day08A = Day08A("input/2025/input08.txt")
    val duration08A = measureTimeMillis {
        val solution = day08A.solve(maxConnections = 1000)
        println("Day08A: result: $solution, expected result: 123234, matches: ${solution == 123234L}")
    }

    println("Solution took $duration08A milliseconds")

    val day08BTest = Day08B("input/2025/input08-test.txt")
    val day08BTestResult = day08BTest.solve()
    println("Day08B: result: $day08BTestResult, expected result: 25272, matches: ${day08BTestResult == 25272L}")

    val day08B = Day08B("input/2025/input08.txt")
    val duration08B = measureTimeMillis {
        val solution = day08B.solve()
        println("Day08B: result: $solution, expected result: 9259958565, matches: ${solution == 9259958565L}")
    }

    println("Solution took $duration08B milliseconds")
}

data class Day08A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input, maxConnections: Int): Long {
        val boxes = input.lines().map {
            val (x, y, z) = it.split(',').map { coord -> coord.toDouble() }
            JunctionBox(x, y, z)
        }

        val relations = findAllRelations(boxes) { a, b -> a.distanceTo(b) }
        val connections = relations.sortedBy { it.value }.take(maxConnections).toMutableList()
        val circuits = boxes.map { mutableSetOf(it) }.toMutableSet()

        while (connections.isNotEmpty()) {
            val minRel = connections.removeFirst()

            val aCluster = circuits.find { it.contains(minRel.a) } ?: throw IllegalStateException()
            val bCluster = circuits.find { it.contains(minRel.b) } ?: throw IllegalStateException()

            if (aCluster != bCluster) {
                circuits.removeIf { it.contains(minRel.b) }
                aCluster.addAll(bCluster)
            }
        }

        return circuits.map { it.size }.sortedByDescending { it }.take(3).reduce { acc, cur -> acc * cur }.toLong()
    }
}

data class Day08B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        val boxes = input.lines().map {
            val (x, y, z) = it.split(',').map { coord -> coord.toDouble() }
            JunctionBox(x, y, z)
        }

        val relations = findAllRelations(boxes) { a, b -> a.distanceTo(b) }
        val connections = relations.sortedBy { it.value }.toMutableList()
        val circuits = boxes.map { mutableSetOf(it) }.toMutableSet()


        var a: JunctionBox = JunctionBox(0.0, 0.0, 0.0)
        var b: JunctionBox = JunctionBox(0.0, 0.0, 0.0)
        while (circuits.size > 1) {
            val minRel = connections.removeFirst()

            a = minRel.a
            b = minRel.b

            val aCluster = circuits.find { it.contains(a) } ?: throw IllegalStateException()
            val bCluster = circuits.find { it.contains(b) } ?: throw IllegalStateException()

            if (aCluster != bCluster) {
                circuits.removeIf { it.contains(b) }
                aCluster.addAll(bCluster)
            }
        }

        return (a.x * b.x).toLong()
    }
}

data class JunctionBox(val x: Double, val y: Double, val z: Double) {
    fun distanceTo(other: JunctionBox): Double {
        val xDist = (this.x - other.x).pow(2)
        val yDist = (this.y - other.y).pow(2)
        val zDist = (this.z - other.z).pow(2)
        return sqrt(xDist + yDist + zDist)
    }
}


