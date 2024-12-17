package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
//  val day13ATest = Day13A("input/test_13")
//  val day13ATestResult = day13ATest.solve()
//  println("Test result for Day13A: $day13ATestResult")
//  assert(day13ATestResult == 480.toDouble())
//
//  val day13A = Day13A("input/input_13")
//  val durationA = measureTimeMillis {
//    val solution = day13A.solve()
//    println("Solution for Day13A: $solution")
//    assert(solution == 36571.toDouble())
//  }
//  println("Solution took $durationA milliseconds")

  val day13BTest = Day13B("input/test_13")
  val day13BTestResult = day13BTest.solve()
  println("Test result for Day13B: ${day13BTestResult.toLong()}")
  assert(day13BTestResult == 875318608908.toDouble())

//  val day13B = Day13B("input/input_13")
//  val duration13B = measureTimeMillis {
//    val solution = day13B.solve()
//    println("Solution for Day13B: ${solution.toLong()}")
//    assert(solution > 75171399383205.toDouble())
//  }
//  println("Solution took $duration13B milliseconds")
}

data class Day13A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Double {
    val xyPattern = Regex("""X.(?<x>\d{2,6}).*Y.(?<y>\d{2,6})""")
    val games = parseGames(input, xyPattern)

//    games.forEach(::println)

    return games.mapNotNull { game -> game.searchOptimalSolution() }.sum()
  }
}

data class Day13B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Double {
    val xyPattern = Regex("""X.(?<x>\d{2,6}).*Y.(?<y>\d{2,6})""")
    val games =
      parseGames(input, xyPattern).map { game: Game ->
        val prize = game.prize
        game.copy(prize = prize.copy(x = prize.x + 10000000000000, y = prize.y + 10000000000000))
      }

    return games.mapNotNull { game -> game.searchOptimalSolution() }.sum()
  }
}

private fun parseGames(input: String, xyPattern: Regex) = input.split("\n\n")
  .map { block ->
    val lines = block.split("\n")
    val buttonA = lines[0]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonA(it["x"]!!.value.toDouble(), it["y"]!!.value.toDouble()) }
    val buttonB = lines[1]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonB(it["x"]!!.value.toDouble(), it["y"]!!.value.toDouble()) }
    val prize = lines[2]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { Prize(it["x"]!!.value.toDouble(), it["y"]!!.value.toDouble()) }

    Game(prize, buttonA, buttonB)
  }

private sealed class Button(open val x: Double, open val y: Double, open val cost: Int) {
  fun push(position: Position, times: Int = 1): Position =
    Position(position.x + (times * x), position.y + (times * y))
}

private data class ButtonA(override val x: Double, override val y: Double) : Button(x, y, 3)
private data class ButtonB(override val x: Double, override val y: Double) : Button(x, y, 1)

private data class Prize(val x: Double, val y: Double)

private data class Position(val x: Double, val y: Double)

private data class Game(val prize: Prize, val buttonA: ButtonA, val buttonB: ButtonB) {
  fun searchOptimalSolution(): Double? {

    val origin = Point(0.0, 0.0)
    val aPointVector = Point(buttonA.x, buttonA.y)

    val target = Point(prize.x, prize.y)
    val bPointVector = Point(target.x + buttonB.x, target.y + buttonB.y)

    val aLine = Line(origin, aPointVector)
    val bLine = Line(target, bPointVector)

    val intersection = findDoubleersection(aLine, bLine)

    println("intersection: $intersection")

    println(intersection.x % buttonA.x)
    println(intersection.y % buttonA.y)
    println((prize.x - intersection.x) % buttonB.x)
    println((prize.y - intersection.y) % buttonB.y)

    if (
      intersection.x % buttonA.x == 0.0 &&
      intersection.y % buttonA.y == 0.0 &&
      (prize.x - intersection.x) % buttonB.x == 0.0 &&
      (prize.y - intersection.y) % buttonB.y == 0.0
    ) {
      val aPushes = intersection.x / buttonA.x
      val bPushes = (prize.x - intersection.x) / buttonB.x

      val tokens = (bPushes * buttonB.cost) + (aPushes * buttonA.cost)

      println("$bPushes bPushes and $aPushes aPushes needed for $tokens to reach \n$this")
      return tokens
    } else {

      println("Couldn't find solution for: \n$this")
      return null
    }
  }
}


// line intersection code taken from https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Kotlin

data class Point(val x: Double, val y: Double) {
  override fun toString() = "{$x, $y}"
}

data class Line(val A: Point, val B: Point)

fun findDoubleersection(line: Line, other: Line): Point {
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