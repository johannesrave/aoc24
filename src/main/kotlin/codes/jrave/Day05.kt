package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day05ATest = Day05A("input/test_05")
  val day05ATestResult = day05ATest.solve().also { println(it) }
  assert(day05ATestResult == 143)

  val day05A = Day05A("input/input_05")
  val durationA = measureTimeMillis {
    val solution = day05A.solve()
//    assert(solution == 2517)
    println("Solution for Day05A: $solution")
  }
  println("Solution took $durationA milliseconds")

//  val day05B = Day05B("input/input_05")
//  val duration05B = measureTimeMillis {
//    val solution = day05B.solve()
//    assert(solution == 1960)
//    println("Solution for Day05B: $solution")
//  }
//  println("Solution took $duration05B milliseconds")
}


data class Day05A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val (rules, pages) = input.split("\n\n").map { it.split("\n") }

    val rulePatterns =
      rules.map { rule ->
        val (first, second) = rule.split("|")
        first to second
      }

    return pages
      .filter { page ->
        rulePatterns.all { (first, second) ->
          if (first in page && second in page) {
            Regex("$first.*$second").containsMatchIn(page)
          } else true
        }
      }
      .map {
        val numbers = it.split(",")
        val middleIndex = numbers.count() / 2
        numbers[middleIndex]
      }.sumOf { it.toInt() }
  }
}


data class Day05B(
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

    val xmasPattern =
      Regex("(?=M${fillerChar}(A)${fillerChar}S)|(?=S${fillerChar}(A)${fillerChar}M)")

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