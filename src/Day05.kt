import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong
import kotlin.math.absoluteValue

/** --- Day 5: If You Give A Seed A Fertilizer --- */

fun main() {
    fun part1(input: List<String>): Long {
        val listOfFinalPaths = mutableListOf<Long>()

        val seeds =
            input.find { it.startsWith("seeds:") }!!.substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }
                .map { it.toLong() }

        val listOfMaps = getListOfMaps(input)

        /*
        1. destination range start
        2. source range start
        3. range length
         */

        for (seed in seeds) {
            var seedToSoil = seed

            for (map in listOfMaps) {
                for (line in map) {
                    val destinationRangeStart = line.split(" ")[0].toLong()
                    val sourceRangeStart = line.split(" ")[1].toLong()
                    val rangeLength = line.split(" ")[2].toLong()

                    if (seedToSoil in sourceRangeStart until sourceRangeStart + rangeLength) {
                        seedToSoil = destinationRangeStart + (seedToSoil - sourceRangeStart)
                        break
                    }
                }
            }

            listOfFinalPaths.add(seedToSoil)
        }

        return listOfFinalPaths.minOrNull()!!
    }

    /* Error when adding all seeds to list before doing the calculation
    Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	    at java.base/java.lang.Long.valueOf(Long.java:1204)
	    at Day05Kt.main$part2(Day05.kt:190)
	    at Day05Kt.main(Day05.kt:226)
	    at Day05Kt.main(Day05.kt)
     */

//    fun part2(input: List<String>): Long {
//        val listOfMaps = getListOfMaps(input)
//        val seedsInRangeForm =
//            input.find { it.startsWith("seeds:") }!!.substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }
//                .map { it.toLong() }
//
//
//        var seeds = mutableListOf<Long>()
//        var minLocation = Long.MAX_VALUE
//
//        for (i in seedsInRangeForm.indices step 2) {
//            val start = seedsInRangeForm[i]
//            val end = start + seedsInRangeForm[i + 1]
//            for (seed in start until end) {
//                if (seed !in seeds) seeds.add(seed)
//            }
//        }
//
//        for (seed in seeds) {
//            var seedToSoil = seed
//
//            for (map in listOfMaps) {
//                for (line in map) {
//                    val destinationRangeStart = line.split(" ")[0].toLong()
//                    val sourceRangeStart = line.split(" ")[1].toLong()
//                    val rangeLength = line.split(" ")[2].toLong()
//
//                    if (seedToSoil in sourceRangeStart until sourceRangeStart + rangeLength) {
//                        seedToSoil = destinationRangeStart + (seedToSoil - sourceRangeStart)
//                        break
//                    }
//                }
//            }
//
//            if (seedToSoil < minLocation) {
//                minLocation = seedToSoil
//            }
//        }
//
//        return minLocation
//    }

    val testInput = readInput("Day05test")
    check(part1(testInput).toInt() == 35)
//check(part2(testInput) == 2286)

    val input = readInput("Day05")
    print("Part 1: " + part1(input) + "\n")
    // print("Part 2: " + part2(input) + "\n")
    runBlocking {
        println("Part 2: ${part2(input)}\n")
    }

}

data class RangeMapEntry(val start: Long, val end: Long, val range: Long)

/**
 * This is a very slow solution, but it works.
 * Also goes into negative? Processed -2078000000 seeds
 * But got the correct answer
 */
suspend fun part2(input: List<String>): Long {
    val listOfMaps = getListOfMaps(input).map { map ->
        map.map { line ->
            val parts = line.split(" ").map { it.toLong() }
            RangeMapEntry(parts[0], parts[1], parts[2])
        }
    }

    val seedsInRangeForm = input.find { it.startsWith("seeds:") }!!
        .substringAfter(":")
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { it.toLong() }

    var totalSeeds = 0L
    for (i in seedsInRangeForm.indices step 2) {
        totalSeeds += (seedsInRangeForm[i + 1] - seedsInRangeForm[i]).absoluteValue // 12306371110
    }
    print("Total seeds: $totalSeeds\n")

    val minLocation = AtomicLong(Long.MAX_VALUE)
    val processedCount = AtomicInteger(0)

    coroutineScope {
        for (i in seedsInRangeForm.indices step 2) {
            val start = seedsInRangeForm[i]
            val end = start + seedsInRangeForm[i + 1]
            print("Processing seeds from $start to $end\n")

            launch {
                for (seed in start until end) {
                    var seedToSoil = seed
                    for (map in listOfMaps) {
                        seedToSoil = map.firstOrNull { (destStart, sourceStart, rangeLength) ->
                            seedToSoil in sourceStart until (sourceStart + rangeLength)
                        }?.let { (destStart, sourceStart, _) ->
                            destStart + (seedToSoil - sourceStart)
                        } ?: seedToSoil
                    }

                    minLocation.updateAndGet { currentMin ->
                        if (seedToSoil < currentMin) seedToSoil else currentMin
                    }

                    val count = processedCount.incrementAndGet()
                    if (count % 10000000 == 0) {
                        println("Processed $count seeds")
                    }
                }
            }
        }
    }

    return minLocation.get()
}

fun getListOfMaps(input: List<String>): List<List<String>> {
    val stsIndex = input.indexOfFirst { it.startsWith("seed-to-soil map:") }
    val stfIndex = input.indexOfFirst { it.startsWith("soil-to-fertilizer map:") }
    val ftwIndex = input.indexOfFirst { it.startsWith("fertilizer-to-water map:") }
    val wtlIndex = input.indexOfFirst { it.startsWith("water-to-light map:") }
    val lttIndex = input.indexOfFirst { it.startsWith("light-to-temperature map:") }
    val tthIndex = input.indexOfFirst { it.startsWith("temperature-to-humidity map:") }
    val htlIndex = input.indexOfFirst { it.startsWith("humidity-to-location map:") }

    val seedToSoilMap = input.subList(stsIndex + 1, stfIndex - 1)
    val soilToFertilizerMap = input.subList(stfIndex + 1, ftwIndex - 1)
    val fertilizerToWaterMap = input.subList(ftwIndex + 1, wtlIndex - 1)
    val waterToLightMap = input.subList(wtlIndex + 1, lttIndex - 1)
    val lightToTemperatureMap = input.subList(lttIndex + 1, tthIndex - 1)
    val temperatureToHumidityMap = input.subList(tthIndex + 1, htlIndex - 1)
    val humidityToLocationMap = input.subList(htlIndex + 1, input.size - 1)

    return listOf(
        seedToSoilMap,
        soilToFertilizerMap,
        fertilizerToWaterMap,
        waterToLightMap,
        lightToTemperatureMap,
        temperatureToHumidityMap,
        humidityToLocationMap
    )
}