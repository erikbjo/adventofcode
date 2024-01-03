/** --- Day 6: Wait For It --- */
/*
Time:      7  15   30
Distance:  9  40  200

This document describes three races:
    - The first race lasts 7 milliseconds. The record distance in this race is 9 millimeters.
    - The second race lasts 15 milliseconds. The record distance in this race is 40 millimeters.
    - The third race lasts 30 milliseconds. The record distance in this race is 200 millimeters.
 */
fun main() {
    fun part1(input: List<String>): Int {
        var procuct = 1
        var waysToWin = 0

        val times = input[0].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val distances = input[1].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

        for (i in times.indices) {
            val time = times[i]
            val distance = distances[i]

            // 1 ms waited = 1 mm/ms acceleration
            for (j in 1..time) {
                val distanceCovered = j * (time - j)

                if (distanceCovered > distance) {
                    waysToWin++
                }
            }
            procuct *= waysToWin
            waysToWin = 0
        }

        return procuct
    }

    fun part2(input: List<String>): Long {
        var waysToWin = 0L

        // add all times and distances together
        val times = input[0].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }
        val distances = input[1].substringAfter(":").trim().split(" ").filter { it.isNotEmpty() }

        val time = StringBuilder().apply { for (i in times) { append(i) } }.toString().toLong()
        val distance = StringBuilder().apply { for (i in distances) { append(i) } }.toString().toLong()

        var firstWinningTime = 0L
        for (j in 1..time) {
            val distanceCovered = j * (time - j)

            if (distanceCovered > distance) {
                print("Found first winning time: $j\n")
                firstWinningTime = j
                break
            }
        }

        var lastWinningTime = 0L
        for (j in time downTo 1) {
            val distanceCovered = j * (time - j)

            if (distanceCovered > distance) {
                print("Found last winning time: $j\n")
                lastWinningTime = j
                break
            }
        }

        waysToWin = lastWinningTime - firstWinningTime + 1
        print("Ways to win: $waysToWin\n")

        return waysToWin
    }

    val input = readInput("Day06")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")

}