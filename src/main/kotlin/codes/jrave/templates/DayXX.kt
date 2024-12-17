package codes.jrave.templates

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val dayXXATest = DayXXA("input/dayXX_test")
  val dayXXATestResult = dayXXATest.solve()
  println("Test result for DayXXA: $dayXXATestResult")
  assert(dayXXATestResult == 1)

  val dayXXA = DayXXA("input/dayXX_input")
  val durationA = measureTimeMillis {
    val solution = dayXXA.solve()
    println("Solution for DayXXA: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val dayXXBTest = DayXXB("input/dayXX_test")
  val dayXXBTestResult = dayXXBTest.solve()
  println("Test result for DayXXB: $dayXXBTestResult")
  assert(dayXXBTestResult == 1)

  val dayXXB = DayXXB("input/dayXX_input")
  val durationXXB = measureTimeMillis {
    val solution = dayXXB.solve()
    println("Solution for DayXXB: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationXXB milliseconds")
}


data class DayXXA(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

data class DayXXB(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}