package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day02ATest = Day02A("input/day02_test")
  assert(day02ATest.solve() == 2)

  val day02A = Day02A("input/day02_input")
  val durationA = measureTimeMillis { println("Solution for Day02A: ${day02A.solve()}") }
  println("solution took $durationA milliseconds")

  val day02BTest = Day02B("input/day02_test")
  assert(day02BTest.solve() == 4)

  val day02B = Day02B("input/day02_input")
  val durationB = measureTimeMillis { println("Solution for Day02B: ${day02B.solve()}") }
  println("solution took $durationB milliseconds")

}

data class Day02A(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(): Int {
    val reports = input.split("\n")
      .map { report ->
        report.split(" ")
          .map { level -> level.toInt() }
      }

    var safeCounter = 0
    reportLoop@ for (report in reports) {
      val isRising = report[0] < report[1]
      val safeChanges = if (isRising) {
        -1 downTo -3
      } else {
        1..3
      }

      for (i in report.indices) {
        if (i == report.lastIndex) continue

        val change = report[i] - report[i + 1]
        if (change !in safeChanges) continue@reportLoop
      }
      safeCounter++
    }

    return safeCounter
  }
}

data class Day02B(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(): Int {
    val reports = input.split("\n")
      .map { report ->
        report.split(" ")
          .map { level -> level.toInt() }
      }

    var safeCounter = 0
    reportLoop@ for (report in reports) {
      val isRising = report[0] < report[1]
      val safeChanges = if (isRising) {
        -1 downTo -3
      } else {
        1..3
      }

      var jokerUsed = false

      for (i in report.indices) {
        if (i == report.lastIndex) continue

        val change = report[i] - report[i + 1]
        if (change !in safeChanges) {
          if (jokerUsed) continue@reportLoop
          jokerUsed = true
        }
      }
      safeCounter++
    }

    return safeCounter
  }
}