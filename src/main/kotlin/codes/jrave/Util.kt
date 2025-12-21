package codes.jrave

import java.math.BigInteger
import kotlin.math.pow

fun main() {

    val variationTable = VariationsTable
    for (i in 1..5) {
        println(variationTable[i])
    }

    for (i in 1..3) {
        println(variationTable[i, 3.0])
    }
}


object VariationsTable {
    private val tables = mutableMapOf<Pair<Int, Double>, List<IntArray>>()

    operator fun get(bits: Int, variants: Double = 2.0): List<IntArray> {
        tables.computeIfAbsent(bits to variants) { buildTable(bits, variants) }
        return tables[bits to variants]!!
    }

    private fun buildTable(bits: Int, variants: Double = 2.0): List<IntArray> {
        if (variants > 9.0) throw IllegalArgumentException("Variants can't be more than 9")
        println("Building table for $bits bits and $variants variants")
        var i = 0b0
        val rows = mutableListOf<IntArray>()
        while (i < variants.pow(bits)) {
            val bitString = i.toString(variants.toInt()).padStart(bits, '0')
            rows += bitString.map { it.toString().toInt() }.toIntArray()
            i++
        }
        return rows
    }
}

fun List<BigInteger>.sum(): BigInteger = this.reduce(BigInteger::plus)

fun String.splitOnIndices(indices: List<Int>): List<String> {
    return this.mapIndexed { i, c ->
        if (i in indices) '%' else c
    }.joinToString("").split("%")
}

fun buildCombinations(length: Int, items: Collection<Int>): List<IntArray> {
    val combinations = emptyList<IntArray>().toMutableList()
    val queue = items.map { listOf(it) }.toMutableList()
    while (queue.isNotEmpty()) {
        val combination = queue.removeLast()
        if (combination.size < length) {
            for (item in items) {
                queue.add(combination + item)
            }
        } else {
            combinations.add(combination.toIntArray())
        }
    }

    return combinations
}