@OptIn(ExperimentalStdlibApi::class)
fun main() {

    val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val altCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    class Hand(val hand: String, val bid: Int) {
        val value: String
            get() {
                return hand.map{ c -> cards.reversed().indexOf(c).toHexString()}.reduce{acc, s -> acc+s }
            }

        val altValue: String
            get() {
                return hand.map{ c -> altCards.reversed().indexOf(c).toHexString()}.reduce{acc, s -> acc+s }
            }

        val strength: Int
            get() {
                return hand.groupBy({ it }, { it }).map { (k, v) -> k to v.size }.fold(0) { acc, pair -> acc + pair.second * pair.second }
            }

        val altStrength: Int
            get() {
                val mostFreqChars = hand.groupBy({ it }, { it }).map { (k, v) -> k to v.size }.toList().sortedWith(
                    compareBy({ it.second }, {altCards.reversed().indexOf(it.first)})
                ).reversed()
                var mostFreqChar = mostFreqChars.first()
                if(mostFreqChar.first=='J' && mostFreqChar.second!=5) {
                    mostFreqChar = mostFreqChars[1]
                }
                return hand.replace('J', mostFreqChar.first).groupBy({ it }, { it }).map { (k, v) -> k to v.size }.fold(0) { acc, pair -> acc + pair.second * pair.second }
            }

        override fun toString(): String {
            return "Hand(hand='$hand', bid=$bid, strength=${strength}, altStr=${altStrength})"
        }
    }

    fun mapInputToListOfHands(input: List<String>): List<Hand> {
        return input.map { line ->
            val (hand, bid) = line.split(" ")
            Hand(hand, bid.toInt())
        }
    }

    fun part1(input: List<String>): Int {
       return mapInputToListOfHands(input).sortedWith(
            compareBy<Hand>{it.strength}.thenBy { it.value }
        ).mapIndexed{ index, hand -> (index+1)*hand.bid }.sum()


    }

    fun part2(input: List<String>): Int {
        return mapInputToListOfHands(input).sortedWith(
            compareBy<Hand>{it.altStrength}.thenBy { it.altValue }
        ).mapIndexed{ index, hand -> (index+1)*hand.bid }.sum()

    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")

    part1(input).println()
    part2(input).println()
}
