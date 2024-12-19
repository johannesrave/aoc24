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

    val start = board.findFirstPosition('S')
    val paths = findPaths(board.deepClone(), Step(start, E))

    println("Path with lowest score:")
    paths.minBy { path -> scorePathByStepsAndTurns(path) }
      .let { path -> board.markSteps(path) }
      .toPrintString().also { println(it) }


    return paths.minOf { path -> scorePathByStepsAndTurns(path) }
  }


  private fun findPaths(
    board: Array<CharArray>,
    initialStep: Step,
    wall: Char = '#',
    end: Char = 'E'
  ): MutableList<List<Step>> {
    val pathsQueue = mutableListOf(listOf(initialStep))
    val pathsFinished = mutableListOf<List<Step>>()
    val minimalStepsBoard = Array(board.size) { IntArray(board.first().size) { Int.MAX_VALUE } }

    while (pathsQueue.isNotEmpty()) {
      val path = pathsQueue.removeFirst()
      val step = path.last()
      for (dir in Direction.entries.filter { it != step.dir.flip() }) {
        val nextStep = Step(step.pos + dir, dir)
        when {
          board[nextStep.pos] == end -> {
            println("Finished Path:")
            board.markSteps(path).toPrintString().also { println(it) }
            pathsFinished += path
          }

          board[nextStep.pos] == wall -> continue
          scorePathByStepsAndTurns(path) >= minimalStepsBoard[nextStep.pos] -> continue
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
}

data class Day16B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}