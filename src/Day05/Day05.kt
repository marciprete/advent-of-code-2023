import java.math.BigInteger

fun main() {
    val sequence = listOf(
        "seed-to-soil",
        "soil-to-fertilizer",
        "fertilizer-to-water",
        "water-to-light",
        "light-to-temperature",
        "temperature-to-humidity",
        "humidity-to-location",
    )

    class SeedConverter(val from: BigInteger, val range: BigInteger, val value: BigInteger) {

        fun convert(seed: BigInteger): BigInteger? {
            return if (seed in from..from + range) {
                (seed - from) + value
            } else {
                null
            }
        }

        override fun toString(): String {
            return "SeedConverter(from=$from, range=$range, value=$value)"
        }


    }

    class ConversionMap() : HashMap<String, MutableList<SeedConverter>>() {
        fun convert(key: String, seed: BigInteger): BigInteger {
            val converters = this[key]
            val converted = converters?.firstOrNull { it.convert(seed) != null }?.convert(seed)
            return when (converted) {
                null -> seed
                else -> converted
            }
        }
    }

    fun part1(input: List<String>): BigInteger {
        val seeds = mutableListOf<BigInteger>()
        val conversionMap = ConversionMap()
        var currentMapKey = ""
        input.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(line.split(": ")[1].split(" ").map { it.trim().toBigInteger() }.toList())
            } else if (line.contains("map")) {
                currentMapKey = line.split(" map:")[0]
                conversionMap.computeIfAbsent(currentMapKey) { v -> mutableListOf() }
            } else if (line.isNotEmpty()) {
                val values = line.split(" ").map { it.toBigInteger() }
                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2] - BigInteger.ONE, values[0]))
            }
        }
        val distances = mutableListOf<BigInteger>()
        seeds.forEach { seed ->
            var distance = seed
            sequence.forEach { key ->
                distance = conversionMap.convert(key, distance)
            }
            distances.add(distance)
        }
        return distances.min()
    }

    fun part2(input: List<String>): BigInteger {

        val seeds = mutableListOf<BigInteger>()
        val conversionMap = ConversionMap()
        var currentMapKey = ""
        input.forEach { line ->
            if (line.contains("seeds")) {
                val ugo = line.split(": ")[1].split(" ").map { it.trim().toBigInteger() }.windowed(2,2)
                ugo.forEach{ coppia ->
//                    for (i in coppia[0]?.plus(BigInteger.ZERO)..coppia[1].plus(B)) {
////                        seeds.add(i)
//                    }
//
                }
                //seeds.addAll(ugo.map { it.toList() })
              println( ugo[0].javaClass)
                seeds.addAll(line.split(": ")[1].split(" ").map { it.trim().toBigInteger() }.toList())
            } else if (line.contains("map")) {
                currentMapKey = line.split(" map:")[0]
                conversionMap.computeIfAbsent(currentMapKey) { v -> mutableListOf() }
            } else if (line.isNotEmpty()) {
                val values = line.split(" ").map { it.toBigInteger() }
                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2] - BigInteger.ONE, values[0]))
            }
        }
        val distances = mutableListOf<BigInteger>()
        seeds.forEach { seed ->
            var distance = seed
            sequence.forEach { key ->
                distance = conversionMap.convert(key, distance)
            }
            distances.add(distance)
        }
        return distances.min()
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == BigInteger.valueOf(35))
    check(part2(testInput) == BigInteger.valueOf(135))

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
