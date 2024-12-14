package codes.jrave

import java.io.File
import java.math.BigInteger
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {
  val day07ATest = Day07A("input/test_07")
  val day07ATestResult = day07ATest.solve()
  assert(day07ATestResult == 3749.toBigInteger())

  val day07A = Day07A("input/input_07")
  val durationA = measureTimeMillis {
    val solution = day07A.solve()
    println("Solution for Day07A: $solution")
    assert(solution == 6083020304036.toBigInteger())
  }
  println("Solution took $durationA milliseconds")

  val day07BTest = Day07B("input/test_07")
  val day07BTestResult = day07BTest.solve()
  println("Test result for Day07B: $day07BTestResult")
  assert(day07BTestResult == 11387.toBigInteger())
//
//  val day07B = Day07B("input/input_07")
//  val duration07B = measureTimeMillis {
//    val solution = day07B.solve()
//    println("Solution for Day07B: $solution")
//    assert(solution == 1688)
//  }
//  println("Solution took $duration07B milliseconds")
}


data class Day07A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val lines = input.split("\n")
    val resultsToOperands = lines.map { line ->
      val result = line.split(":")[0].toBigInteger()
      val operands = line.split(":")[1].trim().split(" ").map { it.toBigInteger() }
      result to operands
    }

    return resultsToOperands
      .filter { (result, operands) -> validOperationExists(result, operands) }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }

  fun validOperationExists(result: BigInteger, operands: List<BigInteger>): Boolean {
    if (operands.size == 1) return result == operands.first()
    val operationsTable = OperationsTable.ops(operands.size - 1)

    return operationsTable.any { operationsRow ->
      val operationsRowResult = operationsRow
        .foldIndexed(operands.first()) { i, acc, op -> op(acc, operands[i + 1]) }
      result == operationsRowResult
    }
  }
}

data class Day07B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val lines = input.split("\n")


    val resultsToNestedOperands = lines.map { line ->
      val result = line.split(":")[0].toBigInteger()
      val stringOperands = line.split(":")[1].trim().split(" ")
      val nestedOperands = joinOrConcatenate(stringOperands).map { it.map { it.toBigInteger() } }
      result to nestedOperands
    }

    return resultsToNestedOperands
      .filter { (result, operandsTable) ->
        operandsTable.any { operandsRow -> validOperationExists(result, operandsRow) }
      }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }

  fun joinOrConcatenate(operandsRow: List<String>): List<List<String>> {
    val join = "";
    val separate = "_"
    val separatorTable = Truthtable.bits(operandsRow.size - 1)
      // map bools from truthtable to " " or "" and add one empty "" as
      // last element to make it the same size as the operands
      .map { boolRow -> (boolRow.map { if (it) separate else join } + join) }

    val newOperandsTable = mutableListOf<List<String>>()

    for (separatorRow in separatorTable) {
      val joinedOperandsRow = operandsRow
        .reduceIndexed { i, acc, int ->
          "$acc${separatorRow[i - 1]}$int"
        }
      newOperandsTable += joinedOperandsRow.split(separate)
    }
    return newOperandsTable.also { println(it) }
  }

  fun validOperationExists(result: BigInteger, operands: List<BigInteger>): Boolean {
    if (operands.size == 1) return result == operands.first()
    val operationsTable = OperationsTable.ops(operands.size - 1)

    return operationsTable.any { operationsRow ->
      val operationsRowResult = operationsRow
        .foldIndexed(operands.first()) { i, acc, op -> op(acc, operands[i + 1]) }
      result == operationsRowResult
    }
  }
}

typealias Operation = (left: BigInteger, right: BigInteger) -> BigInteger

object OperationsTable {
  val tables = mutableMapOf<Int, List<List<Operation>>>()

  fun ops(ops: Int): List<List<Operation>> {
    tables.putIfAbsent(ops, buildTable(ops))
    return tables[ops]!!
  }

  private fun buildTable(bits: Int): List<List<Operation>> {
    var i = 0b00000
    var rows = mutableListOf<List<Operation>>()
    while (i < (2.0).pow(bits)) {
      val bitString = i.toString(2).padStart(bits, '0')
      val row: List<Operation> =
        bitString.map { if (it == '1') BigInteger::add else BigInteger::multiply }
      rows += listOf(row)
      i++
    }
    return rows
  }
}