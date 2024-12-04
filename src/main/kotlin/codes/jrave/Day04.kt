package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day04ATest = Day04A("input/test_04")
  val testResult = day04ATest.solve().also { println(it) }
  assert(testResult == 18)

  val day04A = Day04A("input/input_04")
  val durationA = measureTimeMillis {
    val solution = day04A.solve()
    assert(solution == 2517)
    println("Solution for Day04A: $solution")
  }
  println("Solution took $durationA milliseconds")

//  val day04BTest = Day04B("input/test_04")
//  assert(day04BTest.solve("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))") == 48)
//
//  val day04B = Day04B("input/input_04")
//  val durationB = measureTimeMillis {
//    val solution = day04B.solve()
//    assert(solution == 90044227)
//    println("Solution for Day04B: $solution")
//  }
//  println("solution took $durationB milliseconds")

}

data class Day04A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)

    val diagonalBoard = board.rotate45Degrees()

    println(board.joinEach().joinToString("\n"))
    println(diagonalBoard.joinEach().joinToString("\n"))

    val xmasPattern = Regex("(?=XMAS)|(?=SAMX)")
    val boardAtAngles = listOf(
      board.joinEach(),
      board.transpose().joinEach(),
      diagonalBoard.joinEach(),
      diagonalBoard.transpose().joinEach()
    )
    return boardAtAngles.sumOf { boardAtAngle ->
      boardAtAngle.sumOf { rowString -> xmasPattern.findAll(rowString).count() }
    }
  }
}

data class Day04B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {

  fun solve(input: String = this.input): Int {
    val inactiveSegmentRegex = Regex("""don't\(\).*?(do\(\)|$)""")
    val multiplicationRegex = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

    val memory = input.replace("\n", "").split(inactiveSegmentRegex).joinToString("")


    return multiplicationRegex.findAll(memory).map { multiplication ->
      val (first, second) = multiplication.destructured
      first.toInt() * second.toInt()
    }.sum()
  }
}