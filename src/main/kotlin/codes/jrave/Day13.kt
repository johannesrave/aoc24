package codes.jrave

import java.io.File
import java.math.BigInteger
import java.math.BigInteger.ZERO
import kotlin.system.measureTimeMillis

fun main() {
  val day13ATest = Day13A("input/day13_test")
  val day13ATestResult = day13ATest.solve()
  println("Test result for Day13A: $day13ATestResult")
  assert(day13ATestResult == 480.toBigInteger())

  val day13A = Day13A("input/day13_input")
  val durationA = measureTimeMillis {
    val solution = day13A.solve()
    println("Solution for Day13A: $solution")
    assert(solution == 36571.toBigInteger())
  }
  println("Solution took $durationA milliseconds")

  val day13BTest = Day13B("input/day13_test")
  val day13BTestResult = day13BTest.solve()
  println("Test result for Day13B: $day13BTestResult")
  assert(day13BTestResult == 875318608908.toBigInteger())

  val day13B = Day13B("input/day13_input")
  val duration13B = measureTimeMillis {
    val solution = day13B.solve()
    println("Solution for Day13B: ${solution.toLong()}")
    assert(solution > 75171399383205.toBigInteger())
  }
  println("Solution took $duration13B milliseconds")
}

data class Day13A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val xyPattern = Regex("""X.(?<x>\d{2,6}).*Y.(?<y>\d{2,6})""")
    val games = parseGames(input, xyPattern)

    return games
      .mapNotNull { game -> game.searchOptimalSolution(maxPushes = 100.toBigInteger()) }.sum()
  }
}

data class Day13B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val xyPattern = Regex("""X.(?<x>\d{2,6}).*Y.(?<y>\d{2,6})""")
    val correction = 10000000000000.toBigInteger()
    val games = parseGames(input, xyPattern)
      .map { game: Game ->
        val prize = game.prize
        game.copy(prize = prize.copy(x = prize.x + correction, y = prize.y + correction))
      }

    return games.mapNotNull { game -> game.searchOptimalSolution() }.sum()
  }
}

private fun parseGames(input: String, xyPattern: Regex) = input.split("\n\n")
  .map { block ->
    val lines = block.split("\n")
    val buttonA = lines[0]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonA(it["x"]!!.value.toBigInteger(), it["y"]!!.value.toBigInteger()) }
    val buttonB = lines[1]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonB(it["x"]!!.value.toBigInteger(), it["y"]!!.value.toBigInteger()) }
    val prize = lines[2]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { Prize(it["x"]!!.value.toBigInteger(), it["y"]!!.value.toBigInteger()) }

    Game(prize, buttonA, buttonB)
  }

private sealed class Button(open val x: BigInteger, open val y: BigInteger, open val cost: Int)

private data class ButtonA(override val x: BigInteger, override val y: BigInteger) : Button(x, y, 3)
private data class ButtonB(override val x: BigInteger, override val y: BigInteger) : Button(x, y, 1)

private data class Prize(val x: BigInteger, val y: BigInteger)

private data class Game(val prize: Prize, val buttonA: ButtonA, val buttonB: ButtonB) {
  fun searchOptimalSolution(maxPushes: BigInteger? = null): BigInteger? {
    if (maxPushes != null &&
      (prize.x > maxPushes * (buttonA.x + buttonB.x) || prize.y > maxPushes * (buttonA.y + buttonB.y))
    ) {
      println()
      println("can't even reach \n$this")
      return null
    }

    val origin = Point(ZERO, ZERO)
    val aPointVector = Point(buttonA.x, buttonA.y)

    val target = Point(prize.x, prize.y)
    val bPointVector = Point(target.x + buttonB.x, target.y + buttonB.y)

    val aLine = Line(origin, aPointVector)
    val bLine = Line(target, bPointVector)

    val intersection = findIntersection(aLine, bLine)

    println("intersection: $intersection")

    if (
      intersection.x % buttonA.x == ZERO &&
      intersection.y % buttonA.y == ZERO &&
      (prize.x - intersection.x) % buttonB.x == ZERO &&
      (prize.y - intersection.y) % buttonB.y == ZERO
    ) {
      val aPushes = intersection.x / buttonA.x
      val bPushes = (prize.x - intersection.x) / buttonB.x

      val tokens = (bPushes * buttonB.cost.toBigInteger()) + (aPushes * buttonA.cost.toBigInteger())

      println("$bPushes bPushes and $aPushes aPushes needed for $tokens to reach \n$this")
      return tokens
    } else {

      println("Couldn't find solution for: \n$this")
      return null
    }
  }
}

// line intersection code taken from https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Kotlin
// i think some of this stuff is redundant for my purposes but it does work :shrug:

data class Point(val x: BigInteger, val y: BigInteger) {
  override fun toString() = "{$x, $y}"
}

data class Line(val A: Point, val B: Point)

fun findIntersection(line: Line, other: Line): Point {
  val a1 = line.B.y - line.A.y
  val b1 = line.A.x - line.B.x
  val c1 = a1 * line.A.x + b1 * line.A.y

  val a2 = other.B.y - other.A.y
  val b2 = other.A.x - other.B.x
  val c2 = a2 * other.A.x + b2 * other.A.y

  val delta = a1 * b2 - a2 * b1
  // If lines are parallel, intersection point will contain infinite values
  return Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
}