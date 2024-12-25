package codes.jrave

import java.io.File
import kotlin.math.pow
import kotlin.system.measureTimeMillis

fun main() {
  val day17ATest = Day17A("input/day17_test")
  val day17ATestResult = day17ATest.solve()
  println("Test result for Day17A: $day17ATestResult")
  assert(day17ATestResult == "4,6,3,5,6,3,5,2,1,0")

  val day17A = Day17A("input/day17_input")
  val durationA = measureTimeMillis {
    val solution = day17A.solve()
    println("Solution for Day17A: $solution")
    assert(solution == "")
  }
  println("Solution took $durationA milliseconds")

//  val day17BTest = Day17B("input/day17_test")
//  val day17BTestResult = day17BTest.solve()
//  println("Test result for Day17B: $day17BTestResult")
//  assert(day17BTestResult == "")
//
//  val day17B = Day17B("input/day17_input")
//  val duration17B = measureTimeMillis {
//    val solution = day17B.solve()
//    println("Solution for Day17B: $solution")
//    assert(solution == "")
//  }
//  println("Solution took $duration17B milliseconds")
}


data class Day17A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): String {

    val instructions = Regex("""Program: (\S*)""").find(input)!!.groupValues[1].split(',')
      .chunked(2) { (a, b) -> Instruction(a.toInt(), b.toInt()) }

    val a = Regex("""Register A: (\d+)""").find(input)!!.groupValues[1].toInt()
    val b = Regex("""Register B: (\d+)""").find(input)!!.groupValues[1].toInt()
    val c = Regex("""Register C: (\d+)""").find(input)!!.groupValues[1].toInt()

    var state = ProgramState(a, b, c, 0, mutableListOf())

    while (state.i / 2 <= instructions.lastIndex) {
      state = state.process(instructions[state.i / 2])
    }

    return state.output.joinToString(",")
  }
}

data class Day17B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): String {

    val programString = Regex("""Program: (\S*)""").find(input)!!.groupValues[1]
    val instructions = programString.split(',')
      .chunked(2) { (a, b) -> Instruction(a.toInt(), b.toInt()) }

    val a = Regex("""Register A: (\d+)""").find(input)!!.groupValues[1].toInt()
    val b = Regex("""Register B: (\d+)""").find(input)!!.groupValues[1].toInt()
    val c = Regex("""Register C: (\d+)""").find(input)!!.groupValues[1].toInt()

    var state = ProgramState(a, b, c, 0, mutableListOf())

    while (state.i / 2 <= instructions.lastIndex) {
      state = state.process(instructions[state.i / 2])
    }

    return state.output.joinToString(",")
  }
}

data class Instruction(val opcode: Int, val operand: Int)

data class ProgramState(val a: Int, val b: Int, val c: Int, val i: Int, val output: List<Int>) {
  fun process(inst: Instruction): ProgramState {
    val (opcode, operand) = inst
    return when (opcode) {
      0 -> copy(a = (this.a / 2.0.pow(comboOperand(operand))).toInt(), i = i + 2)

      1 -> copy(b = (b xor operand), i = i + 2)

      2 -> copy(b = (comboOperand(operand) % 8), i = i + 2)

      3 -> if (a == 0) {
        copy(i = i + 2)
      } else {
        copy(i = operand)
      }

      4 -> copy(b = (b xor c), i = i + 2)

      5 -> copy(output = output + comboOperand(operand) % 8, i = i + 2)

      6 -> copy(b = (this.a / 2.0.pow(comboOperand(operand))).toInt(), i = i + 2)

      7 -> copy(c = (this.a / 2.0.pow(comboOperand(operand))).toInt(), i = i + 2)

      else -> {
        throw IllegalArgumentException("Invalid opcode: $inst")
      }
    }
  }

  private fun comboOperand(operand: Int): Int = when (operand) {
    0, 1, 2, 3 -> operand
    4 -> a
    5 -> b
    6 -> c
    else -> throw IllegalArgumentException("Invalid combo operand $operand")
  }
}