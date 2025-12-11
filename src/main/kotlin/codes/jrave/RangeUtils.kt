package codes.jrave

infix operator fun LongRange.contains(other: LongRange): Boolean =
    this.first <= other.first && other.last <= this.last


fun LongRange.overlaps(other: LongRange): Boolean {
    return (other in this) || (this in other) || this.first in other || this.last in other
}

fun Collection<LongRange>.allOverlap(): Boolean {
    val ranges = this.sortedBy { it.first }
    val pairs = ranges.dropLast(1) zip ranges.drop(1)
    for ((a, b) in pairs) {
        if (!a.overlaps(b)) {
            return false
        }
    }
    return true
}

fun Collection<LongRange>.groupIntoUnions(): List<LongRange> {
    val ranges = this.sortedBy { it.first }
    val unions = emptyList<LongRange>().toMutableList()
    for (range in ranges) {
        val overlapping = unions.filter { range.overlaps(it) }.toMutableList()
        unions.removeAll(overlapping)
        overlapping.add(range)
        val newUnion = overlapping.union()
        unions.add(newUnion)
    }

    return unions
}

fun Collection<LongRange>.union(): LongRange {
    if (!this.allOverlap()) {
        throw RangesDisjunctException()
    }

    val min = this.minOf { it.first }
    val max = this.maxOf { it.last }
    return min..max
}

class RangesDisjunctException : Throwable()
