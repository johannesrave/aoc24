package codes.jrave

import codes.jrave.Direction.*
import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
  val day20ATest = Day20A("input/day20_test")
  val day20ATestResult = day20ATest.solve(savedStepsThreshold = 20)
  println("Test result for Day20A: $day20ATestResult")
  assert(day20ATestResult == 5) // could be parametrized to look for the 5 cheats saving 20 or more

  val day20A = Day20A("input/day20_input")
  val durationA = measureTimeMillis {
    val solution = day20A.solve(savedStepsThreshold = 100)
    println("Solution for Day20A: $solution")
    assert(solution == 1422)
  }
  println("Solution took $durationA milliseconds")

  val day20BTest = Day20B("input/day20_test")
  val day20BTestResult = day20BTest.solve(savedStepsThreshold = 50)
  println("Test result for Day20B: $day20BTestResult")
  assert(day20BTestResult == 285)

  val day20B = Day20B("input/day20_input")
  val duration20B = measureTimeMillis {
    val solution = day20B.solve(savedStepsThreshold = 100)
    println("Solution for Day20B: $solution")
    assert(solution == 1009299)
  }
  println("Solution took $duration20B milliseconds")
}


data class Day20A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, savedStepsThreshold: Int): Int {

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

    return (verticalBreakThroughs + horizontalBreakThroughs).filter { it >= savedStepsThreshold }.size
  }
}

data class Day20B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, savedStepsThreshold: Int): Int {

    val board = parseBoard(input)

    val startPos = board.findFirstPosition('S')
    val endPos = board.findFirstPosition('E')

    val minBoard = board.minimalDistanceBoard(startPos, endPos)

    val positionsOnPath = (board.findPositions('.') + startPos + endPos)

    val directConnections = positionsOnPath.flatMap { pos ->
      // for each position on the path, filter from all other positions where:
      // - the other pos is reachable in 20 tiles by manhattan distance
      // - the difference between them on the minBoard (minus the direct distance) clears the threshold
      positionsOnPath
        .filter { otherPos ->
          val differenceOnDefaultPath = minBoard[otherPos] - minBoard[pos]
          val directDistance = pos.manhattanDistance(otherPos)

          val actualDistanceSaved = differenceOnDefaultPath - directDistance

          (directDistance <= 20 && actualDistanceSaved >= savedStepsThreshold)
        }
    }

    return directConnections.size
  }
}