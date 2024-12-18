package codes.jrave

import codes.jrave.Direction.*
import java.io.File

fun main() {
//  val day15ATest = Day15A("input/day15_test")
//  val day15ATestResult = day15ATest.solve()
//  println("Test result for Day15A: $day15ATestResult")
//  assert(day15ATestResult == 10092)
//
//  val day15A = Day15A("input/day15_input")
//  val durationA = measureTimeMillis {
//    val solution = day15A.solve()
//    println("Solution for Day15A: $solution")
//    assert(solution == 1)
//  }
//  println("Solution took $durationA milliseconds")

  val day15BTest = Day15B("input/day15_test")
  val day15BTestResult = day15BTest.solve()
  println("Test result for Day15B: $day15BTestResult")
  assert(day15BTestResult == 9021)

//  val day15B = Day15B("input/day15_input")
//  val duration15B = measureTimeMillis {
//    val solution = day15B.solve()
//    println("Solution for Day15B: $solution")
//    assert(solution == 1)
//  }
//  println("Solution took $duration15B milliseconds")
}


data class Day15A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val (boardString, directionsString) = input.split("\n\n")

    val board = parseBoard(boardString)
    val robotDirections = directionsString.mapNotNull { c -> Direction.from(c) }

    println(board.toPrintString())

    var robotPos = board.findFirstPosition('@')

    robotDirections.forEach { direction ->
      val lookAheadPos = robotPos + direction
      if (moveNeighbour(board, lookAheadPos, direction)) {
        board[lookAheadPos] = '@'
        board[robotPos] = '.'
        robotPos = lookAheadPos
      }
    }

    return board.findPositions('O').sumOf { pos -> pos.y * 100 + pos.x }
  }

  private fun moveNeighbour(board: Array<CharArray>, pos: Pos, direction: Direction): Boolean {
    if (pos !in board) return false
    return when (board[pos]) {
      '#' -> false
      '.' -> true
      'O' -> {
        val lookAheadPos = pos + direction
        if (moveNeighbour(board, lookAheadPos, direction)) {
          board[lookAheadPos] = board[pos]
          true
        } else
          false
      }

      else -> throw IllegalStateException("illegal character in board: ${board[pos]} at position $pos")
    }
  }
}

data class Day15B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val (boardString, directionsString) = input.split("\n\n")

    val board = parseBoard(boardString).widenBoard()
    val robotDirections = directionsString.mapNotNull { c -> Direction.from(c) }

    println(board.toPrintString())

    val robotPos = board.findFirstPosition('@')
    var robot = Robot(robotPos)

    robotDirections.forEach { dir ->
      val (canMove, crates) = robot.canMove(board, dir)
      if (canMove) {
        robot = robot.moveAndPush(board, dir, crates)
      }
      println(board.toPrintString())
    }

    return board.findPositions('O').sumOf { pos -> pos.y * 100 + pos.x }
  }

  private fun Array<CharArray>.widenBoard(): Array<CharArray> = map { row ->
    row.flatMap { c ->
      when (c) {
        '#' -> listOf('#', '#')
        '.' -> listOf('.', '.')
        'O' -> listOf('[', ']')
        '@' -> listOf('@', '.')
        else -> throw IllegalStateException("illegal character in board: $c")
      }
    }.toCharArray()
  }.toTypedArray()
}

data class Robot(val pos: Pos) {
  fun canMove(board: Array<CharArray>, dir: Direction): Pair<Boolean, List<WideCrate>> {
    if (board[pos + dir] == '#') return false to emptyList()
    if (board[pos + dir] == '.') return true to emptyList()
    val neighbouringCrate = getNeighbouringCrate(board, dir)
    val crates = neighbouringCrate.getCratesInDirection(board, dir) + neighbouringCrate
    return crates.all { it.canMove(board, dir) } to crates
  }

  private fun getNeighbouringCrate(board: Array<CharArray>, dir: Direction): WideCrate = when {
    (board[pos + dir] == '[') -> WideCrate(pos + dir, pos + dir + E)
    (board[pos + dir] == ']') -> WideCrate(pos + dir + W, pos + dir)
    else -> throw IllegalStateException("illegal character in board: ${board[pos + dir]} at position ${pos + dir} when moving $dir")
  }

  fun moveAndPush(board: Array<CharArray>, dir: Direction, crates: List<WideCrate>): Robot {
    crates.distinct().forEach { it.move(board, dir) }
    board[pos] = '.'
    board[pos + dir] = '@'
    return Robot(pos + dir)
  }
}

data class WideCrate(val left: Pos, val right: Pos) {
  fun canMove(board: Array<CharArray>, dir: Direction): Boolean =
    (board[left + dir] != '#' && board[right + dir] != '#')

  fun move(board: Array<CharArray>, direction: Direction) = when (direction) {
    W -> {
      board[left + W] = '['; board[left] = ']'; board[right] = '.'
    }

    E -> {
      board[left] = '.'; board[right] = '['; board[right + E] = ']'
    }

    N -> {
      board[left] = '.'; board[right] = '.'; board[left + N] = '['; board[right + N] = ']'
    }

    S -> {
      board[left] = '.'; board[right] = '.'; board[left + S] = '['; board[right + S] = ']'
    }
  }

  fun getCratesInDirection(board: Array<CharArray>, dir: Direction): List<WideCrate> {

    val crates = mutableListOf<WideCrate>()

    if (dir == W && board[left + W] == ']')
      crates += WideCrate(left + W + W, left + W)

    if (dir == E && board[right + E] == '[')
      crates += WideCrate(right + E, right + E + E)

    if (dir.vertical && board[left + dir] == ']')
      crates += WideCrate(left + dir + W, left + dir)

    if (dir.vertical && board[right + dir] == '[')
      crates += WideCrate(right + dir, right + dir + E)

    if (dir.vertical && board[left + dir] == '[' && board[right + dir] == ']')
      crates += WideCrate(left + dir, right + dir)

    return (crates.flatMap { it.getCratesInDirection(board, dir) } + crates)

  }
}