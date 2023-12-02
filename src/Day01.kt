/**
 * Day 1:
 * Create a two-digit number from the first digit and the last digit of a given input string.
 *
 * Sample:
 * 1abc2
 * pqr3stu8vwx
 * a1b2c3d4e5f
 * treb7uchet
 *
 * Part 2:
 * Some strings contain digits as text (nine, eight, seven, ...).
 *
 * two1nine
 * eightwothree
 * abcone2threexyz
 * xtwone3four
 * 4nineeightseven2
 * zoneight234
 * 7pqrstsixteen
 */

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        for (string in input) {
            val firstDigit = string.first { it.isDigit() }
            val lastDigit = string.last { it.isDigit() }
            val mergedInt = (firstDigit.toString() + lastDigit.toString()).toInt()
            sum += mergedInt
        }

        return sum
    }

    // Not solved, see alternative solution below
    fun part2(input: List<String>): Int {
        val numberWords = mapOf(
            "one" to "1", "two" to "2", "three" to "3", "four" to "4",
            "five" to "5", "six" to "6", "seven" to "7", "eight" to "8",
            "nine" to "9"
        )

        val numberWordRegex = Regex(numberWords.keys.joinToString("|"))

        var sum = 0

        for (string in input) {
            var replacedString = string

            val matches = numberWordRegex.findAll(string)
            for (match in matches) {
                val word = match.value
                val digit = numberWords[word]
                replacedString = replacedString.replace(word, digit!!)
            }

            val digits = replacedString.filter { it.isDigit() }
            if (digits.isNotEmpty()) {
                val firstDigit = digits.first().toString()
                val lastDigit = digits.last().toString()

                val mergedInt = (firstDigit + lastDigit).toInt()
                sum += mergedInt
            }
        }

        return sum
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01")
    // check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()


    // Alternative solution for part 2:
    // FROM https://github.com/Oziomajnr/AdventOfCodeSolutions/blob/main/solutions/src/2023/Day1.kt
    // @Oziomajnr
    val digitMap = (1..9).groupBy { it }.mapKeys { it.key.toString() }.mapValues { it.value.first() } +
            ("one,two,three,four,five,six,seven,eight,nine").split(",")
                .mapIndexed { index, value -> value to index + 1 }.toMap()

    val result = input.sumOf { line ->
        val matched = Regex("""(?=(\d|eight|one|two|three|four|five|six|seven|nine))""").findAll(line).toList()
        (digitMap[matched.first().groups[1]!!.value]!! * 10 + digitMap[matched.last().groups[1]!!.value]!!).toInt()
    }
    println(result)
}
