package codes.jrave

import arrow.core.memoize
import org.w3c.dom.ProcessingInstruction
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
    assert(solution == "7,4,2,5,1,4,6,0,4")
  }
  println("Solution took $durationA milliseconds")

  val day17BTest = Day17B("input/day17_test_1")
  val day17BTestResult = day17BTest.solve()
  println("Test result for Day17B: $day17BTestResult")
  assert(day17BTestResult == 117440L)

  val day17B = Day17B("input/day17_input")
  val duration17B = measureTimeMillis {
    val solution = day17B.solve()
    println("Solution for Day17B: $solution")
    assert(solution > 162360291261L)
  }
  println("Solution took $duration17B milliseconds")
}


data class Day17A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): String {

    val instructions = Regex("""Program: (\S*)""").find(input)!!.groupValues[1].split(',')
      .chunked(2) { (opcode, operand) -> Instruction(opcode.toInt(), operand.toInt()) }

    var a = Regex("""Register A: (\d+)""").find(input)!!.groupValues[1].toInt()
    var b = Regex("""Register B: (\d+)""").find(input)!!.groupValues[1].toInt()
    var c = Regex("""Register C: (\d+)""").find(input)!!.groupValues[1].toInt()

    val output = mutableListOf<Int>()

    println(Regex("""Program: (\S*)""").find(input)!!.groupValues[1])

    var i = 0

    while ((i shr 1) <= instructions.lastIndex) {
      println("Instruction counter: $i")
      println("Registers: $a, $b, $c, $output")
      println(instructions[(i shr 1)])

      val (opcode, operand) = instructions[(i shr 1)]

      val combo = when {
        operand <= 3 -> operand
        operand == 4 -> a
        operand == 5 -> b
        operand == 6 -> c
        else -> throw IllegalArgumentException("Invalid combo operand $operand")
      }

      when (opcode) {
        // adv
        0 -> {
          a = a shr (combo)
          i += 2
        }

        // bdv
        6 -> {
          b = a shr (combo)
          i += 2
        }

        // cdv
        7 -> {
          c = a shr (combo)
          i += 2
        }

        // bxl
        1 -> {
          b = (b xor operand)
          i += 2
        }

        // bst
        2 -> {
          b = combo and 0b111
          i += 2
        }

        // jnz
        3 -> {
          if (a == 0)
            i += 2;
          else
            i = operand;
        }

        // bxc
        4 -> {
          b = (b xor c)
          i += 2
        }

        // out
        5 -> {
          output.addLast(combo and 0b111)
          i += 2
        }
      }
    }

    return output.joinToString(",")
  }
}

data class Day17B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Long {

    val programString = Regex("""Program: (\S*)""").find(input)!!.groupValues[1]
    val programValues = programString.split(',').map { it.toInt() }.toTypedArray()

    val instructions = Regex("""Program: (\S*)""").find(input)!!.groupValues[1].split(',')
      .chunked(2) { (opcode, operand) -> Instruction(opcode.toInt(), operand.toInt()) }

    // 10604411317
    // 156723146685
    var aInitial = 0L
    val bInitial = Regex("""Register B: (\d+)""").find(input)!!.groupValues[1].toLong()
    val cInitial = Regex("""Register C: (\d+)""").find(input)!!.groupValues[1].toLong()


    while (true) {
      aInitial++

      var a = aInitial
      var b = bInitial
      var c = cInitial

      val output = mutableListOf<Int>()

//      println(Regex("""Program: (\S*)""").find(input)!!.groupValues[1])

      var i = 0

      while ((i shr 1) <= instructions.lastIndex) {
//        println("Registers: $a, $b, $c, $output")
//        println(instructions[(i shr 1)])

        val (opcode, operand) = instructions[(i shr 1)]

        val combo = when {
          operand <= 3 -> operand.toLong()
          operand == 4 -> a
          operand == 5 -> b
          operand == 6 -> c
          else -> throw IllegalArgumentException("Invalid combo operand $operand")
        }

        when (opcode) {
          // adv
          0 -> {
            a = a shr (combo).toInt()
            i += 2
          }

          // bdv
          6 -> {
            b = a shr (combo).toInt()
            i += 2
          }

          // cdv
          7 -> {
            c = a shr (combo).toInt()
            i += 2
          }

          // bxl
          1 -> {
            b = (b xor operand.toLong())
            i += 2
          }

          // bst
          2 -> {
            b = combo and 0b111
            i += 2
          }

          // jnz
          3 -> {
            if (a == 0L)
              i += 2;
            else
              i = operand;
          }

          // bxc
          4 -> {
            b = (b xor c)
            i += 2
          }

          // out
          5 -> {
            val nextValue = (combo and 0b111).toInt()
            if (nextValue != programValues[output.size]) {
              break
            }
            output.addLast(nextValue)
            if (output.size >= 8) println("$aInitial:     $output");
            i += 2
          }
        }
      }
      if (output.size == programValues.size) return aInitial;
    }
  }
}

data class Instruction(val opcode: Int, val operand: Int)