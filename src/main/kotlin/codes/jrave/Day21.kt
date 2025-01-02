package codes.jrave

import codes.jrave.DirectionalPadKey.*
import java.io.File
import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
  val day21ATest = Day21A("input/day21_test")
  val day21ATestResult = day21ATest.solve()
  println("Test result for Day21A: $day21ATestResult")
  assert(day21ATestResult == 126384)

  val day21A = Day21A("input/day21_input")
  val durationA = measureTimeMillis {
    val solution = day21A.solve()
    println("Solution for Day21A: $solution")
    assert(solution < 170862)
  }
  println("Solution took $durationA milliseconds")

  val day21BTest = Day21B("input/day21_test")
  val day21BTestResult = day21BTest.solve()
  println("Test result for Day21B: $day21BTestResult")
  assert(day21BTestResult == 1)

  val day21B = Day21B("input/day21_input")
  val duration21B = measureTimeMillis {
    val solution = day21B.solve()
    println("Solution for Day21B: $solution")
    assert(solution == 1)
  }
  println("Solution took $duration21B milliseconds")
}

data class Day21A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val solutionsPerLine = input.split("\n").map { numericalCodeString ->
      val numericalKeys = numericalCodeString.map { c -> NumericPadKey.from(c) }
//      val firstVectors = (listOf(NumericPadKey.A) + numericalKeys).getVectorsOnNumericPad()
//      val firstExpansion = firstVectors.expand()
//
//      val secondVectors = (listOf(A) + firstExpansion).getVectorsOnDirectionalPad()
//      val secondExpansion = secondVectors.expand()
//
//      val thirdVectors = (listOf(A) + secondExpansion).getVectorsOnDirectionalPad()
//      val thirdExpansion = thirdVectors.expand()

      val firstExpansion = (listOf(NumericPadKey.A) + numericalKeys).expand()

      val secondExpansion = (listOf(A) + firstExpansion).expand()

      val thirdExpansion = (listOf(A) + secondExpansion).expand()

      println(firstExpansion.joinToString(separator = ""))
      println(secondExpansion.joinToString(separator = ""))
      println(thirdExpansion.joinToString(separator = ""))

      println("" + numericalCodeString.dropLast(1).toInt() + " * " + thirdExpansion.size)

      numericalCodeString.dropLast(1).toInt() * thirdExpansion.size
    }
    return solutionsPerLine.sum()
  }
}

data class Day21B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    return 0
  }
}

//private fun List<DirectionalPadKey>.getVectorsOnDirectionalPad(): List<Pos> =
//  windowed(2) { (a, b) -> b.pos - a.pos }
//
//private fun List<NumericPadKey>.getVectorsOnNumericPad(): List<Pos> =
//  windowed(2) { (a, b) -> b.pos - a.pos }

//private fun List<Pos>.expand(): List<DirectionalPadKey> = flatMap { vec ->
//  val horizontalDirection = when {
//    vec.x > 0 -> RIGHT
//    else -> LEFT
//  }
//
//  val verticalDirection = when {
//    vec.y > 0 -> DOWN
//    else -> UP
//  }
//
//  val horizontalMoves = List(abs(vec.x)) { horizontalDirection }
//  val verticalMoves = List(abs(vec.y)) { verticalDirection }
//
//  when {
//    horizontalDirection == LEFT -> {
//      (verticalMoves + horizontalMoves + A).toMutableList().also {
//        // swap LEFT away from A if
//        if (it.size > 2 && it[lastIndex - 1] == LEFT) {
//          it[lastIndex - 1] = it[lastIndex - 2]
//          it[lastIndex - 2] = LEFT
//        }
//      }
//    }
//
//    else -> horizontalMoves + verticalMoves + A
//  }
//}

private fun List<Key>.expand(): List<DirectionalPadKey> {
  val gapPos = Pos(0, -2)

  return windowed(2)
    .map { (from, to) ->
      val vector = to.pos - from.pos

      val horizontalDirection = if (vector.x > 0) RIGHT else LEFT
      val verticalDirection = if (vector.y > 0) DOWN else UP

      val horizontalMoves = List(abs(vector.x)) { horizontalDirection }
      val verticalMoves = List(abs(vector.y)) { verticalDirection }

      val couldHitGap = from.pos.y == gapPos.y && (from.pos.x + vector.x == gapPos.x)

      if (couldHitGap) {
//        verticalMoves.take(1) + horizontalMoves + verticalMoves.drop(1) + A
        verticalMoves + horizontalMoves + A
      } else {
        horizontalMoves + verticalMoves + A
      }


    }.flatten()
}


fun <T> weave(listA: List<T>, listB: List<T>): List<T> {
  val list = mutableListOf<T>()
  val (longerList, shorterList) = if (listA.size >= listB.size) listA to listB else listB to listA
  for (i in longerList.indices) {
    list.add(longerList[i])
    if (i <= shorterList.lastIndex) {
      list.add(shorterList[i])
    }
  }
  return list
}

interface Key {
  val pos: Pos
}

enum class NumericPadKey(override val pos: Pos, val c: Char) : Key {
  //@formatter:off
  SEVEN(  Pos(-3, -2), '7'),
  EIGHT(  Pos(-3, -1), '8'),
  NINE(   Pos(-3,  0), '9'),
  FOUR(   Pos(-2, -2), '4'),
  FIVE(   Pos(-2, -1), '5'),
  SIX(    Pos(-2,  0), '6'),
  ONE(    Pos(-1, -2), '1'),
  TWO(    Pos(-1, -1), '2'),
  THREE(  Pos(-1,  0), '3'),
  ZERO(   Pos( 0, -1), '0'),
  A(      Pos( 0,  0), 'A'),
  ;
  //@formatter:on

  override fun toString(): String = c.toString()

  companion object {
    fun from(c: Char): NumericPadKey = when (c) {
      'A' -> A
      '0' -> ZERO
      '1' -> ONE
      '2' -> TWO
      '3' -> THREE
      '4' -> FOUR
      '5' -> FIVE
      '6' -> SIX
      '7' -> SEVEN
      '8' -> EIGHT
      '9' -> NINE
      else -> throw IllegalArgumentException("Illegal char: $c")
    }
  }
}

enum class DirectionalPadKey(override val pos: Pos, val c: Char) : Key {
  UP(Pos(0, -1), '^'),
  A(Pos(0, 0), 'A'),
  LEFT(Pos(1, -2), '<'),
  DOWN(Pos(1, -1), 'v'),
  RIGHT(Pos(1, 0), '>'),
  ;

  val dirPad = parseBoard(
    """
     ^A
    <v>
    """.trimIndent()
  )

  val parsedPos = dirPad.findFirstPosition(c)

  init {
    println(parsedPos)
  }

  override fun toString(): String = c.toString()

  companion object {
    fun from(c: Char): DirectionalPadKey = when (c) {
      '^' -> UP
      'A' -> A
      '<' -> LEFT
      'v' -> DOWN
      '>' -> RIGHT
      else -> throw IllegalArgumentException("Illegal char: $c")
    }
  }
}