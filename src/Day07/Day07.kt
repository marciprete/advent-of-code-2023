class Hand(var hand: String, val win: Int) : Comparable<Hand> {
    val shuffle = getPoints(hand)
    val power = getPower(shuffle)
//    val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val cards = listOf('A', 'K', 'Q',  'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    fun getPoints(hand: String) = hand.groupBy({ it }, { it }).map { (k, v) -> k to v.size }
    fun getPower(pairs: List<Pair<Char, Int>>) = pairs.map{it.second}.fold(0) { acc, pair -> acc + (pair * pair) }

    fun getJokerPower(): Int {
        val nec = hand.groupBy({ it }, { it }).maxBy { (_,v) -> v.size }.key
        return when (hand.contains('J')) {
            true -> {
                println("$hand to $nec ")
                getPower(getPoints(hand.replace('J', nec)))
            }//.replace('J', )
            false -> power
        }
    }

    override fun compareTo(o: Hand): Int {
        val oList = this.hand.toList()
        val itera = o.hand.toList().iterator().withIndex()
        var comp: Int = 0
        while (itera.hasNext()) {
            val c = itera.next()
            comp = cards.indexOf(c.value).compareTo(cards.indexOf(oList[c.index]))
            if (comp != 0) break
        }
        return comp
    }

    override fun toString(): String {
        return "Hand(hand='$hand', win=$win, shuffle=$shuffle, power=$power, cards=$cards)"
    }

}

fun main() {

    fun part1(input: List<String>): Int {
        val list = input.map { line ->
            val (hand, win) = line.trim().split(" ")
            Hand(hand, win.toInt())
        }.toList()
        val sorted = list.sortedWith( compareBy<Hand>({it.power}, {it}))

//        println(sorted.mapIndexed{ index, hand -> (index+1)*hand.win }.sum())
        return 0
    }

    fun part2(input: List<String>): Int {
        val list = input.map { line ->
            val (hand, win) = line.trim().split(" ")
            Hand(hand, win.toInt())
        }.toList()
        val sorted = list.sortedWith( compareBy<Hand>({it.getJokerPower()}, {it}))
        println ("--part2--")
        println(sorted.mapIndexed{ index, hand -> (index+1)*hand.win }.sum())
        //246710247 too high
        //246954468
        return 0
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 0)
    check(part2(testInput) == 0)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
