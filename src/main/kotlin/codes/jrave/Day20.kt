package codes.jrave

import codes.jrave.Direction.*
import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
  val day20ATest = Day20A("input/day20_test")
  val day20ATestResult = day20ATest.solve()
  println("Test result for Day20A: $day20ATestResult")
  assert(day20ATestResult == 1)

  val day20A = Day20A("input/day20_input")
  val durationA = measureTimeMillis {
    val solution = day20A.solve()
    println("Solution for Day20A: $solution")
    assert(solution == 1)
  }
  println("Solution took $durationA milliseconds")

  val day20BTest = Day20B("input/day20_test")
  val day20BTestResult = day20BTest.solve()
  println("Test result for Day20B: $day20BTestResult")
  assert(day20BTestResult == 1)

  val day20B = Day20B("input/day20_input")
  val duration20B = measureTimeMillis {
    val solution = day20B.solve()
    println("Solution for Day20B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration20B milliseconds")
}


data class Day20A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {

    val board = parseBoard(input)

    val startPos = board.findFirstPosition('S')
    val endPos = board.findFirstPosition('E')

    val minBoard = board.minimalDistanceBoard(startPos, endPos)

    val cheatCandidates = board.findPositions('#')
      .filter { pos ->
        // filter walls that frame the board
        (pos.x != 0 && pos.y != 0) && (pos.x != board[0].lastIndex && pos.y != board.lastIndex)
      }

    val verticalBreakThroughs = cheatCandidates
      .filter { pos -> (board[pos + S] != '#' && board[pos + N] != '#') }
      .map { pos -> abs(minBoard[pos + S] - minBoard[pos + N]) - 2 }

    val horizontalBreakThroughs = cheatCandidates
      .filter { pos -> (board[pos + E] != '#' && board[pos + W] != '#') }
      .map { pos -> abs(minBoard[pos + E] - minBoard[pos + W]) - 2 }

    val defaultDistance = minBoard[endPos]

    return (verticalBreakThroughs + horizontalBreakThroughs).filter { it >= 100 }.size
  }
}

data class Day20B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}