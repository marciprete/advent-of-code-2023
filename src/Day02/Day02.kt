import kotlin.math.max

fun main() {

    fun mapInputToStruct(input: List<String>): Map<String, List<Map<String, Int>>> {
        val mapping = input.map { line ->
            val (game, rounds) = line.trim().split(":")
            game.trim().split(" ")[1] to rounds.trim().split(";").map {
                it.trim().split(",").associate {
                    val (size, color) = it.trim().split(" ")//.associateTo(HashMap()) {}
                    color to size.toInt()
                }
            }
        }.toMap()
        return mapping
    }

    fun part1(input: List<String>): Int {

        val redLimit = 12
        val greenLimit = 13
        val blueLimit = 14;

        val mapping = mapInputToStruct(input)

        val validRounds = mapping.mapNotNull { (key, round) ->
            var found = true
            round.forEach { game ->
                if(game["red"]?.compareTo(redLimit) ?: 0 > 0) {
                    found = false
                } else if (game["green"]?.compareTo(greenLimit) ?: 0 > 0) {
                    found = false
                } else if (game["blue"]?.compareTo(blueLimit) ?: 0 > 0) {
                    found = false
                }
            }

            if (found) {
                key.toInt() to round
            } else { null }
        }.toMap()

        return validRounds.keys.sum()
    }

    fun part2(input: List<String>): Int {
        val roundsMap = mapInputToStruct(input)
        val powers = roundsMap.map{ (_, round) ->
            var red = 0
            var green = 0
            var blue = 0

            round.forEach {
                red = max(it["red"] ?: 0, red)
                green = max(it["green"] ?: 0, green)
                blue = max(it["blue"] ?: 0, blue)
            }
            red*green*blue
        }
        return powers.sum()
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
