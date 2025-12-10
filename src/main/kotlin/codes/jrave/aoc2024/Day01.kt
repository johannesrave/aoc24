package codes.jrave.aoc2024

import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
  val day01A = Day01A("input/2024/day01_input")
  val durationA = measureTimeMillis { println("Solution for Day01A: ${day01A.solve()}") }
  println("solution took $durationA milliseconds")

  val day01BTest = Day01B("input/2024/day01_test")
  assert(day01BTest.solve() == 31L)


  val day01B = Day01B("input/2024/day01_input")
  val durationB = measureTimeMillis { println("Solution for Day01B: ${day01B.solve()}") }
  println("solution took $durationB milliseconds")

}

data class Day01A(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(): Long {
    val split = input.split("\n").map { it.split("   ") }

    val left = split.map { it.first().toLong() }.sorted()
    val right = split.map { it.last().toLong() }.sorted()

    val diff = (left zip right).sumOf { (left, right) -> abs(left - right) }
    return diff
  }
}

data class Day01B(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(): Long {
    val split = input.split("\n").map { it.split("   ") }

    val leftList = split.map { it.first().toLong() }
    val rightList = split.map { it.last().toLong() }

    val occurrences = mutableMapOf<Long, Long>()

    var similarity: Long = 0
    for (left in leftList) {
      if (left !in occurrences.keys) {
        occurrences[left] = rightList.count { it == left }.toLong()
      }
      similarity += left * occurrences.getOrDefault(left, 0)
    }

    println(occurrences.maxOf { it.value })
    println(occurrences.filter { it.value != 0L })

    return similarity
  }
}