package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day14ATest = Day14A("input/day14_input")
  val day14ATestResult = day14ATest.solve()
  println("Test result for Day14A: $day14ATestResult")
  assert(day14ATestResult == 1)

  val day14A = Day14A("input/day14_test")
  val durationA = measureTimeMillis {
    val solution = day14A.solve()
    println("Solution for Day14A: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val day14BTest = Day14B("input/day14_test")
  val day14BTestResult = day14BTest.solve()
  println("Test result for Day14B: $day14BTestResult")
  assert(day14BTestResult == 1)

  val day14B = Day14B("input/day14_input")
  val duration14B = measureTimeMillis {
    val solution = day14B.solve()
    println("Solution for Day14B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration14B milliseconds")
}


data class Day14A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

data class Day14B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}