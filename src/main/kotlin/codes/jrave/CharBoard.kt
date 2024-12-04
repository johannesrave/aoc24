package codes.jrave

val nullChar: Char = '\u0000'

fun parseBoard(input: String): Array<CharArray> =
  input.split("\n").map { line -> line.toCharArray() }.toTypedArray()

fun emptyBoard(dim: Int, nullChar: Char? = null): Array<CharArray> =
  emptyBoard(dim, dim, nullChar)

fun emptyBoard(rows: Int, cols: Int, nullChar: Char? = null): Array<CharArray> =
  Array(rows) { nullChar?.let { CharArray(cols).also { it.fill(nullChar) } } ?: CharArray(cols) }

fun Array<CharArray>.transpose(): Array<CharArray> =
  this.first().mapIndexed { x, _ -> this.map { it[x] }.toCharArray() }.toTypedArray()

fun Array<CharArray>.mirrorX(): Array<CharArray> =
  this.map { row -> row.reversedArray() }.toTypedArray()

fun Array<CharArray>.rotate45Degrees(): Array<CharArray> {
  val xDim = first().lastIndex
  val yDim = lastIndex
  val dimensionOfDiagonalBoard = xDim + yDim + 2
  val diagonalBoard = emptyBoard(dimensionOfDiagonalBoard)

  for (y in 0..yDim) {
    for (x in 0..xDim) {
      val (newX, newY) = (x + y) to (yDim + y - x + 1)

      diagonalBoard[newY][newX] = this[y][x]
    }
  }
  return diagonalBoard
}

fun Array<CharArray>.joinEach(filterChars: Set<Char> = setOf(nullChar)): List<String> =
  this.map { row -> row.filter { it !in filterChars }.joinToString("") }

fun Array<CharArray>.shrinkWrap(): Array<CharArray> {
  val shrinkingArray = this

  return shrinkingArray
    .dropWhile { row -> row.all { it == nullChar } }
    .dropLastWhile { row -> row.all { it == nullChar } }
    .toTypedArray().transpose()
    .dropWhile { row -> row.all { it == nullChar } }
    .dropLastWhile { row -> row.all { it == nullChar } }
    .toTypedArray()
}

fun main() {
  val testBoard = emptyBoard(3)
  testBoard[1][1] = 'A'

  println(testBoard.joinToString("\n") { it.joinToString("").replace(nullChar, '_') })

  println("shrinkwrapped:")
  println(testBoard.shrinkWrap().joinToString("\n") { it.joinToString("").replace(nullChar, '_') })

  println("original board unchanged:")
  println(testBoard.joinToString("\n") { it.joinToString("").replace(nullChar, '_') })
}