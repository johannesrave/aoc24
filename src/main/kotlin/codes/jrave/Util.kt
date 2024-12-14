package codes.jrave

import kotlin.math.pow

class Util {
}

fun main() {
  for (i in 1..5) {
    println(Truthtable.bits(i))
  }
}


object Truthtable {
  val tables = mutableMapOf<Int, List<List<Boolean>>>()

  fun bits(bits: Int): List<List<Boolean>> {
    tables.putIfAbsent(bits, buildTable(bits))
    return tables[bits]!!
  }

  private fun buildTable(bits: Int): List<List<Boolean>> {
    var i = 0b00000
    val rows = mutableListOf<List<Boolean>>()
    while (i < (2.0).pow(bits)) {
      val bitString = i.toString(2).padStart(bits, '0')
      rows += bitString.map { if (it == '1') true else false }
      i++
    }
    return rows
  }
}