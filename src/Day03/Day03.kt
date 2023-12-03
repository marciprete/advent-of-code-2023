import kotlin.math.min

fun main() {
    fun addEdges(numbers: List<Int>): List<Int> {
        var clone = numbers.toMutableList()
        clone.add(numbers.min() - 1)
        clone.add(numbers.max() + 1)
        return clone
    }

    fun mapLinesToNumbersWithPositionByIndex(input: List<String>) = input.mapIndexed { idx, originalLine ->
        val line = "$originalLine.";
        val numbers = mutableListOf<Triple<Int, Int, List<Int>>>()
        var number: String = ""
        var indexes = mutableListOf<Int>()
        line.forEachIndexed { i, char ->
            if (char.isDigit()) {
                number += "" + char
                indexes.add(i)
            } else {
                if (number.isNotEmpty()) {
                    numbers.add(Triple(number.toInt(), idx, addEdges(indexes.toList())))
                    number = ""
                    indexes.clear()
                }
            }
        }
        idx to numbers
    }.toMap()

    fun part1(input: List<String>): Int {
        val mappedNumbers = mapLinesToNumbersWithPositionByIndex(input)
        val pattern = "[^a-zA-Z0-9.]".toRegex()
        val cosa = input.mapIndexed { i, line ->
            val indici = mutableListOf<Int>()
            line.forEachIndexed { idx, char ->
                if (pattern.matches(char.toString())) {
                    indici.add(idx)
                }
            }
            val retto = indici.toList()
            indici.clear()
            i to retto
        }.toMap()
        val elems = mutableSetOf<Triple<Int, Int, List<Int>>>()

        for (i in 1..<mappedNumbers.size) {
            val positions = mutableSetOf<Int>()
            cosa[i - 1]?.let { positions.addAll(it) }
            cosa[i]?.let { positions.addAll(it) }

            val toSearch = mutableSetOf<Triple<Int, Int, List<Int>>>()
            mappedNumbers[i - 1]?.let { toSearch.addAll(it) }
            mappedNumbers[i]?.let { toSearch.addAll(it) }

            toSearch.forEach { pair ->
                if (pair.third.intersect(positions).isNotEmpty()) {
                    elems.add(pair)
                }
            }
        }
        var numbers = elems.map {
            it.first
        }
        return numbers.sum()

    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    println("più di 542793")
    part2(input).println()
}
