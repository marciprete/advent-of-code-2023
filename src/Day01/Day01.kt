fun main() {
    fun getCalibrationValues(input: List<String>): List<Int> {
        return input.map { line ->
            val (digits) = line.toList().partition { it.isDigit() }
            digits[0].toString().plus(digits[digits.size - 1]).toInt()
        }
    }

    fun part1(input: List<String>): Int {
        val calibrationValues = getCalibrationValues(input)
        return calibrationValues.sum()
    }

    fun part2(input: List<String>): Int {
        val spelled = arrayOf(Pair("one","1"),
            Pair("two","2"),
            Pair("three","3"),
            Pair("four","4"),
            Pair("five","5"),
            Pair("six","6"),
            Pair("seven","7"),
            Pair("eight","8"),
            Pair("nine","9"))
        var fixed = mutableListOf<String>()
        input.forEach { line ->
            var newLine = line;
            spelled.forEach { literal ->
                newLine = newLine.replace(literal.first, literal.first+literal.second+literal.first)
            }
            fixed.add(newLine)
        }
        return getCalibrationValues(fixed).sum()
    }

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
