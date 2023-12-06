fun main() {

//    var races = mutableListOf(Pair(62,553), Pair(64, 1010), Pair(91, 1473), Pair(90, 1074), )

    fun getRacePairs(input: List<String>): List<Pair<String, String>> {
        val lines = input.map { line ->
            """\d+""".toRegex().findAll(line).map { seq ->
                seq.value
            }.toList()
        }
        return lines[0].mapIndexed() { idx, lin ->
            Pair(lin, lines[1][idx])
        }.toList()
    }

    fun countWinners(races: List<Pair<String, String>>): MutableList<Int> {
        val counters = mutableListOf<Int>()
        races.forEach { race ->
            val n = mutableListOf<Long>()
            for (i in 0..race.first.toLong()) {
                if((race.first.toLong()-i)*i > race.second.toLong()) {
                    n.add(i)
                }
            }
            counters.add(n.size)
        }
        return counters
    }

    fun part1(input: List<String>): Int {
        val races = getRacePairs(input)
        return countWinners(races).reduce { acc, i -> acc*i }
    }

    fun part2(input: List<String>): Int {
        val races = getRacePairs(input)

        return countWinners(mutableListOf(races.reduce{ acc, i ->
                 Pair(acc.first+i.first, acc.second+i.second)
        })).reduce { acc, i -> acc*i }
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

