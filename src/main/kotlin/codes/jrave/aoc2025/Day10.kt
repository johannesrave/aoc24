package codes.jrave.aoc2025

import codes.jrave.buildCombinations
import java.io.File
import kotlin.system.measureTimeMillis

fun main() {
    val day10ATest = Day10A("input/2025/input10-test.txt")
    val day10ATestResult = day10ATest.solve()
    println("Day10A: result: $day10ATestResult, expected result: 7, matches: ${day10ATestResult == 7L}")

    val day10A = Day10A("input/2025/input10.txt")
    val duration10A = measureTimeMillis {
        val solution = day10A.solve()
        println("Day10A: result: $solution, expected result: 578, matches: ${solution == 578L}")
    }

    println("Solution took $duration10A milliseconds")

    //    val day10BTest = Day10B("input/2025/input10-test.txt")
    //    val day10BTestResult = day10BTest.solve()
    //    println("Day10B: result: $day10BTestResult, expected result: 24, matches: ${day10BTestResult == 24L}")
    //
    //    val day10B = Day10B("input/2025/input10.txt")
    //    val duration10B = measureTimeMillis {
    //        val solution = day10B.solve()
    //        println("Day10B: result: $solution, expected result: 1461987144, matches: ${solution == 1461987144L}")
    //    }
    //
    //    println("Solution took $duration10B milliseconds")
}

data class Day10A(
    val inputPath: String,
    val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {

        val flips = mutableListOf<Int>()
        val machines = parseMachines(input)

        machineLoop@ for (machine in machines) {
            val indicators = machine.targetIndicators.copyOf()
            val combos = buildCombinations(machine.switches.size, (0..3).toList())
            for (switchFlipCombo in combos) {
                indicators.fill(false)

                val appendedIndicatorFlips = switchFlipCombo
                    .foldIndexed(mutableListOf<Int>()) { i, acc, times ->
                        repeat(times) {
                            acc.addAll(machine.switches[i].toggledIndicators)
                        }
                        acc
                    }

                machine.targetIndicators.forEachIndexed { indicatorIndex, isIndicatorOn ->
                    // true indicators need to appear an uneven number of times in the switches
                    // so we can append all indicators being flipped by the current switchFlipCombo
                    // and check for even and uneven occurence of the indices (convoluted but i think correct)
                    val occurrencesInFlips = appendedIndicatorFlips.count { it == indicatorIndex }
                    indicators[indicatorIndex] = occurrencesInFlips % 2 == 1
                }

                //                for (i in switchFlipCombo) {
                //                    val switch = machine.switches[i]
                //                    for (j in switch.toggledIndicators) {
                //                        indicators[j] = !indicators[j]
                //                    }
                //                }
                if (indicators.contentEquals(machine.targetIndicators)) {
                    println(switchFlipCombo.joinToString("-"))
                    println(machine.targetIndicators.joinToString("|"))
                    println(machine)
                    flips.add(switchFlipCombo.sum())
                    continue@machineLoop
                }
            }

            println()
        }

        return flips.sum().toLong()
    }

    private fun parseMachines(input: String): List<Machine> {
        val indicatorsRegex = Regex("""\[(.*)]""")
        val switchesRegex = Regex("""\((.+?)\)""")
        val joltagesRegex = Regex("""\{.*}""")

        val machines = input.lines().map { line ->

            val indicators = indicatorsRegex.find(line)!!.value
                .trim('[', ']')
                .map { it == '#' }
                .toBooleanArray()

            val switches = switchesRegex
                .findAll(line)
                .map { match ->
                    val toggledIndicators = match.value
                        .trim('(', ')')
                        .split(",")
                        .map { n -> n.toInt() }
                        .toSet()

                    Switch(toggledIndicators)
                }.toList()
                .toTypedArray()

            Machine(indicators, switches)
        }
        return machines
    }
}

data class Machine(val targetIndicators: BooleanArray, val switches: Array<Switch>) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Machine

        return targetIndicators.contentEquals(other.targetIndicators)
    }

    override fun hashCode(): Int {
        return targetIndicators.contentHashCode()
    }
}

data class Switch(val toggledIndicators: Set<Int>)

data class Day10B(
    val inputPath: String, val input: String = File(inputPath).readText(Charsets.UTF_8)
) {
    fun solve(input: String = this.input): Long {
        return 0L
    }

}
