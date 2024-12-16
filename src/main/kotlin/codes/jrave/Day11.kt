package codes.jrave

import arrow.core.MemoizedDeepRecursiveFunction
import java.io.File
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
  val day11ATest = Day11A("input/test_11")
  val day11ATestResult = day11ATest.solve()
  println("Test result for Day11A: $day11ATestResult")
  assert(day11ATestResult == 55312.toBigInteger())

  val day11A = Day11A("input/input_11")
  val durationA = measureTimeMillis {
    val solution = day11A.solve()
    println("Solution for Day11A: $solution")
    assert(solution == 233050.toBigInteger())
  }
  println("Solution took $durationA milliseconds")

  val day11BTest = Day11B("input/test_11")
  val day11BTestResult = day11BTest.solve()
  println("Test result for Day11B: $day11BTestResult")
//  assert(day11BTestResult == 55312.toBigInteger())

  val day11B = Day11B("input/input_11")
  val duration11B = measureTimeMillis {
    val solution = day11B.solve()
    println("Solution for Day11B: $solution")
    assert(solution == 1.toBigInteger())
  }
  println("Solution took $duration11B milliseconds")
}


data class Day11A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val blinks = 25
    val stones = input.split(" ").map { value -> Stone(value, blinks) }.toMutableList()

    return stones.map { memoizedStoneTransform(it) }.sum()
  }
}

data class Day11B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val blinks = 75
    val stones = input.split(" ").map { value -> Stone(value, blinks) }.toMutableList()

    return stones.map { memoizedStoneTransform(it) }.sum()
  }
}

val memoizedStoneTransform = MemoizedDeepRecursiveFunction<Stone, BigInteger> { (value, blinks) ->
  when {
    blinks == 0 -> BigInteger.ONE
    value == "0" ->
      callRecursive(Stone("1", blinks - 1))

    value.length % 2 == 0 -> {
      val left = value.substring(0, value.length / 2)
      val right = value.substring(value.length / 2).toBigInteger().toString()

      callRecursive(Stone(left, blinks - 1)) +
          callRecursive(Stone(right, blinks - 1))
    }

    else ->
      callRecursive(Stone((value.toBigInteger() * 2024.toBigInteger()).toString(), blinks - 1))
  }
}

data class Stone(val n: String, val blinks: Int)