package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day10ATest = Day10A("input/test_10")
  val day10ATestResult = day10ATest.solve()
  println("Test result for Day10A: $day10ATestResult")
  assert(day10ATestResult == 1)

  val day10A = Day10A("input/input_10")
  val durationA = measureTimeMillis {
    val solution = day10A.solve()
    println("Solution for Day10A: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val day10BTest = Day10B("input/test_10")
  val day10BTestResult = day10BTest.solve()
  println("Test result for Day10B: $day10BTestResult")
  assert(day10BTestResult == 1)

  val day10B = Day10B("input/input_10")
  val duration10B = measureTimeMillis {
    val solution = day10B.solve()
    println("Solution for Day10B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration10B milliseconds")
}


data class Day10A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

data class Day10B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}