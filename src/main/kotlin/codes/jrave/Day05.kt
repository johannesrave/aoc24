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
    println("Solution for Day05A: $solution")
    assert(solution == 5651)
  }
  println("Solution took $durationA milliseconds")

  val day05BTest = Day05B("input/test_05")
  val day05BTestResult = day05BTest.solve()
  assert(day05BTestResult == 123)
  val day05B = Day05B("input/input_05")
  val duration05B = measureTimeMillis {
    val solution = day05B.solve()
    println("Solution for Day05B: $solution")
    assert(solution == 4743)
  }
  println("Solution took $duration05B milliseconds")
}


data class Day05A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val (rules, pages) = input.split("\n\n").map { it.split("\n") }

    val rulePatterns = rules.map { rule ->
      val (first, second) = rule.split("|")
      first to second
    }

    return pages.filter { page ->
      rulePatterns.all { (first, second) ->
        (first in page && second in page && isSortedCorrectly(page, first, second))
            || (first !in page || second !in page)
      }
    }.map { it.split(",") }
      .map { numbers ->
        val middleIndex = numbers.count() / 2
        numbers[middleIndex]
      }.sumOf { it.toInt() }
  }

  private fun isSortedCorrectly(page: String, first: String, second: String) =
    Regex("$first.*$second").containsMatchIn(page)
}


data class Day05B(
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