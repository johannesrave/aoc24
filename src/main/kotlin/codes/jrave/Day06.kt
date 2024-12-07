package codes.jrave

import codes.jrave.GuardDirection.Companion.guardMarkers
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

//  val day06BTest = Day06B("input/test_06")
//  val day06BTestResult = day06BTest.solve()
//  assert(day06BTestResult == 123)
//  val day06B = Day06B("input/input_06")
//  val duration06B = measureTimeMillis {
//    val solution = day06B.solve()
//    println("Solution for Day06B: $solution")
//    assert(solution == 4743)
//  }
//  println("Solution took $duration06B milliseconds")
}


data class Day06A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val guardPosition = findGuardPosition(board)
    val guardMarker = board[guardPosition]
    val guardDirection = GuardDirection.from(guardMarker)

    val positions = walkPath(guardPosition, guardDirection, board)

    return positions.size
  }

  private fun findGuardPosition(board: Array<CharArray>): Pos {
    val guardPosition = board.mapIndexedNotNull { y, row ->
      if (guardMarkers.any { it in row }) {
        val x = row.indexOfFirst { it in guardMarkers }
        Pos(y, x)
      } else null
    }.first()
    return guardPosition
  }

  private fun walkPath(
    guardPosition: Pos,
    guardDirection: GuardDirection,
    board: Array<CharArray>,
    block: Char = '#'
  ): MutableSet<Pos> {
    var guardPosition1 = guardPosition
    var guardDirection1 = guardDirection
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

  private operator fun Pos.plus(guardDirection: GuardDirection): Pos =
    Pos(y + guardDirection.y, x + guardDirection.x)

}


enum class GuardDirection(val x: Int, val y: Int, val c: Char) {
  N(0, -1, '^'),
  E(1, 0, '>'),
  S(0, 1, 'v'),
  W(-1, 0, '<'), ;


  fun turnClockwise() = turnBy(1)

  private fun turnBy(n: Int) = entries[(this.ordinal + n) % entries.size]

  companion object {
    val guardMarkers = entries.map { it.c }.toSet()
    fun from(c: Char): GuardDirection = entries.find { it.c == c }!!
  }
}


data class Day06B(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val (rules, pages) = input.split("\n\n").map { it.split("\n") }

    val rulePatterns = rules.map { rule ->
      val (first, second) = rule.split("|")
      first to second
    }

    val incorrectPages = pages.filterNot { page ->
      rulePatterns.all { (first, second) ->
        (first in page && second in page && isSortedCorrectly(page, first, second))
            || (first !in page || second !in page)
      }
    }

    return incorrectPages
      .map { it.split(",") }
      .map { numbers -> sortByRules(numbers, rulePatterns) }
      .map { numbers ->
        val middleIndex = numbers.count() / 2
        numbers[middleIndex]
      }.sumOf { it.toInt() }
  }

  fun sortByRules(page: List<String>, rulePatterns: List<Pair<String, String>>): List<String> {
    val rulePatterns_ = rulePatterns.filter { (a, b) -> a in page && b in page }
    val page_ = page.toMutableList()
    var runs = 0 // just out of interest
    do {
      for (pattern in rulePatterns_) {
        val (a, b) = pattern
        val aIndex = page_.indexOf(a)
        val bIndex = page_.indexOf(b)
        if (aIndex > bIndex) {
          page_.removeAt(aIndex)
          page_.add(bIndex, a)
        }
      }
      runs++
    } while (rulePatterns_.any { (a, b) -> !isSortedCorrectly(page_.joinToString(), a, b) })
    println("$runs runs for $page to $page_")
    return page_
  }

  fun isSortedCorrectly(page: String, first: String, second: String) =
    Regex("$first.*$second").containsMatchIn(page)
}