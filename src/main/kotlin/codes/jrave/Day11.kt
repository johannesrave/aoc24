package codes.jrave

import java.io.File
import java.math.BigInteger
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.async

fun main() {
  val day11ATest = Day11A("input/test_11")
  val day11ATestResult = day11ATest.solve()
  println("Test result for Day11A: $day11ATestResult")
  assert(day11ATestResult == 55312)

  val day11A = Day11A("input/input_11")
  val durationA = measureTimeMillis {
    val solution = day11A.solve()
    println("Solution for Day11A: $solution")
    assert(solution == 233050)
  }
  println("Solution took $durationA milliseconds")

  val day11BTest = Day11B("input/test_11")
  val day11BTestResult = day11BTest.solve()
  println("Test result for Day11B: $day11BTestResult")
  assert(day11BTestResult == 55312.toBigInteger())

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
  fun solve(input: String = this.input): Int {
    var stones = input.split(" ").map { n -> Stone(n) }
    val blinks = 25

    for (i in 0..<blinks) {
      stones = stones.flatMap { it.transform() }
    }
    return stones.size
  }
}

data class Day11B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val initialStones = input.split(" ").map { n -> Stone(n) }.toMutableList()
    val blinks = 75

    var queueOfLists = mutableListOf(initialStones)
    for (i in 0..<blinks) {
      if (i % 5 == 0 || i > blinks - 5) {
        println(
          "after $i blinks there are this many stones: ${
            queueOfLists.map { it.size.toBigInteger() }.sum()
          }"
        )
      }
      val queueBuffer = mutableListOf<MutableList<Stone>>()
      for (list in queueOfLists) {
        val listBuffer = mutableListOf<Stone>()
        for (stone in list) {
          val n = stone.n
          when {
            n == "0" -> listBuffer.add(Stone("1"))
            n.length % 2 == 0 -> {
              val left = n.substring(0, n.length / 2)
              val right = n.substring(n.length / 2).toBigInteger().toString()
              listBuffer.add(Stone(left))
              listBuffer.add(Stone(right))
            }
            else -> listBuffer.add(Stone((n.toBigInteger() * 2024.toBigInteger()).toString()))
          }
        }
        queueBuffer.addAll(listBuffer.chunked(1_000_000).map { it.toMutableList() })
      }
      queueOfLists = queueBuffer
    }
    return queueOfLists.map { it.size.toBigInteger() }.sum()
  }
}

private data class Stone(val n: String) {
  fun transform(): List<Stone> = when {
    n == "0" -> listOf(Stone("1"))
    n.length % 2 == 0 -> {
      val left = n.substring(0, n.length / 2)
      val right = n.substring(n.length / 2).toBigInteger().toString()
      listOf(Stone(left), Stone(right))
    }

    else -> listOf(Stone((n.toBigInteger() * 2024.toBigInteger()).toString()))
  }
}