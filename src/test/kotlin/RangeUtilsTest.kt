import codes.jrave.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse


class RangeUtilsTest {
    @Test
    fun `contains should return true when outer range clearly encompasses inner`() {
        val outerRange = 2L..10L
        val innerRange = 4L..6L

        assert(outerRange.contains(innerRange))
    }

    @Test
    fun `contains should return true when outer range has the same limits as inner`() {
        val outerRange = 2L..10L

        assert(outerRange.contains(outerRange))
    }

    @Test
    fun `contains should return false when outer range is inside inner`() {
        val outerRange = 4L..6L
        val innerRange = 2L..10L

        assertFalse(outerRange.contains(innerRange))
    }

    @Test
    fun `contains should return false when outer range only overlaps inner`() {
        val outerRange = 2L..6L
        val innerRange = 4L..8L

        assertFalse(outerRange.contains(innerRange))
    }

    @Test
    fun `contains should return false when outer range doesnt overlap inner`() {
        val outerRange = 2L..4L
        val innerRange = 6L..8L

        assertFalse(outerRange.contains(innerRange))
    }

    @Test
    fun `overlaps should return true when ranges overlap`() {
        val outerRange = 2L..6L
        val innerRange = 4L..8L

        assert(outerRange.overlaps(innerRange))
        assert(innerRange.overlaps(outerRange))
    }

    @Test
    fun `overlaps should return true when outerRange encompasses innerRange`() {
        val outerRange = 2L..10L
        val innerRange = 4L..8L

        assert(outerRange.overlaps(innerRange))
        assert(innerRange.overlaps(outerRange))
    }

    @Test
    fun `union combines overlapping ranges`() {
        val firstRange = 2L..8L
        val lastRange = 4L..10L

        val combinedRange = listOf(firstRange, lastRange).union()

        assert(combinedRange.first == firstRange.first)
        assert(combinedRange.last == lastRange.last)
    }

    @Test
    fun `union doesn't combine disjunct ranges`() {
        val firstRange = 2L..4L
        val lastRange = 6L..10L

        val ranges = listOf(firstRange, lastRange)

        assertThrows<RangesDisjunctException> { ranges.union() }
    }

    @Test
    fun `allOverlap returns true if all ranges overlap`() {
        val ranges = listOf(
            0L..3L,
            2L..6L,
            6L..10L,
        )

        assert(ranges.allOverlap())
    }

    @Test
    fun `allOverlap returns false if not all ranges overlap`() {
        val ranges = listOf(
            0L..3L,
            6L..10L,
        )

        assertFalse(ranges.allOverlap())
    }

    @Test
    fun `allOverlap returns true if all unsorted ranges overlap`() {
        val ranges = listOf(
            6L..10L,
            2L..6L,
            0L..3L,
        )

        assert(ranges.allOverlap())
    }

    @Test
    fun `allOverlap returns true if ranges with duplicates overlap`() {
        val ranges = listOf(
            6L..10L,
            6L..10L,
            6L..10L,
        )

        assert(ranges.allOverlap())
    }

    @Test
    fun `allOverlap returns true if ranges contain each other`() {
        val ranges = listOf(
            6L..10L,
            7L..10L,
            6L..9L,
        )

        assert(ranges.allOverlap())
    }

    @Test
    fun `groupIntoUnions returns correct unions of ranges`() {
        val ranges = listOf(
            2L..10L,
            8L..12L,
            14L..18L,
            18L..20L,
        )

        val unions = ranges.groupIntoUnions()

        assert(unions.size == 2)
        assert(unions[0].first == 2L)
        assert(unions[0].last == 12L)
        assert(unions[1].first == 14L)
        assert(unions[1].last == 20L)
    }

    @Test
    fun `groupIntoUnions returns one union for duplicate ranges`() {
        val ranges = listOf(
            2L..10L,
            2L..10L,
            2L..10L,
            2L..10L,
        )

        val unions = ranges.groupIntoUnions()

        assert(unions.size == 1)
        assert(unions[0].first == 2L)
        assert(unions[0].last == 10L)
    }
}