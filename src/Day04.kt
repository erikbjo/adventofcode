/**
 * Each card has two lists of numbers separated by a vertical bar (|): a list of winning numbers and then a list of
 * numbers you have. You organize the information into a table (your puzzle input).
 *
 * As far as the Elf has been able to figure out, you have to figure out which of the numbers you have appear in the
 * list of winning numbers. The first match makes the card worth one point and each match after the first doubles the
 * point value of that card.
 *
 * For example:
 *
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
 *
 * In the above example, card 1 has five winning numbers (41, 48, 83, 86, and 17) and eight numbers you have
 * (83, 86, 6, 31, 17, 9, 48, and 53). Of the numbers you have, four of them (48, 83, 17, and 86) are winning numbers!
 * That means card 1 is worth 8 points (1 for the first match, then doubled three times for each of the three matches
 * after the first).
 *
 * Card 2 has two winning numbers (32 and 61), so it is worth 2 points.
 * Card 3 has two winning numbers (1 and 21), so it is worth 2 points.
 * Card 4 has one winning number (84), so it is worth 1 point.
 * Card 5 has no winning numbers, so it is worth no points.
 * Card 6 has no winning numbers, so it is worth no points.
 * So, in this example, the Elf's pile of scratchcards is worth 13 points.
 *
 * Take a seat in the large pile of colorful cards.
 * How many points are they worth in total?
 */

var validStrings = mutableListOf<String>()

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0

        for (string in input) {
            val winningNumbers = string.substringBefore("|").trim().split(" ").filter { it.isNotEmpty() }
            val numbersYouHave = string.substringAfter("|").trim().split(" ").filter { it.isNotEmpty() }

            var points = 0

            for (number in numbersYouHave) {
                if (winningNumbers.contains(number)) {
                    if (points == 0) points = 1
                    else points *= 2
                }
            }

            sum += points
        }

        return sum
    }


    /**
     * Specifically, you win copies of the scratchcards below the winning card equal to the number of matches.
     * So, if card 10 were to have five matching numbers, you would win one copy each of cards 11, 12, 13, 14, and 15.
     *
     * Copies of scratchcards are scored like normal scratchcards and have the same card number as the card they copied.
     * So, if you win a copy of card 10, and it has 5 matching numbers, it would then win a copy of the same cards that
     * the original card 10 won: cards 11, 12, 13, 14, and 15. This process repeats until none of the copies cause you
     * to win any more cards. (Cards will never make you copy a card past the end of the table.)
     *
     * Note: Not efficient at all, but it works
     */
    fun part2(input: List<String>): Int {
        // Using global variable to avoid passing it around

        for (string in input) {
            recursivePart2(input, string)
        }

        return validStrings.size + input.size
    }


    val testInput = readInput("Day04test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    validStrings = mutableListOf() // Reset global variable

    val input = readInput("Day04")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")
}

fun recursivePart2(input: List<String>, string: String) {
//    print("         Checking string: $string, index: ${input.indexOf(string)}\n")

    val winningNumbers = string.substringBefore("|").trim().split(" ").filter { it.isNotEmpty() }
    val numbersYouHave = string.substringAfter("|").trim().split(" ").filter { it.isNotEmpty() }

    var points = 0
    val recursiveStrings = mutableListOf<String>()

    for (number in numbersYouHave) {
        if (winningNumbers.contains(number)) {
            points++
        }
    }

//    print("     Points: $points\n")

    for (i in 1..points) {
//        print("     Adding string: ${input[input.indexOf(string) + i]}\n")
        validStrings.add(input[input.indexOf(string) + i])
        recursiveStrings.add(input[input.indexOf(string) + i])
    }

    for (recursiveString in recursiveStrings) {
        recursivePart2(input, recursiveString)
    }
}
