package codes.jrave

import codes.jrave.Direction.E
import java.io.File
import java.util.PriorityQueue
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

  val day16A = Day16A("input/day16_input")
  val durationA = measureTimeMillis {
    val solution = day16A.solve()
    println("Solution for Day16A: $solution")
    assert(solution == 90460)
  }
  println("Solution took $durationA milliseconds")

//  val day16BTest0 = Day16B("input/day16_test_0")
//  val day16BTest0Result = day16BTest0.solve()
//  println("Test result for Day16B: $day16BTest0Result")
//  assert(day16BTest0Result == 45)
//
//  val day16BTest1 = Day16B("input/day16_test_1")
//  val day16BTest1Result = day16BTest1.solve()
//  println("Test result for Day16B: $day16BTest1Result")
//  assert(day16BTest1Result == 64)


//  val day16BTest2 = Day16B("input/day16_test_2")
//  val day16BTest2Result = day16BTest2.solve()
//  println("Test result for Day16B: $day16BTest2Result")
//  assert(day16BTest2Result == 64)

//  val day16B = Day16B("input/day16_input")
//  val duration16B = measureTimeMillis {
//    val solution = day16B.solve()
//    println("Solution for Day16B: $solution")
//    assert(solution > 546)
//  }
//  println("Solution took $duration16B milliseconds")
}


data class Day16A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val startMarker = 'S'
    val endMarker = 'E'

    val board = parseBoard(input)

    val startPos = board.findFirstPosition(startMarker)
    val endPos = board.findFirstPosition(endMarker)

    val minBoard = findShortestPaths(board, startPos, endPos)

    minBoard.forEach { row ->
      row.joinToString(" ") { it.toString().padStart(6) }.also { println(it) }
    }

    return minBoard[endPos]
  }
}

//data class Day16B(
//  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
//) {
//  fun solve(input: String = this.input): Int {
//    val board = parseBoard(input)
//
//    val pathsToBoard = findShortestPaths(board)
//
//    val (paths, markedBaoard) = pathsToBoard
//
//    println("Found paths:")
//    paths.map { path -> path.size to scorePathByStepsAndTurns(path) }.onEach { println(it) }
//
//    val scoresToPaths = paths.map { path -> scorePathByStepsAndTurns(path) to path }
//    val winningScore = scoresToPaths.minOf { (score, path) -> score }
//    val winningPaths = scoresToPaths.filter { (score, path) -> score == winningScore }
//
//    val tilesOnWinningPaths = winningPaths.flatMap { (score, path) -> path.map { it.pos } }.toSet()
//
//    board.markPositions(tilesOnWinningPaths).toPrintString().also { println(it) }
//
//    return tilesOnWinningPaths.size // for the end tile
//  }
//}

private fun findShortestPaths(
  board: Array<CharArray>,
  startPos: Pos,
  endPos: Pos,
  wall: Char = '#',
): Array<IntArray> {
  // this could be initialized to Int.MAX_VALUE instead,
  // I'm just using six digits for more readable prints
  val minimalCostBoard = Array(board.size) { IntArray(board.first().size) { 999_999 } }
  minimalCostBoard[startPos] = 0

  val stepsQueue = PriorityQueue { stepA: Step, stepB: Step ->
    ((endPos.x - stepA.pos.x) + (endPos.y - stepA.pos.y))
    -((endPos.x - stepB.pos.x) + (endPos.y - stepB.pos.y))
  }

  stepsQueue.add(Step(startPos, E))
  while (stepsQueue.isNotEmpty()) {
    val lastStep = stepsQueue.remove()
    for (dir in Direction.entries.filter { it != lastStep.dir.flip() }) {
      val addedCost = if (dir == lastStep.dir) 1 else 1001
      val cost = minimalCostBoard[lastStep.pos] + addedCost
      val nextStep = Step(lastStep.pos + dir, dir)
      when {
        board[nextStep.pos] == wall -> continue
        cost >= minimalCostBoard[nextStep.pos] -> continue
        else -> {
          minimalCostBoard[nextStep.pos] = cost
          if (nextStep.pos != endPos) stepsQueue += nextStep;
        }
      }
    }
  }
  return minimalCostBoard
}