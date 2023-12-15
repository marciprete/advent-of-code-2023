fun main() {

    fun getLenses(input: List<String>) = input[0].split(",")

    fun hash(list: List<Int>) : Int = list.fold(0){acc , i ->  ((i+acc)*17)%256}

    fun part1(input: List<String>): Int {
        val ascii = getLenses(input).map { it.toList().map{ it.toInt() }}
        val hashes: List<Int> = ascii.map { hash(it) }
        return hashes.sum()
    }

    fun part2(input: List<String>): Int {
        val lenses = getLenses(input)
        val pairs = lenses.map { it.split("=")}.map {
            when(it.size) {
                2 -> Pair(it[0], it[1].toInt())
                else -> Pair(it[0].substring(0..<it[0].length-1), 0)
            }
        }
        val boxes = mutableMapOf<Int, MutableMap<String, Int>>()
        pairs.forEach { pair ->
            val key = hash(pair.first.map { it.toInt() })
            if(pair.second != 0) {
                val lens = boxes.computeIfAbsent(key) { _ -> mutableMapOf() }
                lens.put(pair.first, pair.second)
            } else {
                boxes.get(key)?.remove(pair.first)
            }
        }
        val focusingPowers = boxes.map { (k, v) ->
          v.values.toList().mapIndexed { ix, n ->
              (k+1)*(ix+1)*n
          }
        }.flatten()
        return focusingPowers.sum()
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}
