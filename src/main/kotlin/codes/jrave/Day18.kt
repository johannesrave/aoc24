package codes.jrave

import java.io.File
import java.util.PriorityQueue
import kotlin.system.measureTimeMillis

fun main() {
  val day18ATest = Day18A("input/day18_test")
  val day18ATestResult = day18ATest.solve(boardDimension = 7, bytesDropped = 12)
  println("Test result for Day18A: $day18ATestResult")
  assert(day18ATestResult == 22)

  val day18A = Day18A("input/day18_input")
  val durationA = measureTimeMillis {
    val solution = day18A.solve(boardDimension = 71, bytesDropped = 1024)
    println("Solution for Day18A: $solution")
    assert(solution == 294)
  }
  println("Solution took $durationA milliseconds")

  val day18BTest = Day18B("input/day18_test")
  val day18BTestResult = day18BTest.solve(boardDimension = 7, bytesDropped = 12)
  println("Test result for Day18B: $day18BTestResult")
  assert(day18BTestResult == "6,1")

  val day18B = Day18B("input/day18_input")
  val duration18B = measureTimeMillis {
    val solution = day18B.solve(boardDimension = 71, bytesDropped = 1024)
    println("Solution for Day18B: $solution")
    assert(solution == "31,22")
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

    board.markPositions(corruptions, char = '#').toPrintString().also { println(it) }

    val startPos = Pos(0, 0)
    val endPos = Pos(boardDimension - 1, boardDimension - 1)

    val minBoard = minimalDistanceBoard(board.markPositions(corruptions, char = '#'), startPos, endPos)
    return minBoard[endPos]
  }
}

data class Day18B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, boardDimension: Int, bytesDropped: Int): String {
    var board = emptyBoard(boardDimension, '.')
    val corruptions = input.lines()
      .map { line -> line.split(",") }
      .map { (x, y) -> Pos(y.toInt(), x.toInt()) }

    val initialCorruptions = corruptions.take(bytesDropped)
    val droppingCorruptions = corruptions.drop(bytesDropped).toMutableList()

    board.markPositions(initialCorruptions, char = '#').toPrintString()
      .also { println(it) }

    val startPos = Pos(0, 0)
    val endPos = Pos(boardDimension - 1, boardDimension - 1)

    var minBoard =
      minimalDistanceBoard(board.markPositions(initialCorruptions, char = '#'), startPos, endPos)

    var nextCorruption = droppingCorruptions[0] // just to avoid typing as nullable

    while (minBoard[endPos] != 999_999) {
      nextCorruption = droppingCorruptions.removeFirst()
      board = board.markPositions(listOf(nextCorruption), char = '#')
      minBoard =
        minimalDistanceBoard(board.markPositions(initialCorruptions, char = '#'), startPos, endPos)
    }

    return "${nextCorruption.x},${nextCorruption.y}"
  }
}


private fun minimalDistanceBoard(
  board: Array<CharArray>,
  startPos: Pos,
  endPos: Pos,
  wall: Char = '#'
): Array<IntArray> {
  // this could be initialized to Int.MAX_VALUE instead,
  // I'm just using six digits for more readable prints
  val minimalCostBoard = Array(board.size) { IntArray(board.first().size) { 999_999 } }
  minimalCostBoard[startPos] = 0

  val queue = PriorityQueue { posA: Pos, posB: Pos ->
    posA.manhattanDistance(endPos) - posB.manhattanDistance(endPos)
  }

  queue.add(startPos)
  while (queue.isNotEmpty()) {
    val lastPos = queue.remove()
    val cost = minimalCostBoard[lastPos] + 1
    for (dir in Direction.entries) {
      val lookAheadPos = lastPos + dir
      when {
        lookAheadPos !in board -> continue
        board[lookAheadPos] == wall -> continue
        cost >= minimalCostBoard[lookAheadPos] -> continue
        else -> {
          minimalCostBoard[lookAheadPos] = cost
          if (lookAheadPos != endPos) queue += lookAheadPos;
        }
      }
    }
  }
  return minimalCostBoard
}