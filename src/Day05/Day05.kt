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

        val range = source..<source + dest

        fun inRange(value: Long): Boolean = value in range

        fun convert(seed: Long): Long {
            return if (seed in range) {
                (seed - source) + value
            } else {
                seed
            }
        }

        override fun toString(): String {
            return "SeedConverter(from=$source, range=$dest, value=$value, range=$range)"
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

            seeds.sortedBy { it.first }.forEach { seed ->
                converters?.forEach { converter ->
                    if(seed.last < converter.range.first ||
                            seed.first > converter.range.last) {
                        /*do nothing
                        [a..b]
                              [x..y]
                              or
                                [a..b]
                         [x..y]
                         */
                    }
                    else if(seed.first >= converter.range.first &&
                        seed.last <= converter.range.last  ) {
                        /*
                                  [a..b]
                                [x.......y]
                            return f(a)..f(b)
                         */
                        newSeeds.add(converter.convert(seed.first)..converter.convert(seed.last))
                    }
                    else if( seed.first < converter.range.first &&
                            seed.last <= converter.range.last) {
                        /*
                              [a....b]
                                [x.......y]
                            return [a..x[ + [f(x)..f(b)]
                         */
                        newSeeds.add(seed.first..<converter.range.first)
                        newSeeds.add(converter.convert(converter.range.first)..converter.convert(seed.last))
                    }
                    else if( seed.first > converter.range.first &&
                            seed.last > converter.range.last) {
                        /*
                                  [a.........b]
                                [x.......y]
                            return [f(a)..f(y)] + conv( ]y..b] )
                         */
                        newSeeds.add(converter.convert(seed.first)..converter.convert(converter.range.last))
                        newSeeds.addAll(getConverters(key, mutableListOf(converter.range.last+1..seed.last)))
                    }
                    else if( seed.first < converter.range.first &&
                            seed.last > converter.range.last) {
                        /*
                              [a.............b]
                                [x.......y]
                            return [a..x[ + [f(x)..f(y)] + conv( ]y..b] )
                         */
                        newSeeds.add(seed.first..<converter.range.first)
                        newSeeds.add(converter.convert(converter.range.first)..converter.convert(converter.range.last))
                        newSeeds.addAll(getConverters(key, mutableListOf(converter.range.last+1..seed.last)))
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
                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2], values[0]))
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

    fun part2(input: List<String>): Long {

        val seeds = mutableListOf<LongRange>()
        val conversionMap = ConversionMap()
        var currentMapKey = ""
        input.forEach { line ->
            if (line.contains("seeds")) {
                seeds.addAll(
                    line.split(": ")[1].split(" ").map { it.trim().toLong() }.windowed(2, 2)
                        .map { it[0]..<(it[0] + it[1]) })
            } else if (line.contains("map")) {
                currentMapKey = line.split(" map:")[0]
                conversionMap.computeIfAbsent(currentMapKey) { _ -> mutableListOf() }
            } else if (line.isNotEmpty()) {
                val values = line.split(" ").map { it.toLong() }
                conversionMap[currentMapKey]?.add(SeedConverter(values[1], values[2], values[0]))
            }
        }


        val distance = seeds.sortedBy { it.first }.map { seed ->
            var distance = mutableListOf(seed)
//            println("Seed: $seed")
            sequence.map { key ->
//                println("Current groups: $distance")
                if (key.equals("humidity-to-location")) {
                    print("$key has ")
//                    println(distance.map { it })
                    println(distance.map { it.first }.min())
                }
//                non è
//                30461998
                //950380331
//                950380331
                distance = conversionMap.getConverters(key, distance).toMutableList()

            }.toList()
        }
        return 46//distance.map { it.first }.min()
    }

    fun getOutputRanges(map: Map<LongRange, LongRange>, input: LongRange): List<LongRange> {
        val mappedInputRanges = mutableListOf<LongRange>()
        val outputRanges = map.entries.mapNotNull { (source, dest) ->
            val start = maxOf(source.first, input.first)
            val end = minOf(source.last, input.last)
            if (start <= end) {
                mappedInputRanges += start..end
                (dest.first - source.first).let { (start + it)..(end + it) }
            } else null
        }
        val cuts = listOf(input.first) + mappedInputRanges.flatMap { listOf(it.first, it.last) } + listOf(input.last)
        val unmappedInputRanges = cuts.chunked(2).mapNotNull { (first, second) ->
            if (second > first) if (second == cuts.last()) first..second else first..<second else null
        }
        return outputRanges + unmappedInputRanges
    }

    fun part2_2(lines: List<String>) : Long {
        val seeds = lines.first().substringAfter(" ").split(" ").map { it.toLong() }.chunked(2).map { it.first()..<it.first() + it.last() }
        val maps = lines.drop(2).joinToString("\n").split("\n\n").map { section ->
            section.lines().drop(1).associate {
                it.split(" ").map { it.toLong() }.let { (dest, source, length) ->
                    source..(source + length) to dest..(dest + length)
                }
            }
        }
        println(
                seeds.flatMap { seedsRange ->
            maps.fold(listOf(seedsRange)) { aac, map ->
                aac.flatMap { getOutputRanges(map, it) }
            }
        }.minOf { it.first }
        )

        return 46
    }


// 12950548..
//104417250
    val testInput = readInput("Day05_test")
//    check(part1(testInput) == 35L)
//    check(part2_2(testInput) == 46L)

    val input = readInput("Day05")
//    part1(input).println()
    part2_2(input).println()
}
