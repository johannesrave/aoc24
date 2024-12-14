package codes.jrave

import java.io.File
import java.math.BigInteger
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

  val day07B = Day07B("input/input_07")
  val duration07B = measureTimeMillis {
    val solution = day07B.solve()
    println("Solution for Day07B: $solution")
    assert(solution == 59002246504791.toBigInteger())
  }
  println("Solution took $duration07B milliseconds")
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

    val allowedOperations = listOf(BigInteger::add, BigInteger::multiply)
    val operationsTable = OperationVariationsTable(allowedOperations)

    return resultsToOperands
      .filter { (result, operands) -> validOperationExists(result, operands, operationsTable) }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }
}

data class Day07B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): BigInteger {
    val lines = input.split("\n")

    val resultsToOperands = lines.map { line ->
      val result = line.split(":")[0].toBigInteger()
      val operands = line.split(":")[1].trim().split(" ").map { it.toBigInteger() }
      result to operands
    }

    val allowedOperations = listOf(BigInteger::add, BigInteger::multiply, BigInteger::concatenate)
    val operationsTable = OperationVariationsTable(allowedOperations)

    return resultsToOperands
      .filter { (result, operands) -> validOperationExists(result, operands, operationsTable) }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }
}

fun validOperationExists(
  result: BigInteger,
  operands: List<BigInteger>,
  operationsTable: OperationVariationsTable
): Boolean {
  return operationsTable[operands.size - 1].any { operationsRow ->
    val operationsRowResult = operationsRow
      .foldIndexed(operands.first()) { i, acc, op -> op(acc, operands[i + 1]) }
    result == operationsRowResult
  }
}

typealias Operation = (left: BigInteger, right: BigInteger) -> BigInteger

fun BigInteger.concatenate(other: BigInteger): BigInteger =
  (this.toString() + other.toString()).toBigInteger()

data class OperationVariationsTable(val operations: List<Operation>) {
  private val tables = mutableMapOf<Int, List<List<Operation>>>()

  operator fun get(width: Int): List<List<Operation>> {
    tables.computeIfAbsent(width) { buildTable(width) }
    return tables[width]!!
  }

  private fun buildTable(noOfOperands: Int): List<List<Operation>> {
    println("Building OperationVariationTable for $noOfOperands operands")
    val variants = operations.size
    return VariationsTable[noOfOperands, variants.toDouble()]
      .map { row -> row.map { i -> operations[i] } }
  }
}