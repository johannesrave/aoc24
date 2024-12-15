package codes.jrave

import java.io.File
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
  val day09ATest = Day09A("input/test_09")
  val day09ATestResult = day09ATest.solve()
  println("Test result for Day09A: $day09ATestResult")
  assert(day09ATestResult == 1928.toBigInteger())

  val day09A = Day09A("input/input_09")
  val durationA = measureTimeMillis {
    val solution = day09A.solve()
    println("Solution for Day09A: $solution")
    assert(solution > 152661574.toBigInteger())
  }
  println("Solution took $durationA milliseconds")

//  val day09BTest = Day09B("input/test_09")
//  val day09BTestResult = day09BTest.solve()
//  println("Test result for Day09B: $day09BTestResult")
//  assert(day09BTestResult == 1)
//
//  val day09B = Day09B("input/input_09")
//  val duration09B = measureTimeMillis {
//    val solution = day09B.solve()
//    println("Solution for Day09B: $solution")
//    assert(solution == 1)
//  }
//  println("Solution took $duration09B milliseconds")
}


data class Day09A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {

    val expandedMemory = mutableListOf<Int?>()
    var idCounter = 0
    input
      .map { it.toString().toInt() }
      .forEachIndexed { i, n ->
        if (i % 2 == 0) {
          expandedMemory += List(n) { idCounter }.toMutableList()
          idCounter++
        } else {
          expandedMemory += List(n) { null }.toMutableList()
        }
      }

    expandedMemory.also { println(it) }

    while (expandedMemory.indices.any { i ->
        if (i == expandedMemory.lastIndex) return@any false
        expandedMemory[i] == null && expandedMemory[i + 1] != null
      }) {
      val blockToMove = expandedMemory.removeLast()
      if (blockToMove != null) {
        val insertionIndex = expandedMemory.indexOfFirst { it == null }
        expandedMemory.removeAt(insertionIndex)
        expandedMemory.add(insertionIndex, blockToMove)
      }
    }
    expandedMemory.also { println(it) }

    val checksums = expandedMemory.mapIndexed { i, id -> (i.toBigInteger() * id!!.toBigInteger()) }

    checksums.also { println(it) }

    return checksums.sum()
  }
}

private fun List<BigInteger>.sum(): BigInteger = this.reduce(BigInteger::plus)

data class Day09B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}