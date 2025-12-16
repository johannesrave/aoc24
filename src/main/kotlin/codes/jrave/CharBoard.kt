package codes.jrave

import codes.jrave.Direction.*
import java.util.*
import kotlin.math.abs

const val NULL_CHAR: Char = '\u0000'

fun parseBoard(input: String): Array<CharArray> =
    input.split("\n").filter { it.isNotEmpty() }.map { line -> line.toCharArray() }.toTypedArray()

fun emptyBoard(dim: Int, nullChar: Char? = null): Array<CharArray> =
    emptyBoard(dim, dim, nullChar)

fun emptyBoard(rows: Int, cols: Int, nullChar: Char? = null): Array<CharArray> =
    Array(rows) { nullChar?.let { CharArray(cols).also { it.fill(nullChar) } } ?: CharArray(cols) }


fun emptyBoard(board: Array<CharArray>, nullChar: Char? = null): Array<CharArray> {
    val rows = board.size
    val cols = board.first().size
    return Array(rows) {
        nullChar?.let { CharArray(cols).also { it.fill(nullChar) } }
            ?: CharArray(cols)
    }
}

fun Array<CharArray>.toPrintString(): String = joinEach().joinToString("\n")

fun Array<CharArray>.transpose(): Array<CharArray> =
    this.first().mapIndexed { x, _ -> this.map { it[x] }.toCharArray() }.toTypedArray()

fun Array<IntArray>.transpose(): Array<IntArray> =
    this.first().mapIndexed { x, _ -> this.map { it[x] }.toIntArray() }.toTypedArray()

fun Array<LongArray>.transpose(): Array<LongArray> =
    this.first().mapIndexed { x, _ -> this.map { it[x] }.toLongArray() }.toTypedArray()

fun Array<CharArray>.mirrorX(): Array<CharArray> =
    this.map { row -> row.reversedArray() }.toTypedArray()

fun Array<CharArray>.rotate45Degrees(nullChar: Char? = null): Array<CharArray> {
    val xDim = first().lastIndex
    val yDim = lastIndex
    val dimensionOfDiagonalBoard = xDim + yDim + 2
    val diagonalBoard = emptyBoard(dimensionOfDiagonalBoard, nullChar)

    for (y in 0..yDim) {
        for (x in 0..xDim) {
            val (newX, newY) = (x + y) to (yDim + y - x + 1)

            diagonalBoard[newY][newX] = this[y][x]
        }
    }
    return diagonalBoard
}

fun Array<CharArray>.joinEach(filterChars: Set<Char> = setOf(NULL_CHAR)): List<String> =
    this.map { row -> row.filter { it !in filterChars }.joinToString("") }

fun Array<CharArray>.shrinkWrap(nullChar: Char? = NULL_CHAR): Array<CharArray> = this
    .transpose()
    .dropWhile { row -> row.all { it == nullChar } }
    .dropLastWhile { row -> row.all { it == nullChar } }
    .toTypedArray()
    .transpose()
    .dropWhile { row -> row.all { it == nullChar } }
    .dropLastWhile { row -> row.all { it == nullChar } }
    .toTypedArray()

fun Array<CharArray>.findFirstPosition(char: Char): Pos = findFirstPosition(setOf(char))

fun Array<CharArray>.findFirstPosition(chars: Set<Char>): Pos {
    return mapIndexedNotNull { y, row ->
        if (chars.any { it in row }) {
            val x = row.indexOfFirst { it in chars }
            Pos(y, x)
        } else null
    }.first()
}

fun Array<CharArray>.findPositions(char: Char): Set<Pos> = findPositions(setOf(char))

fun Array<CharArray>.findPositions(chars: Set<Char>): Set<Pos> =
    flatMapIndexed { y, row ->
        row
            .mapIndexed { x, c -> if (c in chars) Pos(y, x) else null }
            .filterNotNull()
    }.toSet()

operator fun Array<IntArray>.plus(other: Array<IntArray>): Array<IntArray> {
    if (this.size != other.size ||
        this.any { it.size != this.first().size } ||
        other.any { it.size != this.first().size }
    ) {
        throw IllegalArgumentException("Array size mismatch")
    }

    return indices.map { y ->
        first().indices.map { x ->
            this[Pos(y, x)] + other[Pos(y, x)]
        }.toIntArray()
    }.toTypedArray()
}

fun Array<IntArray>.printWithPadding(paddedDecimals: Int = 6): Unit = forEach { row ->
    row.joinToString(" ") { it.toString().padStart(paddedDecimals) }
        .also { println(it) }
}


operator fun Array<IntArray>.get(pos: Pos): Int = this[pos.y][pos.x]
operator fun Array<CharArray>.set(pos: Pos, char: Char) {
    this[pos.y][pos.x] = char
}

operator fun Array<CharArray>.get(pos: Pos): Char = this[pos.y][pos.x]
operator fun Array<IntArray>.set(pos: Pos, int: Int) {
    this[pos.y][pos.x] = int
}

operator fun <T> Array<Array<T>>.get(pos: Pos) = this[pos.y][pos.x]
operator fun <T> Array<Array<T>>.set(pos: Pos, value: T) {
    this[pos.y][pos.x] = value
}

infix operator fun Array<out CharArray>.contains(pos: Pos): Boolean =
    (pos.y in this.indices && pos.x in this.first().indices)


fun Array<CharArray>.deepClone(): Array<CharArray> = Array(size) { get(it).clone() }

fun Array<CharArray>.directNeighbours(pos: Pos): Set<Pos> {
    val xLength = this.first().lastIndex
    val yLength = this.lastIndex
    return setOf(
        pos + N, pos + W, pos + E, pos + S,
    ).filter {
        // Only return positions that are actually on the board
        it.x in 0..xLength && it.y in 0..yLength
    }.toSet()
}

fun Array<CharArray>.allNeighbours(pos: Pos): Set<Pos> {
    val xLength = this.first().lastIndex
    val yLength = this.lastIndex
    return setOf(
        //@formatter:off
        pos + N + W, pos + N, pos + N + E,
        pos     + W,          pos     + E,
        pos + S + W, pos + S, pos + S + E,
        //@formatter:on
    ).filter {
        // Only return positions that are actually on the board
        it.x in 0..xLength && it.y in 0..yLength
    }.toSet()
}


fun Array<CharArray>.markPositions(positions: Collection<Pos>, char: Char = 'O'): Array<CharArray> =
    deepClone().also { board -> positions.forEach { pos -> board[pos] = char } }


fun Array<CharArray>.markSteps(steps: List<Step>, overrideChar: Char? = null): Array<CharArray> {
    val board_ = deepClone()
    steps.forEach { step -> board_[step.pos] = overrideChar ?: step.dir.c }
    board_[steps.first().pos] = 'X'
    return board_
}

// omething dijkstra this way comes
fun Array<CharArray>.minimalDistanceBoard(
    startPos: Pos,
    endPos: Pos,
    wall: Char = '#'
): Array<IntArray> {
    val minimalDistanceBoard = Array(size) { IntArray(first().size) { Int.MAX_VALUE } }
    minimalDistanceBoard[startPos] = 0

    val queue = PriorityQueue { posA: Pos, posB: Pos ->
        posA.manhattanDistance(endPos) - posB.manhattanDistance(endPos)
    }

    queue.add(startPos)

    while (queue.isNotEmpty()) {
        val lastPos = queue.remove()
        val cost = minimalDistanceBoard[lastPos] + 1
        for (dir in Direction.entries) {
            val lookAheadPos = lastPos + dir
            when {
                lookAheadPos !in this -> continue
                this[lookAheadPos] == wall -> continue
                cost >= minimalDistanceBoard[lookAheadPos] -> continue
                else -> {
                    minimalDistanceBoard[lookAheadPos] = cost
                    if (lookAheadPos != endPos) queue += lookAheadPos
                }
            }
        }
    }
    return minimalDistanceBoard
}

data class Pos(val y: Int, val x: Int) {
    operator fun plus(direction: Direction): Pos = Pos(y + direction.y, x + direction.x)
    operator fun plus(velocity: Velocity): Pos = Pos(y + velocity.y, x + velocity.x)
    operator fun minus(other: Pos): Pos = Pos(y - other.y, x - other.x)
    fun manhattanDistance(otherPos: Pos) = (abs(x - otherPos.x) + abs(y - otherPos.y))
}

enum class Direction(
    val x: Int,
    val y: Int,
    val c: Char,
    val horizontal: Boolean,
    val vertical: Boolean
) {
    N(0, -1, '^', false, true),
    E(1, 0, '>', true, false),
    S(0, 1, 'v', false, true),
    W(-1, 0, '<', true, false), ;

    fun turnClockwise() = turnBy(1)
    fun turnCounterClockwise() = turnBy(3)
    fun flip(): Direction = turnBy(2)
    fun allExceptBackwards(): List<Direction> = listOf(turnClockwise(), this, turnCounterClockwise())

    private fun turnBy(n: Int) = entries[(this.ordinal + n) % entries.size]

    companion object {
        val directionMarkers = entries.map { it.c }.toSet()
        fun from(c: Char): Direction? = entries.find { it.c == c }
    }
}

data class Step(val pos: Pos, val dir: Direction)

data class Velocity(val y: Int, val x: Int) {
    operator fun times(factor: Int): Velocity = Velocity(y * factor, x * factor)
}


fun main() {
    val testBoard = emptyBoard(3)
    testBoard[1][1] = 'A'

    println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

    println("shrinkwrapped:")
    println(testBoard.shrinkWrap().joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })

    println("original board unchanged:")
    println(testBoard.joinToString("\n") { it.joinToString("").replace(NULL_CHAR, '_') })
}