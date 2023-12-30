import data.AdjacencyList
import data.Vertex

data class Step(val direction: Char, val coords: Coords) : Comparable<Step> {


    override fun compareTo(other: Step): Int {
        return this.coords.compareTo(other.coords)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Step

        return coords == other.coords
    }

    override fun hashCode(): Int {
        return coords.hashCode()
    }
}

fun main() {
    val graph = AdjacencyList<Step>()
    val directions = setOf('.', '<', '>', '^', 'v')
    val slopes = directions.minus('.')

    var checkSlope: Boolean = true


    fun followTheSlope(matrix: MutableList<MutableList<Char>>, origin: Step): Step {
        val trail = matrix.from(origin.coords)

        return when (trail) {
            '^' -> Step('U', Coords(origin.coords.x, origin.coords.y - 1))
            '>' -> Step('R', Coords(origin.coords.x + 1, origin.coords.y))
            'v' -> Step('D', Coords(origin.coords.x, origin.coords.y + 1))
            else -> Step('L', Coords(origin.coords.x - 1, origin.coords.y))
        }
    }

    fun getSiblings(matrix: MutableList<MutableList<Char>>, origin: Step): List<Step> {
        val siblings = mutableListOf<Step>()
        val originDirection = matrix.from(origin.coords)
        if (originDirection in slopes) {
            siblings.add(followTheSlope(matrix, origin))
        } else {
            if (origin.direction != 'L' && origin.coords.x + 1 in 0..<matrix[0].size) {
                val right = Step('R', Coords(origin.coords.x + 1, origin.coords.y))
                if (matrix.from(right.coords) in directions.minus('<'))
                    siblings.add(right)
            }
            if (origin.direction != 'D' && origin.coords.y - 1 in 0..<matrix.size) {
                val top = Step('U', Coords(origin.coords.x, origin.coords.y - 1))
                if (matrix.from(top.coords) in directions.minus('v'))
                    siblings.add(top)
            }
            if (origin.direction != 'U' && origin.coords.y + 1 in 0..<matrix.size) {
                val bottom = Step('D', Coords(origin.coords.x, origin.coords.y + 1))
                if (matrix.from(bottom.coords) in directions.minus('^'))
                    siblings.add(bottom)
            }
            if (origin.direction != 'R' && origin.coords.x - 1 in 0..<matrix[0].size) {
                val left = Step('L', Coords(origin.coords.x - 1, origin.coords.y))
                if (matrix.from(left.coords) in directions.minus('>'))
                    siblings.add(left)
            }
        }
        return siblings
    }

    fun grafa(matrix: MutableList<MutableList<Char>>, step: Step, origin: Vertex<Step>? = null) {
        var node = origin
        if (origin == null) {
            node = graph.createVertex(step)
        }
        var siblings = getSiblings(matrix, step)
        var steps = 1
        var currentStep: Step? = null
        if (siblings.size == 1) {
            while (siblings.size == 1) {
                currentStep = siblings.first()
                steps++
                siblings = getSiblings(matrix, siblings.first())
            }
            val vertex = graph.createVertex(currentStep!!)
            graph.addDirectedEdge(node!!, vertex, steps.toDouble())
            siblings.forEach { sibling ->
                grafa(matrix, sibling, vertex)
            }
        } else if (siblings.size > 1) {
            siblings.forEach { node -> grafa(matrix, node) }
        }
    }

    fun buildMatrix(input: List<String>): MutableList<MutableList<Char>> {
        val matrix = mutableListOf(mutableListOf<Char>())

        input.forEachIndexed { y, line ->
            if (matrix.size <= y) {
                matrix.add(mutableListOf())
            }
            line.forEachIndexed { x, c ->
                matrix[y].add(c)
            }
        }
        return matrix;
    }

    fun part1(input: List<String>): Int {
        println(checkSlope)
        val matrix = buildMatrix(input)
        val start = Step('R', Coords(matrix[0].indexOf('.'), 0))
        grafa(matrix, start)
        val startVertex = Vertex(start)
        val last = Step('*', Coords(matrix[matrix.size - 1].lastIndexOf('.'), matrix.size - 1))
        var endVertex = Vertex(last)

        return (graph.longestPath(startVertex, endVertex)!! - 1).toInt()
    }

    fun part2(input: List<String>): Int {
        checkSlope = false

        return part1(input)
    }

    val testInput = readInput("Day23_test")
    check(part1(testInput) == 94)
    check(part2(testInput) == 94)

    val input = readInput("Day23")
//    part1(input).println()
//    part2(input).println()
}

private fun <E> MutableList<MutableList<E>>.from(coords: Coords): E {
    return this[coords.y][coords.x]
}
