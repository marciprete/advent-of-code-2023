fun main() {

    data class Universe(var rows: MutableList<MutableList<Char>>) {
        fun getColumn(col: Int) = CharArray(rows.size) { rows[it][col] }
    }

    data class Galaxy(val x: Long, val y: Long) {
        fun distance(from: Galaxy): Long {
          return  Math.abs(this.x - from.x) + Math.abs(this.y - from.y)
        }
    }

    fun howManyExpansions(coord: Int, expansions: List<Int>) : Int{
        var total = 0
        expansions.forEach { n ->
            if(coord > n) {
                total++
            }
        }
        return total
    }

    fun expandAndGetGalaxies(universe: Universe, amount: Long): MutableList<Galaxy> {
        val expandingRows = mutableListOf<Int>()
        universe.rows.forEachIndexed { i, p ->
            if (!p.contains('#')) {
                expandingRows.add(i)
            }
        }
        val expandingCols = mutableListOf<Int>()
        for (i in 0..<universe.rows[0].size) {
            if (!universe.getColumn(i).contains('#')) {
                expandingCols.add(i)
            }
        }
        val galaxies = mutableListOf<Galaxy>()
        universe.rows.forEachIndexed { x, row ->
            row.forEachIndexed { y, c ->
                if (c == '#') {
                    galaxies.add(Galaxy(
                            (x + howManyExpansions(x, expandingRows) * amount),
                            (y + howManyExpansions(y, expandingCols) * amount)
                    ))
                }
            }
        }
        return galaxies;
    }

    fun getPermutationsMap(galaxies: List<Galaxy>): MutableMap<Galaxy, MutableList<Galaxy>> {
        val pairs = mutableListOf<Pair<Galaxy, Galaxy>>()
        val map = mutableMapOf<Galaxy, MutableList<Galaxy>>()
        for (i in 0..<(galaxies.size - 1)) {
            for (n in i + 1..<galaxies.size) {
                pairs.add(Pair(galaxies[i], galaxies[n]))
                map.computeIfAbsent(galaxies[i]) { _ -> mutableListOf() }.add(galaxies[n])
            }
        }
        return map
    }


    fun part1(input: List<String>): Long {
        val universe = input.map { it.toMutableList() }.let { Universe(it.toMutableList()) }
        val galaxies = expandAndGetGalaxies(universe, 1)
        val map = getPermutationsMap(galaxies)
        val mins = mutableListOf<Long>()
        map.forEach { (key, values) ->
            mins.addAll(values.map { key.distance(it) })
        }
        return mins.sum()
    }

    fun part2(input: List<String> , amount: Long = 99): Long {
        val universe = input.map { it.toMutableList() }.let { Universe(it.toMutableList()) }
        val galaxies = expandAndGetGalaxies(universe, amount)
        val map = getPermutationsMap(galaxies)
        val mins = mutableListOf<Long>()
        map.forEach { (key, values) ->
            mins.addAll(values.map { key.distance(it) })
        }
        return mins.sum()
    }

    val testInput = readInput("Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput) == 8410L)

    val input = readInput("Day11")
    part1(input).println()
    part2(input, 999999).println()
}
