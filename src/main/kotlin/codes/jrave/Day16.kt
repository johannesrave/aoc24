package codes.jrave.templates

import codes.jrave.findPositions
import codes.jrave.parseBoard
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day16ATest0 = Day16A("input/day16_test_0")
  val day16ATest0Result = day16ATest0.solve()
  println("Test result for Day16A: $day16ATest0Result")
  assert(day16ATest0Result == 7036)

  val day16ATest1 = Day16A("input/day16_test_1")
  val day16ATest1Result = day16ATest1.solve()
  println("Test result for Day16A: $day16ATest1Result")
  assert(day16ATest1Result == 11048)

//  val day16A = Day16A("input/day16_input")
//  val durationA = measureTimeMillis {
//    val solution = day16A.solve()
//    println("Solution for Day16A: $solution")
//    assert(solution == 1)
//  }
//  println("Solution took $durationA milliseconds")
//
//  val day16BTest = Day16B("input/day16_test")
//  val day16BTestResult = day16BTest.solve()
//  println("Test result for Day16B: $day16BTestResult")
//  assert(day16BTestResult == 1)
//
//  val day16B = Day16B("input/day16_input")
//  val duration16B = measureTimeMillis {
//    val solution = day16B.solve()
//    println("Solution for Day16B: $solution")
//    assert(solution == 1)
//  }
//  println("Solution took $duration16B milliseconds")
}


data class Day16A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    board.findPositions('S')
    return 0
  }
}

data class Day16B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}