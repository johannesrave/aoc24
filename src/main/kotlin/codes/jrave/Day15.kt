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

    var robotPos = board.findFirstPosition('@')

    robotDirections.forEach { direction ->
      val lookAheadPos = robotPos + direction
      if (moveNeighbour(board, lookAheadPos, direction)) {
        board[lookAheadPos] = '@'
        board[robotPos] = '.'
        robotPos = lookAheadPos
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

  private fun moveNeighbour(board: Array<CharArray>, pos: Pos, dir: Direction): Boolean {
    if (pos !in board) return false
    val char = board[pos]
    if (char == '#') return false
    if (char == '.') return true


    val lookAheadPos = pos + dir
    val lookAheadTwoPos = lookAheadPos + dir
    val lookAheadPosR = lookAheadPos + E
    val lookAheadPosL = lookAheadPos + W

    val (canMove, secondPos) = when {
      dir.horizontal -> moveNeighbour(board, lookAheadTwoPos, dir) to lookAheadTwoPos
      char == '[' -> tryMoveCrate(board, listOf(lookAheadPos, lookAheadPosR), dir) to lookAheadPosR
      char == ']' -> tryMoveCrate(board, listOf(lookAheadPos, lookAheadPosL), dir) to lookAheadPosL

      else -> throw IllegalStateException("illegal character in board: $char at position $pos")
    }
    when {
      canMove && dir == E -> {
        board[lookAheadPos] = char
        board[lookAheadTwoPos] = ']'
        return true
      }

      canMove && dir == W -> {
        board[lookAheadPos] = char
        board[lookAheadTwoPos] = '['
        return true
      }

      canMove && dir.vertical -> {
        board[lookAheadPos] = char
        board[secondPos] = '.'
        return true
      }

      else -> return false
    }
  }
}

private fun tryMoveCrate(
  board: Array<CharArray>,
  positions: List<Pos>,
  dir: Direction
): Boolean {
  if (positions.any { it !in board }) return false;

  when {
    positions.any { board[it] == '#' } -> return false
    positions.all { board[it] == '.' } -> return true
  }
  val posL = positions[0]
  val posR = positions[1]

  val charL = board[posL]
  val charR = board[posR]

  when {
    charL == '[' && charR == ']' -> {
      if (tryMoveCrate(board, listOf(posL + dir, posR + dir), dir)) {
        board[posL + dir] = '['
        board[posR + dir] = ']'
        board[posL] = '.'
        board[posR] = '.'
        return true
      } else
        return false
    }

    else -> throw IllegalStateException("unclear situation in board at $posL and $posR. \n ${board.toPrintString()}")
  }

}

private fun tryMoveCrate(
  board: Array<CharArray>,
  crate: WideCrate,
  dir: Direction
): Boolean {
  val lookAheadPosLeft = crate.left + dir
  val lookAheadPosRight = crate.right + dir

  val lookAheadLeft = board[lookAheadPosLeft]
  val lookAheadRight = board[lookAheadPosRight]

  if (lookAheadLeft == '#' || lookAheadRight == '#') return false

  if (dir == W && board[crate.left + W] == '.') {
    board[crate.left + W] = '['; board[crate.left] = ']'; board[crate.right] = '.'
    return true
  }
  if (dir == E && board[crate.right + E] == '.') {
    board[crate.left] = '.'; board[crate.right] = '['; board[crate.right + E] = ']'
    return true
  }
  if (dir == W && board[crate.left + W] == ']' &&
    tryMoveCrate(board, WideCrate(crate.left + W + W, crate.left + W), dir)
  ) {
    board[crate.left + W] = '['; board[crate.left] = ']'; board[crate.right] = '.'
    return true
  }
  if (dir == E && lookAheadLeft == '[' &&
    tryMoveCrate(board, WideCrate(crate.right + E, crate.right + E + E), dir)
  ) {
    board[crate.left] = '.'; board[crate.right] = '['; board[crate.right + E] = ']'
    return true
  }

  if (dir.vertical && lookAheadLeft == '.' && lookAheadRight == '.') {
    board[crate.left] = '.'; board[crate.right] = '.'
    board[crate.left + dir] = '['; board[crate.right + dir] = ']'
    return true
  }

  if (dir.vertical && lookAheadLeft == '[' && lookAheadRight == ']' &&
    tryMoveCrate(board, WideCrate(crate.left + dir, crate.right + dir), dir)
  ) {
    board[crate.left] = '.'; board[crate.right] = '.'
    board[crate.left + dir] = '['; board[crate.right + dir] = ']'
    return true
  }

  if (dir.vertical && lookAheadLeft == ']' && lookAheadRight == '.' &&
    tryMoveCrate(board, WideCrate(crate.left + dir + W, crate.left + dir), dir)
  ) {
    board[crate.left] = '.'; board[crate.right] = '.'
    board[lookAheadPosLeft] = '['; board[lookAheadPosRight] = ']'
    return true
  }

  if (dir.vertical && lookAheadLeft == '.' && lookAheadRight == '[' &&
    tryMoveCrate(board, WideCrate(crate.right + dir, crate.right + dir + E), dir)
  ) {
    board[crate.left] = '.'; board[crate.right] = '.'
    board[lookAheadPosLeft] = '['; board[lookAheadPosRight] = ']'
    return true
  }

  if (dir.vertical && lookAheadLeft == ']' && lookAheadRight == '[' &&
    tryMoveCrate(board, WideCrate(crate.left + dir + W, crate.left + dir), dir) &&
    tryMoveCrate(board, WideCrate(crate.right + dir, crate.right + dir + E), dir)
  ) {
    TODO("this is wrong - one of the crates might move even though the other one can't. need a check-round before!")
    board[crate.left] = '.'; board[crate.right] = '.'
    board[lookAheadPosLeft] = '['; board[lookAheadPosRight] = ']'
    return true
  }

  return false
}

data class WideCrate(val left: Pos, val right: Pos)