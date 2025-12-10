package codes.jrave.aoc2024

import codes.jrave.sum
import java.io.File
import java.math.BigInteger
import kotlin.system.measureTimeMillis

fun main() {
  val day09ATest = Day09A("input/2024/day09_test")
  val day09ATestResult = day09ATest.solve()
  println("Test result for Day09A: $day09ATestResult")
  assert(day09ATestResult == 1928.toBigInteger())

  val day09A = Day09A("input/2024/day09_input")
  val durationA = measureTimeMillis {
    val solution = day09A.solve()
    println("Solution for Day09A: $solution")
    assert(solution > 152661574.toBigInteger())
  }
  println("Solution took $durationA milliseconds")

  val day09BTest = Day09B("input/2024/day09_test")
  val day09BTestResult = day09BTest.solve()
  println("Test result for Day09B: $day09BTestResult")
  assert(day09BTestResult == 2858.toBigInteger())

  val day09B = Day09B("input/2024/day09_input")
  val duration09B = measureTimeMillis {
    val solution = day09B.solve()
    println("Solution for Day09B: $solution")
    assert(solution == 6448168620520.toBigInteger())
  }
  println("Solution took $duration09B milliseconds")
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

data class Day09B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {

    var memory = parseMemory(input)

    memory.also { println(it) }

    val nOfFiles = memory.count { it is File }
    val checkedIds = mutableSetOf<BigInteger>()

    while (checkedIds.size < nOfFiles) {

      // find the next index of a file that hasn't been checked from the end of the memory
      // there must always be one, or the while loop would've terminated
      val fileIndex = memory.indexOfLast { file -> file is File && file.id !in checkedIds }

      // fetch the file at that index and mark it as 'checked'
      val file = (memory[fileIndex] as File).also { file -> checkedIds.add(file.id) }

      // look for the index of a slot (empty memory location) what is long/wide enough to accept the file
      val slotIndex = memory.subList(0, fileIndex)
        .indexOfFirst { slot -> slot is Slot && slot.length >= file.length }

      // if there is no such slot, leave everthing as it is and move on to the next file
      if (slotIndex == -1) continue

        // fetch the slot object at the index and shorten it by the length of the file.
      val slot = memory[slotIndex] as Slot
      slot.length -= file.length

      // remove the file from the memory and replace it with an empty Slot of the same length
      memory.removeAt(fileIndex)
      memory.add(fileIndex, Slot(file.length))

      // add the file back into memory at the location where we found the slot earlier
      memory.add(slotIndex, file)

      // the process creates neighbouring Slot objects in memory as well as Slots of length 0
      // we brute force cleaning the length-0 ones and merging the others.
      memory = cleanAndMergeNeighbouringSlots(memory)
    }

    memory.also { println(it) }

    return calculateChecksum(memory)
  }

  private fun parseMemory(input: String): MutableList<MemoryLocation> {
    val memory = mutableListOf<MemoryLocation>()
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
    return memory
  }

  private fun cleanAndMergeNeighbouringSlots(memory: MutableList<MemoryLocation>): MutableList<MemoryLocation> {
    var memory1 = memory
    memory1 = memory1.mapIndexedNotNull { i, location ->
      when {
        location !is Slot -> location
        location.length == 0 -> null
        (i + 1 in memory1.indices && memory1[i + 1] is Slot) -> {
          val nextLocation = memory1[i + 1] as Slot
          location.length += nextLocation.length
          nextLocation.length = 0
          location
        }

        else -> location
      }
    }.toMutableList()
    return memory1
  }

  private fun calculateChecksum(memory: MutableList<MemoryLocation>): BigInteger {
    var checkSum = 0.toBigInteger()
    var index = 0
    for (location in memory) {
      // for each file in memory, calculate its checksum by
      // multiplying its id with the index of each of its blocks
      // (so in the range from its first index until the last block whose location is index+file.length-1)
      if (location is File) {
        val indexOfLastFileBlock = index + location.length
        for (i in index..<indexOfLastFileBlock) {
          checkSum += location.id * i.toBigInteger()
        }
      }

      // we need to manually manage the index when walking over both Files and Slots,
      // as we're not using the natural index of the memory list (where items have varying length)
      index += location.length
    }
    return checkSum
  }

  open class MemoryLocation(open val length: Int)

  data class File(override val length: Int, val id: BigInteger) : MemoryLocation(length)
  data class Slot(override var length: Int) : MemoryLocation(length)
}