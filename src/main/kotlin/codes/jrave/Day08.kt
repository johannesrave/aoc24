package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day08ATest = Day08A("input/day08_test")
  val day08ATestResult = day08ATest.solve()
  assert(day08ATestResult == 14)

  val day08A = Day08A("input/day08_input")
  val durationA = measureTimeMillis {
    val solution = day08A.solve()
    println("Solution for Day08A: $solution")
    assert(solution == 308)
  }
  println("Solution took $durationA milliseconds")

  val day08BTest = Day08B("input/day08_test")
  val day08BTestResult = day08BTest.solve()
  println("Test result for Day08B: $day08BTestResult")
  assert(day08BTestResult == 34)

  val day08B = Day08B("input/day08_input")
  val duration08B = measureTimeMillis {
    val solution = day08B.solve()
    println("Solution for Day08B: $solution")
    assert(solution == 1147)
  }
  println("Solution took $duration08B milliseconds")
}


data class Day08A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val emptyChar = '.'
    val charsToPostions = mutableMapOf<Char, MutableList<Pos>>()

    board.forEachIndexed { y, row ->
      row.forEachIndexed { x, c ->
        if (c == emptyChar) return@forEachIndexed
        charsToPostions.putIfAbsent(c, mutableListOf())
        charsToPostions[c]!!.add(Pos(y, x))
      }
    }

    charsToPostions.also { println(it) }

    val antinodes = mutableSetOf<Pos>()

    charsToPostions.keys.forEach { k ->
      val positions = charsToPostions[k]!!
      for (i in positions.indices) {
        val current = positions[i]
        for (j in positions.indices.filterNot { it == i }) {
          val other = positions[j]
          val offset = other - current
          val antiNodeCandidate = current - offset
          if (antiNodeCandidate in board) {
            antinodes.add(antiNodeCandidate)
          }
        }
      }
    }

    println(board.markPositions(antinodes.toList(), '#').toPrintString())

    return antinodes.size
  }
}

data class Day08B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val emptyChar = '.'
    val charsToPostions = mutableMapOf<Char, MutableList<Pos>>()

    board.forEachIndexed { y, row ->
      row.forEachIndexed { x, c ->
        if (c == emptyChar) return@forEachIndexed
        charsToPostions.putIfAbsent(c, mutableListOf())
        charsToPostions[c]!!.add(Pos(y, x))
      }
    }

//    charsToPostions.also { println(it) }

    val antinodes = mutableSetOf<Pos>()

    charsToPostions.keys.forEach { k ->
      val positions = charsToPostions[k]!!
      for (i in positions.indices) {
        for (j in positions.indices.filterNot { it == i }) {
          var current = positions[i]
          val other = positions[j]
          val offset = other - current
          while (current in board) {
            antinodes.add(current)
            current -= offset
          }
        }
      }
    }

//    println(board.markPositions(antinodes.toList(), '#').toPrintString())

    return antinodes.size
  }
}