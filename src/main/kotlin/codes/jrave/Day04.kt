package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day04ATest = Day04A("input/test_04")
  val day04ATestResult = day04ATest.solve().also { println(it) }
  assert(day04ATestResult == 18)

  val day04A = Day04A("input/input_04")
  val durationA = measureTimeMillis {
    val solution = day04A.solve()
    assert(solution == 2517)
    println("Solution for Day04A: $solution")
  }
  println("Solution took $durationA milliseconds")

  val day04BTest = Day04B("input/test_04")
  val day04BTestResult = day04BTest.solve().also { println(it) }
  assert(day04BTestResult == 9)

  val day04B = Day04B("input/input_04")
  val duration04B = measureTimeMillis {
    val solution = day04B.solve()
//    assert(solution == 2517)
    println("Solution for Day04B: $solution")
  }
  println("Solution took $duration04B milliseconds")
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