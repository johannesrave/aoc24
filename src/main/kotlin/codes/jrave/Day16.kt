package codes.jrave

import codes.jrave.Direction.E
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

  val day16A = Day16A("input/day16_input")
  val durationA = measureTimeMillis {
    val solution = day16A.solve()
    println("Solution for Day16A: $solution")
    assert(solution == 90460)
  }
  println("Solution took $durationA milliseconds")

  val day16BTest0 = Day16B("input/day16_test_0")
  val day16BTest0Result = day16BTest0.solve()
  println("Test result for Day16B: $day16BTest0Result")
  assert(day16BTest0Result == 45)

  val day16BTest1 = Day16B("input/day16_test_1")
  val day16BTest1Result = day16BTest1.solve()
  println("Test result for Day16B: $day16BTest1Result")
  assert(day16BTest1Result == 64)


//  val day16BTest2 = Day16B("input/day16_test_2")
//  val day16BTest2Result = day16BTest2.solve()
//  println("Test result for Day16B: $day16BTest2Result")
//  assert(day16BTest2Result == 64)

  val day16B = Day16B("input/day16_input")
  val duration16B = measureTimeMillis {
    val solution = day16B.solve()
    println("Solution for Day16B: $solution")
    assert(solution > 456)
  }
  println("Solution took $duration16B milliseconds")
}


data class Day16A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val paths = findPaths(board)

    println("Path with lowest score:")
    paths.minBy { path -> scorePathByStepsAndTurns(path) }
      .let { path -> board.markSteps(path) }
      .toPrintString().also { println(it) }

    return paths.minOf { path -> scorePathByStepsAndTurns(path) }
  }

}

data class Day16B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val paths = findPaths(board)

    val scoresToPaths = paths.map { path -> scorePathByStepsAndTurns(path) to path }
    val winningScore = scoresToPaths.minOf { (score, path) -> score }
    val winningPaths = scoresToPaths.filter { (score, path) -> score == winningScore }

    val tilesOnWinningPaths = winningPaths.flatMap { (score, path) -> path.map { it.pos } }.toSet()

    board.markPositions(tilesOnWinningPaths).toPrintString().also { println(it) }

    return tilesOnWinningPaths.size + 1 // for the end tile
  }
}

private fun findPaths(
  board: Array<CharArray>,
  wall: Char = '#',
  start: Char = 'S',
  end: Char = 'E'
): MutableList<List<Step>> {
  val startPos = board.findFirstPosition(start)

  val pathsQueue = mutableListOf(listOf(Step(startPos, E)))
  val pathsFinished = mutableListOf<List<Step>>()
  val minimalStepsBoard = Array(board.size) { IntArray(board.first().size) { Int.MAX_VALUE } }

  while (pathsQueue.isNotEmpty()) {
    val path = pathsQueue.removeFirst()
    val step = path.last()
    for (dir in Direction.entries.filter { it != step.dir.flip() }) {
      val nextStep = Step(step.pos + dir, dir)
      when {
        board[nextStep.pos] == end -> pathsFinished += path
        board[nextStep.pos] == wall -> continue
        path.any { it.pos == nextStep.pos } -> continue
        scorePathByStepsAndTurns(path) - 1000 >= minimalStepsBoard[nextStep.pos] -> continue
        else -> {
          minimalStepsBoard[nextStep.pos] = scorePathByStepsAndTurns(path)
          pathsQueue += (path + nextStep)
        }
      }
    }
  }
  return pathsFinished
}

private fun scorePathByStepsAndTurns(path: List<Step>) = path.indices.map { i ->
  if (i != path.lastIndex && (path[i].dir != path[i + 1].dir)) 1001 else 1
}.sum()