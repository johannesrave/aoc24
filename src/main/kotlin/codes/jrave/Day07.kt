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
      .filter { (result, operands) -> isPossible(result, operands) }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }

  private fun isPossible(result: BigInteger, operands: List<BigInteger>): Boolean {
    val operatorCombos = operatorCombos(operands)

    return operatorCombos.any { combo ->
      result == combo.foldIndexed(operands.first()) { i, acc, op -> op(acc, operands[i + 1]) }
    }
  }

  private fun operatorCombos(operands: List<BigInteger>):
      Set<List<(left: BigInteger, right: BigInteger) -> BigInteger>> {

    val operators = setOf(BigInteger::plus, BigInteger::multiply)
    var operatorCombos = setOf(listOf<(left: BigInteger, right: BigInteger) -> BigInteger>())

    for (operand in 1..operands.lastIndex) {
      val expandedCombos = mutableSetOf<List<(left: BigInteger, right: BigInteger) -> BigInteger>>()
      for (operatorCombo in operatorCombos) {
        for (operator in operators) {
          expandedCombos += (operatorCombo + listOf(operator))
        }
      }
      operatorCombos = expandedCombos
    }

    return operatorCombos
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

    return resultsToOperands
      .filter { (result, operands) -> isPossible(result, operands) }
      .fold(BigInteger.ZERO) { acc, (result, _) -> acc + result }
  }

  private fun isPossible(result: BigInteger, operands: List<BigInteger>): Boolean {
    val operatorCombos = operatorCombos(operands)

    return operatorCombos.any { combo ->
      result == combo.foldIndexed(operands.first()) { i, acc, op -> op(acc, operands[i + 1]) }
    }
  }

  private fun operatorCombos(operands: List<BigInteger>):
      Set<List<(left: BigInteger, right: BigInteger) -> BigInteger>> {

    val operators = setOf(BigInteger::plus, BigInteger::multiply)
    var operatorCombos = setOf(listOf<(left: BigInteger, right: BigInteger) -> BigInteger>())

    for (operand in 1..operands.lastIndex) {
      val expandedCombos = mutableSetOf<List<(left: BigInteger, right: BigInteger) -> BigInteger>>()
      for (operatorCombo in operatorCombos) {
        for (operator in operators) {
          expandedCombos += (operatorCombo + listOf(operator))
        }
      }
      operatorCombos = expandedCombos
    }

    return operatorCombos
  }
}