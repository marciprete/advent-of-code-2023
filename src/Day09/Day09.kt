fun main() {

    fun zero(longs: List<Long>) : List<Long> {
        return longs.windowed(2).map { it.reduce{ acc: Long, l: Long -> l-acc } }
    }
    fun part1(input: List<String>): Long {
        val longs = input.map { line -> line.split(" ").map { it.toLong() } }
        val series = longs.map { current ->
            val steps = mutableListOf<List<Long>>(current)
            var step = zero(current)
            while (!step.all { it == 0L }) {
                steps.add(step)
                step = zero(step)
            }
            println(steps)
            steps.reversed().map { step ->
                step.last()
            }.reduce { acc: Long, l: Long -> acc+l }
        }
        return series.sum()
    }

    fun part2(input: List<String>): Long {
        val longs = input.map { line -> line.split(" ").map { it.toLong() } }
        val series = longs.map { current ->
            val steps = mutableListOf<List<Long>>(current)
            var step = zero(current)
            while (!step.all { it == 0L }) {
                steps.add(step)
                step = zero(step)
            }
            steps.add(listOf(0))

            steps.reversed().map { step ->
                step.first()
            }.reduce { acc: Long, l: Long -> -acc+l }
        }
        return series.sum()
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 114L)
    check(part2(testInput) == 2L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
