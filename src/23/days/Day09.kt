/**
 * To best protect the oasis, your environmental report should include a prediction of the next value in each history.
 * To do this, start by making a new sequence from the difference at each step of your history. If that sequence is not
 * all zeroes, repeat this process, using the sequence you just generated as the input sequence. Once all the values
 * in your latest sequence are zeroes, you can extrapolate what the next value of the original history should be.
 *
 * Example input:
 * 0 3 6 9 12 15
 * 1 3 6 10 15 21
 * 10 13 16 21 30 45
 *
 * The third history requires even more sequences, but its next value can be found the same way:
 * 10 13 16 21 30 45 _ 68
 *     3 3 5 9 15 _ 23
 *       0 2 4 6 _ 8
 *        2 2 2 _ 2
 *         0 0 _ 0
 * So, the next value of the third history is 68.
 */
fun main() {
    fun getEstimatedNextValue(input: List<Int>): Int {
        var estNext = 0
        var currentArray = input
        val arrayOfVisitedArrays = mutableListOf<List<Int>>()

        while (!currentArray.all { it == 0 }) {
            arrayOfVisitedArrays.add(currentArray)
            val nextArray = mutableListOf<Int>()
            for (i in 0 until currentArray.size - 1) {
                nextArray.add(currentArray[i + 1] - currentArray[i])
            }
            currentArray = nextArray
        }

        arrayOfVisitedArrays.add(currentArray)

        for (i in (arrayOfVisitedArrays.size) downTo 1) {
            estNext += if (i - 2 < 0) 0 else arrayOfVisitedArrays[i - 2].last()
        }

        return estNext
    }

    fun part1(input: List<String>): Long {
        var sum = 0L

        for (i in input) {
            sum += getEstimatedNextValue(i.split(" ").map { it.toInt() }.toMutableList())
        }

        return sum
    }

    /**
     * For part 2 it is needed to extrapolate back in time also, so that estimate the previous value in the sequence.
     * Like this:
     * 5  10  13  16  21  30  45
     *   5   3   3   5   9  15
     *    -2   0   2   4   6
     *       2   2   2   2
     *         0   0   0
     * 5 is the previous value in the sequence.
     *
     * (Just reverse the input list and use the same function as in part 1)
     */

    fun part2(input: List<String>): Long {
        var sum = 0L

        for (i in input) {
            sum += getEstimatedNextValue(i.split(" ").map { it.toInt() }.toMutableList().reversed())
        }

        return sum
    }

    val input = readInput("Day09")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")

}