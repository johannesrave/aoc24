package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day13ATest = Day13A("input/test_13")
  val day13ATestResult = day13ATest.solve()
  println("Test result for Day13A: $day13ATestResult")
  assert(day13ATestResult == 480)

  val day13A = Day13A("input/input_13")
  val durationA = measureTimeMillis {
    val solution = day13A.solve()
    println("Solution for Day13A: $solution")
    assert(solution > 30747)
  }
  println("Solution took $durationA milliseconds")

  val day13BTest = Day13B("input/test_13")
  val day13BTestResult = day13BTest.solve()
  println("Test result for Day13B: $day13BTestResult")
  assert(day13BTestResult == 1)

  val day13B = Day13B("input/input_13")
  val duration13B = measureTimeMillis {
    val solution = day13B.solve()
    println("Solution for Day13B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration13B milliseconds")
}

data class Day13A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val xyPattern = Regex("""X.(?<x>\d{2,4}).*Y.(?<y>\d{2,4})""")
    val games = parseGames(input, xyPattern)

//    games.forEach(::println)

    return games.mapNotNull { game -> game.searchOptimalSolution() }.sum()
  }
}

private fun parseGames(input: String, xyPattern: Regex) = input.split("\n\n")
  .map { block ->
    val lines = block.split("\n")
    val buttonA = lines[0]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonA(it["x"]!!.value.toInt(), it["y"]!!.value.toInt()) }
    val buttonB = lines[1]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { ButtonB(it["x"]!!.value.toInt(), it["y"]!!.value.toInt()) }
    val prize = lines[2]
      .let { xyPattern.find(it)?.groups as MatchNamedGroupCollection }
      .let { Prize(it["x"]!!.value.toInt(), it["y"]!!.value.toInt()) }

    Game(prize, buttonA, buttonB)
  }

data class Day13B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

private sealed class Button(open val x: Int, open val y: Int, open val cost: Int) {
  fun push(position: Position, times: Int = 1): Position =
    Position(position.x + (times * x), position.y + (times * y))
}

private data class ButtonA(override val x: Int, override val y: Int) : Button(x, y, 3)
private data class ButtonB(override val x: Int, override val y: Int) : Button(x, y, 1)

private data class Prize(val x: Int, val y: Int)

private data class Position(val x: Int, val y: Int)

private data class Game(val prize: Prize, val buttonA: ButtonA, val buttonB: ButtonB) {
  fun searchOptimalSolution(): Int? {
    val maxPushes = 100
    if (prize.x > maxPushes * (buttonA.x + buttonB.x) || prize.y > maxPushes * (buttonA.y + buttonB.y)) {
      println()
      println("can't even reach \n$this")
      return null
    }

    val origin = Point(0.0, 0.0)
    val aPointVector = Point(buttonA.x.toDouble(), buttonA.y.toDouble())

    val target = Point(prize.x.toDouble(), prize.y.toDouble())
    val bPointVector = Point(target.x + buttonB.x.toDouble(), target.y + buttonB.y.toDouble())

    val aLine = Line(origin, aPointVector)
    val bLine = Line(target, bPointVector)

    val intersection = findIntersection(aLine, bLine)
    println("intersection: $intersection")

    println(intersection.x.toInt() % buttonA.x)
    println(intersection.y.toInt() % buttonA.y)
    println((prize.x - intersection.x.toInt()) % buttonB.x)
    println((prize.y - intersection.y.toInt()) % buttonB.y)


    if (
      intersection.x.toInt() % buttonA.x == 0 &&
      intersection.y.toInt() % buttonA.y == 0 &&
      (prize.x - intersection.x.toInt()) % buttonB.x == 0 &&
      (prize.y - intersection.y.toInt()) % buttonB.y == 0
    ) {
      val aPushes = intersection.x.toInt() / buttonA.x
      val bPushes = (prize.x - intersection.x.toInt()) / buttonB.x
      return (bPushes * buttonB.cost) + (aPushes * buttonA.cost)
    } else {
      println("Couldn't find solution for: \n$this")

      return null
    }



    for (aPushes in maxPushes downTo 0) {
      val posAfterA = buttonA.push(Position(0, 0), aPushes)
      for (bPushes in maxPushes downTo 0) {
        val posAfterB = buttonB.push(posAfterA, bPushes)
        if (prize.x == posAfterB.x && prize.y == posAfterB.y) {
          println("posAfterA: $posAfterA")
          println("$bPushes bPushes $aPushes aPushes")
          return (bPushes * buttonB.cost) + (aPushes * buttonA.cost)
        }
      }
    }
    println()
    println("Couldn't find solution for: \n$this")

    return null
  }
}


// line intersection code taken from https://rosettacode.org/wiki/Find_the_intersection_of_two_lines#Kotlin

data class Point(val x: Double, val y: Double) {
  override fun toString() = "{$x, $y}"
}

data class Line(val A: Point, val B: Point)

fun findIntersection(lineM: Line, lineN: Line): Point {
  val a1 = lineM.B.y - lineM.A.y
  val b1 = lineM.A.x - lineM.B.x
  val c1 = a1 * lineM.A.x + b1 * lineM.A.y

  val a2 = lineN.B.y - lineN.A.y
  val b2 = lineN.A.x - lineN.B.x
  val c2 = a2 * lineN.A.x + b2 * lineN.A.y

  val delta = a1 * b2 - a2 * b1
  // If lines are parallel, intersection point will contain infinite values
  return Point((b2 * c1 - b1 * c2) / delta, (a1 * c2 - a2 * c1) / delta)
}