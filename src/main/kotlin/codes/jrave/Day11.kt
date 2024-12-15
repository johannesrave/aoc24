package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day11ATest = Day11A("input/test_11")
  val day11ATestResult = day11ATest.solve()
  println("Test result for Day11A: $day11ATestResult")
  assert(day11ATestResult == 55312)

  val day11A = Day11A("input/input_11")
  val durationA = measureTimeMillis {
    val solution = day11A.solve()
    println("Solution for Day11A: $solution")
    assert(solution == 233050)
  }
  println("Solution took $durationA milliseconds")

  val day11BTest = Day11B("input/test_11")
  val day11BTestResult = day11BTest.solve()
  println("Test result for Day11B: $day11BTestResult")
  assert(day11BTestResult == 55312)

  val day11B = Day11B("input/input_11")
  val duration11B = measureTimeMillis {
    val solution = day11B.solve()
    println("Solution for Day11B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration11B milliseconds")
}


data class Day11A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    var stones = input.split(" ").map { n -> Stone(n) }
    val blinks = 25

    for (i in 0..<blinks) {
      stones = stones.flatMap { it.transform() }
    }
    return stones.size
  }
}

data class Day11B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    var stones = input.split(" ").map { n -> Stone(n) }.asSequence()
    val blinks = 75

    for (i in 0..<blinks) {
      if (i % 5 == 0 || i > blinks - 5) {
        println("after $i blinks there are this many stones: ${stones.count()}")
      }
      stones = stones.flatMap { stone -> stone.transform() }
    }
    return stones.count()
  }
}

private data class Stone(val n: String) {
  fun transform(): List<Stone> = when {
    n == "0" -> listOf(Stone("1"))
    n.length % 2 == 0 -> {
      val left = n.substring(0, n.length / 2)
      val right = n.substring(n.length / 2).toBigInteger().toString()
      listOf(Stone(left), Stone(right))
    }

    else -> listOf(Stone((n.toBigInteger() * 2024.toBigInteger()).toString()))
  }
}