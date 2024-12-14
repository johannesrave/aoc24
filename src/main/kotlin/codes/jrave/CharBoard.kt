package codes.jrave

val NULL_CHAR: Char = '\u0000'

fun parseBoard(input: String): Array<CharArray> =
  input.split("\n").map { line -> line.toCharArray() }.toTypedArray()

fun emptyBoard(dim: Int, nullChar: Char? = null): Array<CharArray> =
  emptyBoard(dim, dim, nullChar)

fun emptyBoard(rows: Int, cols: Int, nullChar: Char? = null): Array<CharArray> =
  Array(rows) { nullChar?.let { CharArray(cols).also { it.fill(nullChar) } } ?: CharArray(cols) }

fun Array<CharArray>.toPrintString(): String = joinEach().joinToString("\n")

fun Array<CharArray>.transpose(): Array<CharArray> =
  this.first().mapIndexed { x, _ -> this.map { it[x] }.toCharArray() }.toTypedArray()

fun Array<CharArray>.mirrorX(): Array<CharArray> =
  this.map { row -> row.reversedArray() }.toTypedArray()

fun Array<CharArray>.rotate45Degrees(nullChar: Char? = null): Array<CharArray> {
  val xDim = first().lastIndex
  val yDim = lastIndex
  val dimensionOfDiagonalBoard = xDim + yDim + 2
  val diagonalBoard = emptyBoard(dimensionOfDiagonalBoard, nullChar)

  for (y in 0..yDim) {
    for (x in 0..xDim) {
      val (newX, newY) = (x + y) to (yDim + y - x + 1)

      diagonalBoard[newY][newX] = this[y][x]
    }
  }
  return diagonalBoard
}

fun Array<CharArray>.joinEach(filterChars: Set<Char> = setOf(NULL_CHAR)): List<String> =
  this.map { row -> row.filter { it !in filterChars }.joinToString("") }

fun Array<CharArray>.shrinkWrap(nullChar: Char? = NULL_CHAR): Array<CharArray> = this
  .transpose()
  .dropWhile { row -> row.all { it == nullChar } }
  .dropLastWhile { row -> row.all { it == nullChar } }
  .toTypedArray()
  .transpose()
  .dropWhile { row -> row.all { it == nullChar } }
  .dropLastWhile { row -> row.all { it == nullChar } }
  .toTypedArray()

fun Array<CharArray>.findPosition(chars: Set<Char>): Pos {
  return mapIndexedNotNull { y, row ->
    if (chars.any { it in row }) {
      val x = row.indexOfFirst { it in chars }
      Pos(y, x)
    } else null
  }.first()
}

operator fun Array<CharArray>.get(pos: Pos): Char = this[pos.y][pos.x]
operator fun Array<CharArray>.set(pos: Pos, char: Char) {
  this[pos.y][pos.x] = char
}

infix operator fun Array<out CharArray>.contains(pos: Pos): Boolean =
  (pos.y in this.indices && pos.x in this.first().indices)


fun Array<CharArray>.deepClone(): Array<CharArray> = Array(size) { get(it).clone() }


fun Array<CharArray>.markPositions(positions: List<Pos>, char: Char = 'O'): Array<CharArray> =
  deepClone().also { board -> positions.forEach { pos -> board[pos] = char } }


fun Array<CharArray>.markSteps(steps: List<Step>, overrideChar: Char? = null): Array<CharArray> {
  val board_ = deepClone()
  steps.forEach { step -> board_[step.pos] = overrideChar ?: step.dir.c }
  board_[steps.first().pos] = 'X'
  return board_
}

data class Pos(val y: Int, val x: Int) {
  operator fun plus(direction: Direction): Pos = Pos(y + direction.y, x + direction.x)
  operator fun minus(other: Pos): Pos = Pos(y - other.y, x - other.x)
}

enum class Direction(val x: Int, val y: Int, val c: Char) {
  N(0, -1, '^'), E(1, 0, '>'), S(0, 1, 'v'), W(-1, 0, '<'), ;

  fun turnClockwise() = turnBy(1)

  private fun turnBy(n: Int) = entries[(this.ordinal + n) % entries.size]

  companion object {
    val directionMarkers = entries.map { it.c }.toSet()
    fun from(c: Char): Direction = entries.find { it.c == c }!!
  }
}

data class Step(val pos: Pos, val dir: Direction)


fun main() {
  val testBoard = emptyBoard(3)
  testBoard[1][1] = 'A'

  println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

  println("shrinkwrapped:")
  println(testBoard.shrinkWrap().joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

  println("original board unchanged:")
  println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })
}