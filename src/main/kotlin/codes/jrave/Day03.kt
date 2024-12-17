package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day03ATest = Day03A("input/day03_test")
  assert(day03ATest.solve() == 161)

  val day03A = Day03A("input/day03_input")
  val durationA = measureTimeMillis { println("Solution for Day03A: ${day03A.solve()}") }
  println("solution took $durationA milliseconds")

  val day03BTest = Day03B("input/day03_test")
  assert(day03BTest.solve("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") == 48)

  val day03B = Day03B("input/day03_input")
  val durationB = measureTimeMillis {
    val solution = day03B.solve()
    assert(solution == 90044227)
    println("Solution for Day03B: $solution")
  }
  println("solution took $durationB milliseconds")

}

data class Day03A(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(): Int {
    val memory = input
    val multiplicationRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    return multiplicationRegex
      .findAll(memory).map { multiplication ->
        val (first, second) = multiplication.destructured
        first.toInt() * second.toInt()
      }.sum()
  }
}

data class Day03B(
  val inputPath: String,
  val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val inactiveSegmentRegex = Regex("""don't\(\).*?(do\(\)|$)""")
    val multiplicationRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    val memory = input
      .replace("\n", "")
      .split(inactiveSegmentRegex)
      .joinToString("")


    return multiplicationRegex.findAll(memory)
      .map { multiplication ->
        val (first, second) = multiplication.destructured
        first.toInt() * second.toInt()
      }.sum()
  }
}