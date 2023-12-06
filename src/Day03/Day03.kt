fun main() {

    val pattern = "[^a-zA-Z0-9.]".toRegex()

    fun addEdges(numbers: List<Int>): List<Int> {
        var clone = numbers.toMutableList()
        clone.add(numbers.min() - 1)
        clone.add(numbers.max() + 1)
        return clone
    }

    fun mapLinesToNumbersWithPositionByIndex(input: List<String>) = input.mapIndexed { idx, originalLine ->
        val line = "$originalLine.";
        val numbers = mutableListOf<Triple<Int, Int, List<Int>>>()
        var number = ""
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

    fun getSymbolPositionMap(input: List<String>) = input.mapIndexed { i, line ->
        val indexes = mutableListOf<Pair<Char, Int>>()
        line.forEachIndexed { idx, char ->
            if (pattern.matches(char.toString())) {
                indexes.add(Pair(char, idx))
            }
        }
        val swap = indexes.toList()
        indexes.clear()
        i to swap
    }.toMap()

    fun part1(input: List<String>): Int {
        val mappedNumbers = mapLinesToNumbersWithPositionByIndex(input)
        val symbolPositions = getSymbolPositionMap(input)

        val elems = mutableSetOf<Triple<Int, Int, List<Int>>>()

        for (i in 1..<mappedNumbers.size) {
            val positions = mutableSetOf<Int>()
            symbolPositions[i - 1]?.map { it.second }?.let { positions.addAll(it) }
            symbolPositions[i]?.map { it.second }?.let { positions.addAll(it) }

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
        val mappedNumbers = mapLinesToNumbersWithPositionByIndex(input)
        val symbolPositions = getSymbolPositionMap(input)
        val gears = symbolPositions.map { (k, v) ->
            k to v.filter { it.first.equals('*') }
        }.toMap()
        val map = mutableMapOf<Triple<Int, Char, Int>, MutableList<Int>>()
        for (i in 1..<mappedNumbers.size) {
            val toSearch = mutableSetOf<Triple<Int, Int, List<Int>>>()
            mappedNumbers[i - 1]?.let { toSearch.addAll(it) }
            mappedNumbers[i]?.let { toSearch.addAll(it) }

            val firstTwoGearRows = mutableListOf<Triple<Int, Char, Int>>()
            gears[i - 1]?.let {
                it.forEach { pair ->
                    firstTwoGearRows.add(Triple(i-1, pair.first, pair.second))
                }
            }
            gears[i]?.let {
                it.forEach { pair ->
                    firstTwoGearRows.add(Triple(i, pair.first, pair.second))
                }
            }

            toSearch.forEach { triple ->
                firstTwoGearRows?.forEach { gear ->
                    if (triple.third.contains(gear.third)) {
                        mappedNumbers[i]?.remove(triple)
                        mappedNumbers[i - 1]?.remove(triple)
                        if (map[gear] == null) {
                            map[gear] = mutableListOf()
                        }
                        map.get(gear)?.add(triple.first)
                    }
                    map.filter { (k,v)->v.size==2 }.map { (k,v) ->
                        v[0]*v[1]
                    }
                }
            }
        }
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
