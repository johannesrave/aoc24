package codes.jrave

import kotlin.math.pow

class Util {
}

fun main() {
  for (i in 1..5) {
    println(Truthtable.bits(i))
  }

  for (i in 1..3) {
    println(Truthtable.bits(i, 3.0))
  }
}


object Truthtable {
  val tables = mutableMapOf<Pair<Int, Double>, List<List<Boolean>>>()

  fun bits(bits: Int, variants: Double = 2.0): List<List<Boolean>> {
    tables.putIfAbsent(bits to variants, buildTable(bits, variants))
    return tables[bits to variants]!!
  }

  private fun buildTable(bits: Int, variants: Double = 2.0): List<List<Boolean>> {
    var i = 0b00000
    val rows = mutableListOf<List<Boolean>>()
    while (i < variants.pow(bits)) {
      val bitString = i.toString(variants.toInt()).padStart(bits, '0').also { println(it) }
      rows += bitString.map { if (it == '1') true else false }
      i++
    }
    return rows
  }
}