package codes.jrave

import codes.jrave.Direction.Companion.directionMarkers
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
    assert(solution == 1688)
  }
  println("Solution took $duration06B milliseconds")
}


data class Day06A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val guardPosition = board.findPosition(directionMarkers)
    val guardMarker = board[guardPosition]
    val direction = Direction.from(guardMarker)

    val positions = walkPath(guardPosition, direction, board)

    return positions.size
  }

  private fun walkPath(
    pos: Pos,
    direction: Direction,
    board: Array<CharArray>,
    block: Char = '#'
  ): MutableSet<Pos> {
    var pos_ = pos
    var direction_ = direction
    val positions = mutableSetOf(pos_)

    while (true) {
      val lookAheadPos = pos_ + direction_
      direction_ = when {
        !(board contains lookAheadPos) -> break
        (board[lookAheadPos] == block) -> direction_.turnClockwise()
        else -> direction_
      }
      pos_ += direction_
      positions += pos_
    }
    return positions
  }
}

data class Day06B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val blockMarker = '#'

    val initialPosition = board.findPosition(directionMarkers)
    val directionMarker = board[initialPosition]
    val initialDirection = Direction.from(directionMarker)

    // the idea is to walk the path first to find all candidate-tiles for placing obstructions
    val (path, _) = walkPath(board, initialPosition, initialDirection, blockMarker)

    val obstructionPositions = path
      // find the obstruction positions by looking ahead one tile from each position
      .asSequence()
      .map { step ->
        val blockPos = step.pos + step.dir
        step to blockPos
      }
      // deduplicate
      .distinctBy { (_, blockPos) -> blockPos }
      // exclude initial guard position
      .filter { (_, blockPos) -> (blockPos != initialPosition) }
      // exclude obstruction candidates that lie outside the board
      .filter { (_, blockPos) -> (blockPos in board) }
      // only keep positions that lead into a cyclical path
      .filter { (step, blockPos) ->
        val blockedBoard = board.deepClone().apply { set(blockPos, blockMarker) }
        val (_, isCyclical) = walkPath(blockedBoard, step.pos, step.dir)
        (isCyclical)
      }.map { (_, blockPos) -> blockPos }
      .toList()

    return obstructionPositions.size
  }

  private fun walkPath(
    board: Array<CharArray>,
    pos: Pos,
    dir: Direction,
    block: Char = '#'
  ): Pair<List<Step>, Boolean> {
    val steps = mutableListOf(Step(pos, dir))

    while (true) {
      var (pos, dir) = steps.last()
      val lookAheadPos = pos + dir

      if (!(board contains lookAheadPos)) return steps to false
      when {
        (board[lookAheadPos] == block) -> dir = dir.turnClockwise()
        else -> pos += dir
      }

      val step = Step(pos, dir)
      if (step in steps) {
        return steps to true
      }
      steps.addLast(step)
    }
  }
}