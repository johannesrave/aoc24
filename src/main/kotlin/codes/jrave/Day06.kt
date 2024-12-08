package codes.jrave

import codes.jrave.Direction.Companion.guardMarkers
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day06ATest = Day06A("input/test_06")
  val day06ATestResult = day06ATest.solve().also { println(it) }
  assert(day06ATestResult == 41)

  val day06A = Day06A("input/input_06")
  val durationA = measureTimeMillis {
    val solution = day06A.solve()
    println("Solution for Day06A: $solution")
    assert(solution == 4665)
  }
  println("Solution took $durationA milliseconds")

  val day06BTest = Day06B("input/test_06")
  val day06BTestResult = day06BTest.solve()
  println("Test result for Day06B: $day06BTestResult")
  assert(day06BTestResult == 6)
  val day06B = Day06B("input/input_06")
  val duration06B = measureTimeMillis {
    val solution = day06B.solve()
    println("Solution for Day06B: $solution")
    assert(solution > 1511)
    assert(solution > 1660)
    assert(solution == 1660)
  }
  println("Solution took $duration06B milliseconds")
}


data class Day06A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val guardPosition = board.findPosition(guardMarkers)
    val guardMarker = board[guardPosition]
    val direction = Direction.from(guardMarker)

    val positions = walkPath(guardPosition, direction, board)

    return positions.size
  }

  private fun walkPath(
    guardPosition: Pos, direction: Direction, board: Array<CharArray>, block: Char = '#'
  ): MutableSet<Pos> {
    var guardPosition1 = guardPosition
    var guardDirection1 = direction
    val positions = mutableSetOf(guardPosition1)

    while (true) {
      val lookAheadPos = guardPosition1 + guardDirection1
      guardDirection1 = when {
        !(board contains lookAheadPos) -> break
        (board[lookAheadPos] == block) -> guardDirection1.turnClockwise()
        else -> guardDirection1
      }
      guardPosition1 += guardDirection1
      positions += guardPosition1
    }
    return positions
  }

  private operator fun Pos.plus(direction: Direction): Pos =
    Pos(y + direction.y, x + direction.x)

}

data class Day06B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val blockChar = '#'

    val guardPosition = board.findPosition(guardMarkers)
    val guardMarker = board[guardPosition]
    val direction = Direction.from(guardMarker)

    val (path, _) = walkPath(board, guardPosition, direction, blockChar)

    println(board.markSteps(path).toPrintString())

    return path.dropLast(1).filter { step ->
      val blockedBoard = board.deepClone().apply { set(step.pos + step.dir, blockChar) }
      val (path_, isCyclical) = walkPath(blockedBoard, step.pos, step.dir)
//      println()
//      println(board.markSteps(path_).toPrintString())
      (isCyclical)
    }.size
  }

  fun Array<CharArray>.markSteps(steps: MutableList<Step>): Array<CharArray> {
    val board_ = deepClone()
    steps.forEach { step -> board_[step.pos] = step.dir.c }
    board_[steps.first().pos] = 'O'
    return board_
  }


  private fun walkPath(
    board: Array<CharArray>,
    pos: Pos,
    dir: Direction,
    block: Char = '#'
  ): Pair<MutableList<Step>, Boolean> {
    var pos_ = pos
    var dir_ = dir
    val steps = mutableListOf<Step>()

    while (true) {
      val lookAheadPos = pos_ + dir_
      dir_ = when {
        !(board contains lookAheadPos) -> {
          steps += Step(pos_, dir_)
          return steps to false
        }

        (board[lookAheadPos] == block) -> dir_.turnClockwise()
        else -> dir_
      }
      val step = Step(pos_, dir_)
      if (step in steps) {
        steps += step
        return steps to true
      }
      steps += step
      pos_ += dir_
    }
  }

  private operator fun Pos.plus(direction: Direction): Pos =
    Pos(y + direction.y, x + direction.x)
}

data class Step(val pos: Pos, val dir: Direction)

enum class Direction(val x: Int, val y: Int, val c: Char) {
  N(0, -1, '^'), E(1, 0, '>'), S(0, 1, 'v'), W(-1, 0, '<'), ;

  fun turnClockwise() = turnBy(1)

  private fun turnBy(n: Int) = entries[(this.ordinal + n) % entries.size]

  companion object {
    val guardMarkers = entries.map { it.c }.toSet()
    fun from(c: Char): Direction = entries.find { it.c == c }!!
  }
}