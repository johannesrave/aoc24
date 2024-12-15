package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day10ATest = Day10A("input/test_10")
  val day10ATestResult = day10ATest.solve()
  println("Test result for Day10A: $day10ATestResult")
  assert(day10ATestResult == 36)

  val day10A = Day10A("input/input_10")
  val durationA = measureTimeMillis {
    val solution = day10A.solve()
    println("Solution for Day10A: $solution")
    assert(solution == 744)
  }
  println("Solution took $durationA milliseconds")

  val day10BTest = Day10B("input/test_10")
  val day10BTestResult = day10BTest.solve()
  println("Test result for Day10B: $day10BTestResult")
  assert(day10BTestResult == 81)

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
    val board = parseBoard(input)
    val startingPositions = board.findPositions(setOf('0'))

    val trailheads = startingPositions.map { startingPosition ->

      val queue = mutableListOf(startingPosition)
      val peaksReached = mutableSetOf<Pos>()

      while (queue.isNotEmpty()) {
        val lastPos = queue.removeFirst()
        for (dir in Direction.entries) {
          val lookAheadPos = lastPos + dir
          if (lookAheadPos in board && board[lookAheadPos] == board[lastPos] + 1) {
            if (board[lookAheadPos] == '9') {
              peaksReached.add(lookAheadPos)
            } else {
              queue.add(lookAheadPos)
            }
          }
        }
      }
      peaksReached.size
    }
    return trailheads.sum()
  }
}

data class Day10B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val startingPositions = board.findPositions(setOf('0'))

    val trailheads = startingPositions.map { startingPosition ->

      val queue = mutableListOf(startingPosition)
      val peaksReached = mutableListOf<Pos>()

      while (queue.isNotEmpty()) {
        val lastPos = queue.removeFirst()
        for (dir in Direction.entries) {
          val lookAheadPos = lastPos + dir
          if (lookAheadPos in board && board[lookAheadPos] == board[lastPos] + 1) {
            if (board[lookAheadPos] == '9') {
              peaksReached.add(lookAheadPos)
            } else {
              queue.add(lookAheadPos)
            }
          }
        }
      }
      peaksReached.size
    }
    return trailheads.sum()
  }
}