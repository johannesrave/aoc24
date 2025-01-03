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
    assert(solution > 166706)
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

      val firstExpansion = (listOf(NumericPadKey.A) + numericalKeys).expand()
      val secondExpansion = (listOf(A) + firstExpansion).expand()
      val thirdExpansion = (listOf(A) + secondExpansion).expand()

      println(numericalCodeString)
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

private fun List<Key>.expand(): List<DirectionalPadKey> {
  return windowed(2)
    .map { (from, to) ->
      val vector = to.pos - from.pos

      val horizontalDist = abs(vector.x)
      val verticalDist = abs(vector.y)

      val horizontalMove = if (vector.x > 0) RIGHT else LEFT
      val verticalMove = if (vector.y > 0) DOWN else UP

      val couldHitGapGoingRIGHT = from.pos.x == GAP.pos.x && to.pos.y == GAP.pos.y
      val couldHitGapGoingLeft = from.pos.y == GAP.pos.y && to.pos.x == GAP.pos.x

      when {
        couldHitGapGoingLeft -> {
          (verticalMove * verticalDist) + (horizontalMove * horizontalDist) + A
        }
        couldHitGapGoingRIGHT -> {
          (horizontalMove * horizontalDist) + (verticalMove * verticalDist) + A
        }
        horizontalMove == RIGHT && verticalMove == DOWN -> {
          (verticalMove * verticalDist) + (horizontalMove * horizontalDist) + A
        }
        else -> {
          (horizontalMove * horizontalDist) + (verticalMove * verticalDist) + A
        }
      }
    }.flatten()
}

interface Key { val pos: Pos }

enum class NumericPadKey(val c: Char) : Key {
  //@formatter:off
  SEVEN('7'),
  EIGHT('8'),
  NINE( '9'),
  FOUR( '4'),
  FIVE( '5'),
  SIX(  '6'),
  ONE(  '1'),
  TWO(  '2'),
  THREE('3'),
  ZERO( '0'),
  A(    'A'),
  GAP(  ' ')
  ;
  //@formatter:on

  val keyPad = parseBoard(
    """
    789
    456
    123
     0A
    """.trimIndent()
  )

  private val origin = keyPad.findFirstPosition('A')
  override val pos = keyPad.findFirstPosition(c) - origin

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
      ' ' -> GAP
      else -> throw IllegalArgumentException("Illegal char: $c")
    }
  }
}

enum class DirectionalPadKey(val c: Char) : Key {
  //@formatter:off
  UP(   '^'),
  A(    'A'),
  LEFT( '<'),
  DOWN( 'v'),
  RIGHT('>'),
  GAP(  ' ')
  //@formatter:on
  ;

  val keyPad = parseBoard(
    """
     ^A
    <v>
    """.trimIndent()
  )

  private val origin = keyPad.findFirstPosition('A')
  override val pos = keyPad.findFirstPosition(c) - origin

  init {
    println("$c: $pos")
  }

  override fun toString(): String = c.toString()

  operator fun times(n: Int) = List(n) { this }

  companion object {
    fun from(c: Char): DirectionalPadKey = when (c) {
      '^' -> UP
      'A' -> A
      '<' -> LEFT
      'v' -> DOWN
      '>' -> RIGHT
      ' ' -> GAP
      else -> throw IllegalArgumentException("Illegal char: $c")
    }
  }
}