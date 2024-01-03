/**
 * | is a vertical pipe connecting north and south.
 * - is a horizontal pipe connecting east and west.
 * L is a 90-degree bend connecting north and east.
 * J is a 90-degree bend connecting north and west.
 * 7 is a 90-degree bend connecting south and west.
 * F is a 90-degree bend connecting south and east.
 * . is ground; there is no pipe in this tile.
 * S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show
 * what shape the pipe has.
 */
fun main() {
    /**
     * Find the single giant loop starting at S. How many steps along the loop does it take
     * to get from the starting position to the point farthest from the starting position?
     */
    // Credit to @elizarov
    fun part1(input: List<String>): Int {
        val di = arrayOf(0, 1, 0, -1)
        val dj = arrayOf(1, 0, -1, 0)
        val db = arrayOf(2, 3, 0, 1)
        val ps = "|-LJ7F".toCharArray()
        val cs = arrayOf(
            "0101", "1010", "1001", "0011", "0110", "1100"
        ).map { it.reversed().toInt(2) }

        val a = buildList {
            val emptyLine = (1..input.first().length + 2).map { '.' }.joinToString("")
            add(emptyLine)
            addAll(input.map { ".$it." })
            add(emptyLine)
        }.toTypedArray()

        val n = a.size
        val m = a[0].length

        data class P2(val x: Int, val y: Int)

        var result = 0
        for (i0 in a.indices) {
            for (j0 in a[i0].indices) {
                if (a[i0][j0] == 'S') {
                    var i = i0
                    var j = j0
                    var cc = 15
                    var pi = -1
                    var pj = -1
                    val loop = ArrayList<P2>()
                    loop@ while (true) {
                        loop += P2(i, j)
                        var found = false
                        for (d in 0..3) {
                            if (((1 shl d) and cc) != 0) {
                                val i1 = i + di[d]
                                val j1 = j + dj[d]
                                if (i1 !in 0 until n || j1 !in 0 until m)
                                    continue

                                if (i1 == pi && j1 == pj)
                                    continue

                                if (i1 == i0 && j1 == j0)
                                    break@loop

                                val k = ps.indexOf(a[i1][j1])
                                if (k < 0)
                                    continue

                                val cn = cs[k]
                                if (((1 shl db[d]) and cn) == 0)
                                    continue

                                pi = i
                                pj = j
                                i = i1
                                j = j1
                                cc = cn
                                found = true
                                break
                            }
                        }
                        check(found) { "($i,$j) -> ${a[i][j]}" }
                    }
                    result = loop.size / 2
                    break
                }
            }
        }
        return result
    }

    fun part2(input: List<String>): Long {
        var sum = 0L
        return sum
    }


    val input = readInput("Day10")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")

}