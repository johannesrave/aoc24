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

    for (aPushes in maxPushes downTo 0) {
      val posAfterA = buttonA.push(Position(0, 0), aPushes)
      for (bPushes in maxPushes downTo 0) {
        val posAfterB = buttonB.push(posAfterA, bPushes)
        if (prize.x == posAfterB.x && prize.y == posAfterB.y) {
//          println()
//          println("$bPushes bPushes $aPushes aPushes")
          return (bPushes * buttonB.cost) + (aPushes * buttonA.cost)
        }
      }
    }
    println()
    println("Couldn't find solution for: \n$this")

    return null
  }
}