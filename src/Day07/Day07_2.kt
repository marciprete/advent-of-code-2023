fun main() {

    val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val altCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    class Hand(val hand: String, val bid: Int, var joked : Boolean = false) : Comparable<Hand> {

        fun strength(): Int {
            val newHand = when (joked) {
                true ->  {
                    val mostFreqChar = hand.groupBy({ it }, { it }).map { (k, v) -> k to v.size }.toList().maxBy{ p -> p.second }
                    hand.replace('J', mostFreqChar.first)
                }
                else -> hand
            }
            return (newHand.groupBy({ it }, { it }).map { (k, v) -> k to v.size }.fold(0) { acc, pair -> acc + pair.second * pair.second }*1000
                     + biStrength()
                    )
        }

        fun biStrength(): Int {
            val myCards = when(joked) {
                true -> altCards
                else -> cards
            }
            return hand.foldIndexed(0){index, acc, c -> acc+(index*myCards.reversed().indexOf(c))}
        }

        override fun compareTo(other: Hand): Int {
            var comp = strength().compareTo(other.strength())
            if (comp == 0) {
                comp = biStrength().compareTo(other.biStrength())
            }
            return comp
        }

        override fun toString(): String {
            return "Hand(hand='$hand', bid=$bid, $joked, strength=${strength()})"
        }
    }

    fun part2(input: List<String>): Int {
        val pi = input.map { line ->
            val (hand, bid) = line.split(" ")
            Hand(hand, bid.toInt(), true)
        }
        var sorted = pi.sortedBy { it.strength() }
        println(pi)
        println(sorted)
        sorted.forEach {
            print(it.strength())
            it.joked = true
            println(" then ${it.strength()}")
        }
        println(sorted.mapIndexed{ index, hand -> (index+1)*hand.bid }.sum())
        return 0
    }

    val testInput = readInput("Day07_test")
    check(part2(testInput) == 142)

    val input = readInput("Day07")
    part2(input).println()
}
