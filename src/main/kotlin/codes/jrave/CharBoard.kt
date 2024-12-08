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

data class Pos(val y: Int, val x: Int)


fun Array<CharArray>.deepClone(): Array<CharArray> = Array(size) { get(it).clone() }

fun main() {
  val testBoard = emptyBoard(3)
  testBoard[1][1] = 'A'

  println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

  println("shrinkwrapped:")
  println(testBoard.shrinkWrap().joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

  println("original board unchanged:")
  println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })
}