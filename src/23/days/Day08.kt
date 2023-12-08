import kotlin.math.abs

/** --- Day 8: Haunted Wasteland --- */

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        val visitSequence = mutableListOf<Char>()
        input[0].forEach { visitSequence.add(it) }

        val start = input.indexOf(input.find { it.contains("AAA =") }!!)
        val end = input.indexOf(input.find { it.contains("ZZZ =") }!!)

        print("Start: $start\nEnd: $end\n")

        var current = start

        // visitSequence contains RLRLRLRLLR etc. Input[x] contains XYZ = (ABC, DEF). L = ABC, R = DEF
        // visitSequence continues after all inputs are exhausted and we are still not at the end

        while (current != end) {
            val line = input[current]
            val left = line.substringAfter(" = (").substringBefore(", ")
            val right = line.substringAfter(", ").substringBefore(")")

            if (visitSequence[0] == 'L') {
                current = input.indexOf(input.find { it.contains("$left =") }!!)
            } else if (visitSequence[0] == 'R') {
                current = input.indexOf(input.find { it.contains("$right =") }!!)
            }

            visitSequence.removeAt(0)

            if (visitSequence.isEmpty()) {
                input[0].forEach { visitSequence.add(it) }
            }

            sum++
        }

        return sum
    }

    /** --- Part Two --- */
    fun gcd(a: Long, b: Long): Long {
        return if (b == 0L) a else gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return abs(a * b) / gcd(a, b)
    }

    fun part2(input: List<String>): Long {
        val conn = input.drop(1)
            .mapNotNull { line ->
                val parts = line.split(" = ", "(", ", ", ")").filter { it.isNotBlank() }
                if (parts.size == 3) parts[0] to (parts[1] to parts[2]) else null
            }.toMap()

        val inst = input.first().toList()

        fun solveSteps(start: String): Int {
            var pos = start
            var idx = 0
            while (!pos.endsWith("Z")) {
                val d = inst[idx % inst.size]
                pos = conn[pos]?.let { if (d == 'L') it.first else it.second } ?: pos
                idx++
            }
            return idx
        }

        var ret: Long = 1
        conn.keys.filter { it.endsWith("A") }.forEach { start ->
            ret = lcm(ret, solveSteps(start).toLong())
        }

        return ret
    }

    val testInput = readInput("Day08test")
    check(part1(testInput) == 2)

    val input = readInput("Day08")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")

}