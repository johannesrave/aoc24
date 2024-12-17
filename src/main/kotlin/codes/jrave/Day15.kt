package codes.jrave.templates

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day15ATest = Day15A("input/day15_test")
  val day15ATestResult = day15ATest.solve()
  println("Test result for Day15A: $day15ATestResult")
  assert(day15ATestResult == 1)

  val day15A = Day15A("input/day15_input")
  val durationA = measureTimeMillis {
    val solution = day15A.solve()
    println("Solution for Day15A: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val day15BTest = Day15B("input/day15_test")
  val day15BTestResult = day15BTest.solve()
  println("Test result for Day15B: $day15BTestResult")
  assert(day15BTestResult == 1)

  val day15B = Day15B("input/day15_input")
  val duration15B = measureTimeMillis {
    val solution = day15B.solve()
    println("Solution for Day15B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration15B milliseconds")
}


data class Day15A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

data class Day15B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}