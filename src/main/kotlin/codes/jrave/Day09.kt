package codes.jrave

import java.io.File
import java.math.BigInteger

fun main() {
//  val day09ATest = Day09A("input/test_09")
//  val day09ATestResult = day09ATest.solve()
//  println("Test result for Day09A: $day09ATestResult")
//  assert(day09ATestResult == 1928.toBigInteger())
//
//  val day09A = Day09A("input/input_09")
//  val durationA = measureTimeMillis {
//    val solution = day09A.solve()
//    println("Solution for Day09A: $solution")
//    assert(solution > 152661574.toBigInteger())
//  }
//  println("Solution took $durationA milliseconds")

  val day09BTest = Day09B("input/test_09")
  val day09BTestResult = day09BTest.solve()
  println("Test result for Day09B: $day09BTestResult")
  assert(day09BTestResult == 2858.toBigInteger())
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
      .also { println(it) }
      .map { it.toString().toInt() }
      .forEachIndexed { i, n ->
        if (i % 2 == 0) {
          expandedMemory += List(n) { idCounter }.toMutableList()
          idCounter++
        } else {
          expandedMemory += List(n) { null }.toMutableList()
        }
      }

//    expandedMemory.also { println(it) }

    while (expandedMemory.indices.any { i ->
        if (i == expandedMemory.lastIndex) return@any false
        expandedMemory[i] == null && expandedMemory[i + 1] != null
      }) {
      val blockToMove = expandedMemory.removeLast()
      if (blockToMove != null) {
        val insertionIndex = expandedMemory.indexOfFirst { it == null }
        expandedMemory[insertionIndex] = blockToMove
      }
    }
//    expandedMemory.also { println(it) }

    val checksums = expandedMemory.mapIndexed { i, id -> (i.toBigInteger() * id!!.toBigInteger()) }

//    checksums.also { println(it) }

    return checksums.sum()
  }
}

private fun List<BigInteger>.sum(): BigInteger = this.reduce(BigInteger::plus)

data class Day09B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {

    var memory = mutableListOf<MemoryLocation>()
    var idCounter = 0.toBigInteger()
    input
      .also { println(it) }
      .map { it.toString().toInt() }
      .forEachIndexed { i, n ->
        if (i % 2 == 0) {
          memory += File(n, idCounter)
          idCounter++
        } else {
          memory += Slot(n)
        }
      }

    memory.also { println(it) }

    val nOfFiles = memory.count { it is File }
    val checkedIds = mutableSetOf<BigInteger>()

    while (checkedIds.size < nOfFiles) {
      val fileIndex = memory
        .indexOfLast { file -> file is File && file.id !in checkedIds }

      val file = memory[fileIndex] as File
      checkedIds.add(file.id)

      val slotIndex = memory
        .indexOfFirst { slot -> slot is Slot && slot.length >= file.length }

      if (slotIndex == -1) continue;

      val slot = memory[slotIndex] as Slot

      slot.length -= file.length

      memory.removeAt(fileIndex)
//      if (slot.length == 0) memory.removeAt(slotIndex)
      memory.add(slotIndex, file)

      memory.reverse()
      memory.forEachIndexed { i, location ->
        val nextLocation = if (i != memory.lastIndex) memory[i + 1]
        else return@forEachIndexed;
        if (location is Slot && nextLocation is Slot) {
          nextLocation.length += location.length
          location.length = 0
        }
      }
      memory.reverse()
      memory = memory.filter { it is Slot && it.length == 0 }.toMutableList()
    }



    memory.also { println(it) }

    var checkSum = 0.toBigInteger()
    var index = 0
    for (location in memory) {
      if (location is File) {
        for (i in index..<(index + location.length)) {
          checkSum += location.id * i.toBigInteger()
        }
      }

      index += location.length
    }

//    checksums.also { println(it) }

    return checkSum
  }

  open class MemoryLocation(open val length: Int)

  data class File(override val length: Int, val id: BigInteger) : MemoryLocation(length)
  data class Slot(override var length: Int) : MemoryLocation(length)
}