fun main() {

    /* J = < ^
       F = \/ >
       | = \/ ^
       L = ^ >
       - = < >
       7 = < \/
     */

    class Coords(val x: Int, val y: Int) {
        override fun toString(): String {
            return "Coords(x=$x, y=$y)"
        }
    }

    class Node(val type: Char, var next: Node? = null) {
        override fun toString(): String {
            return "Node(type=$type, -> $next)"
        }
    }

    class Field(val map: List<List<Char>>) {
        fun getStartingCoords(): Coords {
            map.mapIndexed { i, row ->
                if (row.contains('S')) {
                    return (Coords(row.indexOf('S'), i))
                }
            }
            error("S not found")
        }

        fun getAt(loc: Coords): Char? {
            if (loc.x in map.indices && loc.y in map[0].indices) {
                return map[loc.x][loc.y]
            } else {
                return null
            }
        }

        fun walk(node: Coords) {
            println(getNextFrom(node))
        }

        fun getNextFrom(start: Coords) : Coords {
            val startNode = Node('S')
            //right: -, J, 7
            val right = Coords(start.x+1, start.y)

            if (getAt(right) in setOf('-', 'J', '7')) {
                startNode.next = Node(getAt(right)!!)
                println(startNode)
                return right
            }
            //up:    |, F, 7
            //left:  -, F, L
            //down:  |, J, L
            error("Azz")
        }

    }


    fun part1(input: List<String>): Int {
        val field = input.map { it.toList() }.let { Field(it) }
        val start = field.getStartingCoords()
        field.walk(start)
//        val start = Node(field.getStartingCoords())



        return 0
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 9)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
