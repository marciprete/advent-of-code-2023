import java.lang.Exception

fun main() {
    fun go(pair : Pair<String, String>, dir : Char) :String {
        return when (dir) {
            'R' -> pair.second
            'L' -> pair.first
            else -> "Exception"
        }
    }

    fun part1(input: List<String>): Int {
        var directions = ""
        val steps = mutableMapOf<String, Pair<String,String>>()
        input.forEachIndexed { index, line ->
            if (index==0) {
                directions = line
            } else if (line.trim().isNotEmpty()){

                val step = line.split(" = (")
                val (left, right) = step[1].split(", ")
                steps[step[0]] = Pair(left, right.substring(0..2))
            }
        }
        var step = "AAA"
        var counter = 0;
        while(step != "ZZZ") {
            val direction = directions[counter%directions.length]
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
        return 46
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 2)
    val testInput2 = readInput("Day08_test_2")
    check(part1(testInput2) == 6)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
