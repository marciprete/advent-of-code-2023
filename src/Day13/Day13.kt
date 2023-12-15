fun main() {

    class Mountain() {
        var rows = mutableListOf<CharArray>()
        fun getColumn(col: Int) = CharArray(rows.size) { rows[it][col] }
        override fun toString(): String {
            return "Mountain(rows=$rows)"
        }

        fun findReflections(collection : List<CharArray>) : MutableList<Int> {
            val splits = mutableListOf<Int>()
            for (i in 0..<collection.size - 1) {
                if (collection[i].toList().joinToString("") ==
                        collection[i + 1].toList().joinToString(""))
                    splits.add(i)
            }
            return splits
        }

        fun summarize() : Pair<Int, Int> {
            val columns = rows[0].indices.map { index -> CharArray(rows.size) { rows[it][index] }}
            return Pair(getReflections(rows) , getReflections(columns))
        }

        fun error(list: List<CharArray>, i: Int, j: Int) : Pair<Int, Int> {
            val prev = list[i - j].toList()
            val next = list[i + 1 + j].toList()
            if (prev.joinToString("") ==
                    next.joinToString("")) {
                return Pair(0,0)
            } else {
                var distance = 0
                var error = 0
                prev.forEachIndexed { index, c ->

                    if (c - next[index] != 0) {
                        distance++
                        error = c - next[index]
                    }
                }
                return Pair(distance, error)
            }
        }
        fun getReflections(list: List<CharArray>) : Int {
            for (i in findReflections(list)) {
                var found = true
                val count = Math.min(i+1 , Math.abs((i + 1) - list.size))
                for (j in 0..<count) {
                    val errorPair = error(list, i, j)
                    if(errorPair.first>1) {
                        found = false
                    } else if (errorPair.first==1) {
                        //# - . = -11
                        //. - # = 11
                        println("$i: $errorPair")
                        found = false
                    }
                }
                if (found) return i+1
            }
            return 0
        }
    }


    fun part1(input: List<String>): Int {
        val fields = mutableListOf<Mountain>()
        var mirror = Mountain()
        fields.add(mirror)
        input.forEach {
            if (it.isEmpty()) {
                mirror = Mountain()
                fields.add(mirror)
            } else {
                mirror.rows.add(it.toCharArray())
            }
        }
        return fields.map { it.summarize() }.map { it.second+it.first*100 }.sum()
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 405)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
