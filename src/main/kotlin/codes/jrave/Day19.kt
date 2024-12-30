package codes.jrave

import java.io.File
import java.util.PriorityQueue
import kotlin.system.measureTimeMillis

fun main() {
  val day19ATest = Day19A("input/day19_test")
  val day19ATestResult = day19ATest.solve()
  println("Test result for Day19A: $day19ATestResult")
  assert(day19ATestResult == 6)

  val day19A = Day19A("input/day19_input")
  val durationA = measureTimeMillis {
    val solution = day19A.solve()
    println("Solution for Day19A: $solution")
    assert(solution == 315)
  }
  println("Solution took $durationA milliseconds")

  val day19BTest = Day19B("input/day19_test")
  val day19BTestResult = day19BTest.solve()
  println("Test result for Day19B: $day19BTestResult")
  assert(day19BTestResult == 16)

  val day19B = Day19B("input/day19_input")
  val duration19B = measureTimeMillis {
    val solution = day19B.solve()
    println("Solution for Day19B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration19B milliseconds")
}


data class Day19A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {

    val towels = input.split("\n\n").first().split(", ")
    val patterns = input.split("\n\n").last().split("\n")

    println(towels)
    println(patterns)

    return patterns.filter { pattern -> canBeCreatedWithTowels(pattern, towels) }.size
  }

  private fun canBeCreatedWithTowels(pattern: String, towels: List<String>): Boolean {
    val queue = PriorityQueue<String> { subPatternA, subPatternB ->
      subPatternA.length - subPatternB.length
    }

    queue.add(pattern)

    while (queue.isNotEmpty()) {
      val subPattern = queue.remove()
      if (towels.any { towel -> subPattern == towel }) return true;
      towels.filter { towel -> subPattern.startsWith(towel) }
        .forEach { towel -> queue.add(subPattern.removePrefix(towel)) }
    }
    return false
  }
}

data class Day19B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {

    val towels = input.split("\n\n").first().split(", ").distinct()
    val patterns = input.split("\n\n").last().split("\n")

    println(towels)
    println(patterns)

    return patterns.sumOf { pattern -> canBeCreatedWithTowelsInNWays(pattern, towels) }
  }

  private fun canBeCreatedWithTowelsInNWays(pattern: String, towels: List<String>): Int {
    val relevantTowels = towels.filter { towel -> pattern.contains(towel) }

    val queue = PriorityQueue<String> { subPatternA, subPatternB ->
      subPatternA.length - subPatternB.length
    }

    queue.add(pattern)
    var counter = 0

    while (queue.isNotEmpty()) {
      val subPattern = queue.remove()
//      if (relevantTowels.any { towel -> subPattern == towel }) counter++;
      for (towel in relevantTowels) {
        if (subPattern.startsWith(towel)) {
          val newSubPattern = subPattern.substring(towel.length)
          if (newSubPattern == "") {
            counter++
          } else queue.add(newSubPattern)
        }
      }

    }
    return counter
  }
}