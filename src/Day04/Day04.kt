fun main() {
    fun getMappedListOfWinningNumbersByCard(input: List<String>): Map<Int, List<Int>> {
        val map = input.mapIndexed { idx, line ->
            val (_, numbers) = line.split(": ")
            val (winning, owned) = numbers.split(" | ")
            val won = winning.trim().split(" ").filter { it.isNotEmpty() }.intersect(
                owned.trim().split(" ")
            )
            idx to won.map { it.toInt() }
        }.toMap()
        return map
    }

    fun part1(input: List<String>): Int {
        val winningNumbers = getMappedListOfWinningNumbersByCard(input)

        return winningNumbers.values.map {
            if (it.isNotEmpty()) {
                Math.pow(2.0, (it.size - 1).toDouble())
            } else {
                0
            }
        }.sumOf { it.toInt() }
    }

    fun part2(input: List<String>): Int {
        val mappedListOfWinningCards = getMappedListOfWinningNumbersByCard(input)
        val winningCardOriginalSizes = mutableListOf<Int>()
        val copies = mutableListOf<Int>()
        mappedListOfWinningCards.forEach{ k, v ->
            winningCardOriginalSizes.add(k, v.size)
            if (v.isEmpty()) {
                copies.add(0)
            } else {copies.add(1)}
        }
        winningCardOriginalSizes.forEachIndexed{ idx, count ->
            if(mappedListOfWinningCards[idx]!!.isNotEmpty()) {
                for (i in idx+1..idx+count) {
                    copies[i]=copies[i]+copies[idx]
                }
            }
        }
        mappedListOfWinningCards.forEach{ ix, v ->
            if(v.isEmpty()) { copies[ix]++ }
        }
        return copies.sum()

    }

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
