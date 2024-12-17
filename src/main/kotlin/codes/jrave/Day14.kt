package codes.jrave

import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
//  val day14ATest = Day14A("input/day14_test")
//  val day14ATestResult = day14ATest.solve(rows = 7, columns = 11, seconds = 100)
//  println("Test result for Day14A: $day14ATestResult")
//  assert(day14ATestResult == 12)
//
//  val day14A = Day14A("input/day14_input")
//  val durationA = measureTimeMillis {
//    val solution = day14A.solve(rows = 103, columns = 101, seconds = 100)
//    println("Solution for Day14A: $solution")
//    assert(solution > 500)
//  }
//  println("Solution took $durationA milliseconds")

  val day14B = Day14B("input/day14_input")
  val duration14B = measureTimeMillis {
    day14B.solve(rows = 103, columns = 101, maxSeconds = 7132)
  }
  println("Solution took $duration14B milliseconds")
}


data class Day14A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, rows: Int, columns: Int, seconds: Int): Int {
    val robotRegex = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")
    val robotPosToVelocity = parseRobotPosToVelocity(input, robotRegex)

    val positionsAfterMovement = moveRobotsForSeconds(robotPosToVelocity, rows, columns, seconds)

    val quadrants = splitIntoQuadrants(rows, columns, positionsAfterMovement)

    val board = emptyBoard(rows, columns, ' ')
    println(
      board
        .markPositions(positionsAfterMovement, 'X')
        .markPositions(quadrants[0], 'A')
        .markPositions(quadrants[1], 'B')
        .markPositions(quadrants[2], 'C')
        .markPositions(quadrants[3], 'D')
        .toPrintString()
    )

    return quadrants.fold(1) { acc, quad -> acc * (quad.size) }
  }

}


data class Day14B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input, rows: Int, columns: Int, maxSeconds: Int): Int {
    val robotRegex = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")
    val robotPosToVelocity = parseRobotPosToVelocity(input, robotRegex)

    val initialPositions = robotPosToVelocity.map { (pos) -> pos }.toSet()
    for (i in 0..maxSeconds) {
      val positionsAfterMovement =
        moveRobotsForSeconds(robotPosToVelocity, rows, columns, i).toSet()

      val board = emptyBoard(rows, columns, ' ')

//      if ((i - 62) % 101 == 0) {
//        println("Board after $i seconds:")
//        println(board.markPositions(positionsAfterMovement, 'X').toPrintString())
//      }

      if (initialPositions == positionsAfterMovement) println("repeated state after $i seconds");
      if (i == 7132) {
        println("Board after $i seconds:")
        println(board.markPositions(positionsAfterMovement, 'X').toPrintString())
      }
    }

    return 0
  }

}

private fun parseRobotPosToVelocity(
  input: String,
  robotRegex: Regex
): List<Pair<Pos, Velocity>> {
  val robotPosToVelocity = input.split("\n").map { line ->
    robotRegex.find(line)!!.destructured.let { (px, py, vx, vy) ->
      Pos(py.toInt(), px.toInt()) to Velocity(vy.toInt(), vx.toInt())
    }
  }
  return robotPosToVelocity
}

private fun moveRobotsForSeconds(
  robotPosToVelocity: List<Pair<Pos, Velocity>>,
  rows: Int,
  columns: Int,
  n: Int
): List<Pos> {
  val positionsAfterMovement = robotPosToVelocity
    .map { (pos, velocity) -> pos + (velocity * n) }
    .map { pos -> Pos(pos.y % rows, pos.x % columns) }
    .map { pos ->
      val x = if (pos.x < 0) pos.x + columns else pos.x
      val y = if (pos.y < 0) pos.y + rows else pos.y
      Pos(y, x)
    }
  return positionsAfterMovement
}

private fun splitIntoQuadrants(
  rows: Int,
  columns: Int,
  positionsAfterMovement: List<Pos>
): List<List<Pos>> {
  val topHalf = (0..<(rows / 2))
  val bottomHalf = ((rows / 2) + 1..<rows)
  val leftHalf = (0..<(columns / 2))
  val rightHalf = ((columns / 2) + 1..<columns)

  val quadrants = listOf(
    topHalf to leftHalf,
    topHalf to rightHalf,
    bottomHalf to leftHalf,
    bottomHalf to rightHalf
  ).map { (yRange, xRange) -> positionsAfterMovement.filter { (y, x) -> y in yRange && x in xRange } }

  return quadrants
}