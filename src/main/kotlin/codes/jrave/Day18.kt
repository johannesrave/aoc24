package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day18ATest = Day18A("input/day18_test")
  val day18ATestResult = day18ATest.solve(boardDimension = 7, bytesDropped = 12)
  println("Test result for Day18A: $day18ATestResult")
  assert(day18ATestResult == 1)

  val day18A = Day18A("input/day18_input")
  val durationA = measureTimeMillis {
    val solution = day18A.solve(boardDimension = 71, bytesDropped = 1024)
    println("Solution for Day18A: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val day18BTest = Day18B("input/day18_test")
  val day18BTestResult = day18BTest.solve()
  println("Test result for Day18B: $day18BTestResult")
  assert(day18BTestResult == 1)

  val day18B = Day18B("input/day18_input")
  val duration18B = measureTimeMillis {
    val solution = day18B.solve()
    println("Solution for Day18B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration18B milliseconds")
}


data class Day18A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, boardDimension: Int, bytesDropped: Int): Int {
    val board = emptyBoard(boardDimension, '.')
    val corruptions = input.lines().take(bytesDropped)
      .map { line -> line.split(",") }
      .map { (x, y) -> Pos(y.toInt(), x.toInt()) }


    board.markPositions(corruptions).toPrintString().also { println(it) }
    return 0
  }
}

data class Day18B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}