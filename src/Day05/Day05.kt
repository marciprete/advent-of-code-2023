import kotlin.math.min

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

    class SeedConverter(val source: Long, val dest: Long, val value: Long) {

        val range = source..source + dest

        fun inRange(value: Long): Boolean = value in range

        fun convert(seed: Long): Long {
            return if (seed in range) {
                (seed - source) + value
            } else {
                seed
            }
        }

        override fun toString(): String {
            return "SeedConverter(from=$source, range=$dest, value=$value)"
        }


    }

    class ConversionMap() : HashMap<String, MutableList<SeedConverter>>() {
        fun convert(key: String, seed: Long): Long {
            val converters = this[key]
            val converted = converters?.firstOrNull { it.inRange(seed) }?.convert(seed)
            return when (converted) {
                null -> seed
                else -> converted
            }
        }

        fun getConverter(key: String, seed: Long): SeedConverter? {
            val converters = this[key]
            return converters?.filter { it.inRange(seed) }?.get(0)
        }

        fun getConverters(key: String, seeds: MutableList<LongRange>): List<LongRange> {
            val converters = this[key]
            var newSeeds = mutableSetOf<LongRange>()

            seeds.forEach { seed ->
                converters?.forEach { converter ->
                    if (converter.inRange(seed.first) && converter.inRange(seed.last)) {
                        newSeeds.add(converter.convert(seed.first)..converter.convert(seed.last))
                    } else if (converter.inRange(seed.first)) {
                        newSeeds.add(converter.convert(seed.first)..converter.convert(converter.range.last))
                        val exceed: LongRange = converter.range.last + 1..seed.last
                        newSeeds.addAll(getConverters(key, mutableListOf(exceed)))
                    } else if (converter.inRange(seed.last)) {
                        newSeeds.add(converter.convert(converter.range.first)..converter.convert(seed.last))
                        val exceed = seed.first..<converter.range.first
                        newSeeds.addAll(getConverters(key, mutableListOf(exceed)))
                    } else if (converter.range.first in seed || converter.range.last in seed) {
                        newSeeds.add(seed.first..<converter.range.first)
                        newSeeds.add(converter.convert(converter.range.first)..converter.convert(converter.range.last))
                        newSeeds.add(converter.range.last + 1..seed.last)
                    } else if (
                        !(seed.first in converters.map { it.range.first }.min()..converters.map { it.range.last }
                            .max()) &&
                        !(seed.last in converters.map { it.range.first }.min()..converters.map { it.range.last }.max())
                    ) {
                        newSeeds.add(seed)
                    }
                }
            }
            return when (newSeeds.isEmpty()) {
                true -> seeds
                else -> newSeeds.toList()
            }
        }

    }

    fun part1(input: List<String>): Long {
        val seeds = mutableListOf<Long>()
        val conversionMap = ConversionMap()
        var currentMapKey = ""
        input.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(line.split(": ")[1].split(" ").map { it.trim().toLong() }.toList())
            } else if (line.contains("map")) {
                currentMapKey = line.split(" map:")[0]
                conversionMap.computeIfAbsent(currentMapKey) { v -> mutableListOf() }
            } else if (line.isNotEmpty()) {
                val values = line.split(" ").map { it.toLong() }
                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2] - 1, values[0]))
            }
        }
        val distances = mutableListOf<Long>()
        seeds.forEach { seed ->
            var distance = seed
            sequence.forEach { key ->
                distance = conversionMap.convert(key, distance)
            }
            distances.add(distance)
        }
        return distances.min()
    }

    fun part2(input : List<String>) : Long {
            val seeds = mutableListOf<Long>()
            val conversionMap = ConversionMap()
            var currentMapKey = ""
            input.forEach { line ->
                if (line.contains("seeds")) {
                    var window = line.split(": ")[1].split(" ").map { it.trim().toLong() }.toList().windowed(2,2).toList()
                    window.forEach { it ->
                        for (i in it[0]..<it[0]+it[1]) {
                            seeds.add(i)
                        }
                    }
                    //seeds.addAll()
                } else if (line.contains("map")) {
                    currentMapKey = line.split(" map:")[0]
                    conversionMap.computeIfAbsent(currentMapKey) { v -> mutableListOf() }
                } else if (line.isNotEmpty()) {
                    val values = line.split(" ").map { it.toLong() }
                    conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2] - 1, values[0]))
                }
            }
            val distances = mutableListOf<Long>()
            seeds.forEach { seed ->
                var distance = seed
                sequence.forEach { key ->
                    distance = conversionMap.convert(key, distance)
                    println("Finished $key")
                }
                distances.add(distance)
            }
        println (distances.min())
        return distances.min()
    }

//    fun part2(input: List<String>): Long {
//
//        val seeds = mutableListOf<LongRange>()
//        val conversionMap = ConversionMap()
//        var currentMapKey = ""
//        input.forEach { line ->
//            if (line.contains("seeds")) {
//                seeds.addAll(
//                    line.split(": ")[1].split(" ").map { it.trim().toLong() }.windowed(2, 2)
//                        .map { it[0]..<(it[0] + it[1]) })
//            } else if (line.contains("map")) {
//                currentMapKey = line.split(" map:")[0]
//                conversionMap.computeIfAbsent(currentMapKey) { _ -> mutableListOf() }
//            } else if (line.isNotEmpty()) {
//                val values = line.split(" ").map { it.toLong() }
//                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2] - 1, values[0]))
//            }
//        }
//        seeds.forEach { seed ->
//            var distance = mutableListOf(seed)
//            println("Seed: $seed")
//            sequence.forEach { key ->
//                println("Current groups: $distance")
//                if (key.equals("humidity-to-location")) {
//                    print("$key has ")
////                    println(distance.map { it })
//                    println(distance.map { it.first }.min())
//                }
////                non è
////                30461998
////                950380331
//                distance = conversionMap.getConverters(key, distance).toMutableList()
//
//            }
//        }
//        return 0
//    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
//    part1(input).println()
    part2(input).println()
}
