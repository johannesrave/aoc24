package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day04ATest = Day04A("input/test_04")
  val day04ATestResult = day04ATest.solve().also { println(it) }
  assert(day04ATestResult == 18)

  val day04A = Day04A("input/input_04")
  val durationA = measureTimeMillis {
    val solution = day04A.solve()
    assert(solution == 2517)
    println("Solution for Day04A: $solution")
  }
  println("Solution took $durationA milliseconds")

  val day04BTest = Day04B("input/test_04")
  assert(
    day04BTest.solve(
      """
    XMXM
    XXAX
    XSXS
    XXXX
  """.trimIndent()
    ) == 1
  )

  val day04BTestResult = day04BTest.solve().also { println(it) }
  assert(day04BTestResult == 9)

  val day04B = Day04B("input/input_04")
  val duration04B = measureTimeMillis {
    val solution = day04B.solve()
    assert(solution == 1960)
    println("Solution for Day04B: $solution")
  }
  println("Solution took $duration04B milliseconds")
}


data class Day04A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val diagonalBoard = board.rotate45Degrees()

    println(board.joinEach().joinToString("\n"))
    println(diagonalBoard.joinEach().joinToString("\n"))

    val xmasPattern = Regex("(?=XMAS)|(?=SAMX)")
    val boardAtAngles = listOf(
      board.joinEach(),
      board.transpose().joinEach(),
      diagonalBoard.joinEach(),
      diagonalBoard.transpose().joinEach()
    )
    return boardAtAngles.sumOf { boardAtAngle ->
      boardAtAngle.sumOf { rowString -> xmasPattern.findAll(rowString).count() }
    }
  }
}


data class Day04B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  // the idea is to rotate the board by 45 degrees (filling the resulting spaces with a controlled character),
  // then match all the As in all "criss" strokes of MAS (and SAM). afterwards, i transpose the board
  // and match the As in the "cross" strokes.
  // after transposing the cross stroke coordinates, the matches that appear in both sets
  // are the coordinates of the As that have both a criss and a cross stroke.
  // their number is the solution.
  // this currently only works for square boards (and propbably forever)

  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    // this char is inserted into the board when it is rotated
    // and must be considered when matching later on
    val fillerChar = '_'
    val crissBoard = board.rotate45Degrees(fillerChar).shrinkWrap(fillerChar)
    val crossBoard = crissBoard.transpose()

    val xmasPattern = Regex("(?=M${fillerChar}(A)${fillerChar}S)|(?=S${fillerChar}(A)${fillerChar}M)")

    val crissMatches = crissBoard.getMatchCoordinates(xmasPattern).toSet()
    // need to flip the crossMatches due to transposition
    val crossMatches = crossBoard.getMatchCoordinates(xmasPattern).map { (x, y) -> y to x }.toSet()

    val crissCrossMatches = (crissMatches intersect crossMatches)
    return crissCrossMatches.size
  }

  private fun Array<CharArray>.getMatchCoordinates(pattern: Regex) =
    map { row -> row.joinToString("") }
      .flatMapIndexed { y, rowString ->
        pattern
          .findAll(rowString)
          // only keep the results with matches
          .filter { result -> result.groups.isNotEmpty() }
          // the results for the capturing group with the (A) contain its range
          // the range has a length of 1 (for one char) and is one of the coordinates,
          // the other is the row number
          .map { (it.groups[1] ?: it.groups[2]!!).range.first }
          .map { x -> y to x }
      }
}