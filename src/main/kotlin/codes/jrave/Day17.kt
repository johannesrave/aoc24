package codes.jrave

import arrow.core.memoize
import java.io.File
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

//  val day17BTest = Day17B("input/day17_test_1")
//  val day17BTestResult = day17BTest.solve()
//  println("Test result for Day17B: $day17BTestResult")
//  assert(day17BTestResult == 117440L)

  val day17B = Day17B("input/day17_input")
  val duration17B = measureTimeMillis {
    val solution = day17B.solve()
    println("Solution for Day17B: $solution")
    assert(solution == 164278764924605L)
  }
  println("Solution took $duration17B milliseconds")
}

// part A was relatively trivial, part B was more involved and has more comments.
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

// i stumbled on the solution a bit, and only rationized it after.
// after having gone overboard with optimizations such as memoization, bitwise operations and
// comparison by index of the required output to the intermediate results - and STILL getting
// minute-long runtimes without results - i realized that searching all candidates into the dozens
// of billions probably wasn't the point of the task.
// at some point, i printed all initializations of A that yield at least 8 matching entries to the
// required output. ignoring some noise, they were often 134217728 or 67108864 apart, or
// in other words 2^26 and 2^27, with all numbers inbetween not being very successful. so while
// i didn't understand the pattern at this point (and still don't quite do), it was clear that
// there WAS a pattern in regard to the differences between more successful As.
// i then took the first A that had more than 8 matches (23948989) as a starting point and
// incremented it by 2^27 for each trial, which quickly yielded the solution. this can be
// "optimized" up until 2^29, after which the solution is passed by and another (wrong) A is found
// that also yields the sequence.
// my rationalization of why stepping by some power of 2 makes sense, is that A is every only
// modified by powers of 2 during the algorithm. the "offset" from zero is a run that already gets
// some of the sequence right (like having lockpicked half the cylinder), and then the trick becomes
// finding the multiples of powers of 2 that "configure" the initial state correctly to
// produce the rest of the sequence.

data class Day17B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Long {

    val instructionMatches = Regex("""Program: (\S*)""").find(input)!!.groupValues[1]

    // we need an array of the input integers to be able to efficiently check whether our
    // intermediate results match their counterparts by index during the search.
    val instructionArray = instructionMatches.split(',').map { it.toLong() }.toTypedArray()

    val instructions = instructionArray.toList()
      .chunked(2) { (opcode, operand) -> Instruction(opcode.toInt(), operand.toInt()) }

    // this yields the first sequence with 8 matches, i use it as baseline
    var aInitial = 23948989L
    val bInitial = Regex("""Register B: (\d+)""").find(input)!!.groupValues[1].toLong()
    val cInitial = Regex("""Register C: (\d+)""").find(input)!!.groupValues[1].toLong()


    while (true) {
      // first found the solution with `1 shl 26`, as that was the difference between many numbers
      // yielding 8 or more matches.
      // afterwards manually tested to find the highest shiftCount to return the solution: 29
      aInitial += 1 shl 29

      var a = aInitial
      var b = bInitial
      var c = cInitial

      // instead of creating and appending to lots of lists, we can just test whether all
      // intermediate results match the ones from the input. this is also why we use an array for
      // instructionArray, to speed up checking its entries by index.
      var matchingInstructionIndex = -1

      var instructionCounter = 0

      while (instructionCounter <= instructions.lastIndex) {

        val (opcode, operand) = instructions[instructionCounter]

        instructionCounter++


        val combo = when {
          operand <= 3 -> operand.toLong()
          operand == 4 -> a
          operand == 5 -> b
          operand == 6 -> c
          else -> throw IllegalArgumentException("Invalid combo operand $operand")
        }

        when (opcode) {
          // adv
          0 -> a = dv(a, combo)

          // bdv
          6 -> b = dv(a, combo)

          // cdv
          7 -> c = dv(a, combo)

          // bxl
          1 -> b = b xor operand.toLong()

          // bst
          2 -> b = combo and 0b111 // keep only the lowest 3 bits == modulo 8

          // jnz
          3 -> if (a != 0L) instructionCounter = operand shr 1
          // `shr 1` halves the operand/instructionCounter to account for half-size of the
          // instructions-list when compared to the original AoC task. in the original, the indices
          // are for all comma separated values, so opcodes and operands. we use Instruction
          // objects containing both in one entry so only use half as many indices.

          // bxc
          4 -> b = b xor c

          // out
          5 -> {
            matchingInstructionIndex++
            val nextValue = combo and 0b111

            if (nextValue != instructionArray[matchingInstructionIndex]) break;

            if (matchingInstructionIndex >= 9) println("$aInitial:   ${instructionArray.slice(0..matchingInstructionIndex)}")

            if (matchingInstructionIndex == instructionArray.lastIndex) return aInitial;
          }
        }
      }
    }
  }
}

// the original task states:
// """
// The denominator is found by raising 2 to the power of the instruction's combo operand.
// (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
// The result of the division operation is truncated to an integer
// and then written to the A register.
// """
// this would give us
// `a / 2.0.pow(combo(operand)))`
// but we can use these shortcuts instead:
// raising 2 to some integer N can be achieved by leftshifting 1 by N instead,
// which is equivalent to N doublings of 1 and could be used to find the denominator.
// however since the division by 2 to the power of N is the same as then HALVING the
// operand N times, this can itself be achieved by RIGHTshifting the operand by N instead.
fun dv(a: Long, combo: Long): Long = a shr combo.toInt()

// memoization is in this case actually slower than doing the highly efficient bitwise calculations
val memoizedDv = ::dv.memoize()

data class Instruction(val opcode: Int, val operand: Int)