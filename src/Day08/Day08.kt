fun main() {
    fun go(pair: Pair<String, String>, dir: Char): String {
        return when (dir) {
            'R' -> pair.second
            'L' -> pair.first
            else -> "Exception"
        }
    }
    fun go(pairs: List<Pair<String, String>>, dir: Char): List<String> {
        return when (dir) {
            'R' -> pairs.map {pair -> pair.second }
            'L' -> pairs.map {pair -> pair.first }
            else -> emptyList()
        }
    }

    fun instructions(input: List<String>): Pair<String, MutableMap<String, Pair<String, String>>> {
        var directions = ""
        val steps = mutableMapOf<String, Pair<String, String>>()
        input.forEachIndexed { index, line ->
            if (index == 0) {
                directions = line
            } else if (line.trim().isNotEmpty()) {

                val step = line.split(" = (")
                val (left, right) = step[1].split(", ")
                steps[step[0]] = Pair(left, right.substring(0..2))
            }
        }
        return Pair(directions, steps)
    }

    fun part1(input: List<String>): Int {
        val (directions, steps) = instructions(input)
        var step = "AAA"
        var counter = 0;
        while (step != "ZZZ") {
            val direction = directions[counter % directions.length]
            val next = go(steps[step]!!, direction)
            if (next == step) {
                throw Exception("LOOP")
            } else {
                step = next
                counter++
            }
        }
        return counter
    }


    fun part2(input: List<String>): Long {
        val (directions, steps) = instructions(input)
        var stepList = steps.filter { it.key.endsWith('A') }.map { it.key }
        val minSteps = stepList.map {
            var counter = 0
            var step = it
            do {
                val direction = directions[counter % directions.length]
                val next = go(steps[step]!!, direction)
                if (next == step) {
                    throw Exception("LOOP")
                } else {
                    step = next
                    counter++
                }
            } while (!step.endsWith("Z"))
             counter
        }
        println(minSteps)
        var lcm = minSteps[0].toLong()
        minSteps.drop(1).forEach { lcm = lcm(lcm, it.toLong()) }

        return lcm
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val testInput1_2 = readInput("Day08_test_2")
    check(part1(testInput1_2) == 6)
    val testInput2 = readInput("Day08_2_test")
    check(part2(testInput2) == 6L)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
