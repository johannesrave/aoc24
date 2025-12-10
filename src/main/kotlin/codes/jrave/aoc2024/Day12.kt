package codes.jrave.aoc2024

import codes.jrave.Direction
import codes.jrave.Direction.*
import codes.jrave.Pos
import codes.jrave.contains
import codes.jrave.emptyBoard
import codes.jrave.get
import codes.jrave.markPositions
import codes.jrave.parseBoard
import codes.jrave.set
import codes.jrave.toPrintString
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
  val day12ATest = Day12A("input/2024/day12_test")
  val day12ATestResult = day12ATest.solve()
  println("Test result for Day12A: $day12ATestResult")
  assert(day12ATestResult == 1930)

  val day12A = Day12A("input/2024/day12_input")
  val durationA = measureTimeMillis {
    val solution = day12A.solve()
    println("Solution for Day12A: $solution")
    assert(solution == 1471452)
  }
  println("Solution took $durationA milliseconds")

  val day12BTest = Day12B("input/2024/day12_test")
  val day12BTestResult = day12BTest.solve()
  println("Test result for Day12B: $day12BTestResult")
  assert(day12BTestResult == 1206)

  val day12B = Day12B("input/2024/day12_input")
  val duration12B = measureTimeMillis {
    val solution = day12B.solve()
    println("Solution for Day12B: $solution")
    assert(solution == 863366)
  }
  println("Solution took $duration12B milliseconds")
}


data class Day12A(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val regions = parseRegions(board)
    val plotsToPerimeter = countPlotsAndPerimetersPerRegion(regions)

    return plotsToPerimeter.sumOf { (plot, perimeter) -> plot * perimeter }
  }
}

data class Day12B(
  val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
  fun solve(input: String = this.input): Int {
    val board = parseBoard(input)
    val regions = parseRegions(board)
    val plotsToPerimeter = countPlotsAndStraightPerimetersPerRegion(regions)

    return plotsToPerimeter.sumOf { (plot, perimeter) -> plot * perimeter }
  }
}


private fun parseRegions(board: Array<CharArray>): MutableSet<MutableSet<Pos>> {
  val regions = mutableSetOf<MutableSet<Pos>>()

  val visited = emptyBoard(board)
  val visitedMarker = 'X'

  board.forEachIndexed { y, row ->
    row.forEachIndexed traverseRow@{ x, c ->
      val probePos = Pos(y, x)
      if (visited[probePos] == visitedMarker) return@traverseRow
      visited[probePos] = visitedMarker

//      println("parsing region starting at $probePos")

      val region = mutableSetOf<Pos>()
      val queue = mutableListOf(probePos)

      while (queue.isNotEmpty()) {
        val currentPos = queue.removeFirst()
        region.add(currentPos)
//        println("adding to region: $currentPos")

        for (dir in Direction.entries) {
          val lookAheadPos = currentPos + dir
          if (lookAheadPos in board && board[lookAheadPos] == c && visited[lookAheadPos] != 'X') {
            queue.add(lookAheadPos)
            visited[lookAheadPos] = visitedMarker
          }
        }
      }
      regions.add(region)
    }
  }

  printRegions(board, regions)

  return regions
}

private fun printRegions(
  board: Array<CharArray>,
  regions: MutableSet<MutableSet<Pos>>
) {
  var regionMarker = 'A'
  var markedBoard = emptyBoard(board, '.')
  for (region in regions) {
    markedBoard = markedBoard.markPositions(region, regionMarker)
    regionMarker += 1
  }
  println(markedBoard.toPrintString())
}

private fun countPlotsAndPerimetersPerRegion(regions: MutableSet<MutableSet<Pos>>): List<Pair<Int, Int>> {
  val plotsToPerimeter = regions.map { region ->
    region.size to region.fold(0) { acc, pos ->
      Direction.entries.filterNot { dir -> (pos + dir) in region }.size + acc
    }
  }
  return plotsToPerimeter
}


private fun countPlotsAndStraightPerimetersPerRegion(regions: MutableSet<MutableSet<Pos>>): List<Pair<Int, Int>> {
  val plotsToPerimeter = regions.map { region ->
    val edges = findAllEdges(region).toMutableSet()
//    println(edges)

    val straightEdges = mutableListOf<List<Edge>>()

    edges.forEach { edge ->
      if (straightEdges.any { edge in it }) return@forEach;

      val (pos, dir) = edge
      val edgesOnSameRowOrColumn = edges
        .filter { (_, otherDir) -> dir == otherDir }
        .filter { (otherPos, _) ->
          when (dir) {
            N, S -> pos.y == otherPos.y
            E, W -> pos.x == otherPos.x
          }
        }
      val straightEdge = mutableListOf(edge)

      val lookAheadDir = dir.turnClockwise()

      val lookAheadPos = pos + lookAheadDir
      var lookAheadEdge = lookAheadPos to dir

      while (lookAheadEdge in edgesOnSameRowOrColumn) {
        straightEdge.addLast(lookAheadEdge)
        lookAheadEdge = (lookAheadEdge.first + lookAheadDir) to dir
      }

      val lookBehindDir = dir.turnCounterClockwise()

      val lookBehindPos = pos + lookBehindDir
      var lookBehindEdge = lookBehindPos to dir

      while (lookBehindEdge in edgesOnSameRowOrColumn) {
        straightEdge.addFirst(lookBehindEdge)
        lookBehindEdge = (lookBehindEdge.first + lookBehindDir) to dir
      }

      println(straightEdge)

      straightEdges.add(straightEdge)

    }

    region.size to straightEdges.size

  }
  return plotsToPerimeter
}

fun findAllEdges(region: MutableSet<Pos>): List<Edge> {
  val edges = mutableListOf<Edge>()

  for (pos in region) {
    for (dir in Direction.entries) {
      if (pos + dir !in region) {
        edges += pos to dir
      }
    }
  }
  return edges
}

typealias Edge = Pair<Pos, Direction>