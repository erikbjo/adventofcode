/**  --- Day 7: Camel Cards ---
 * A hand consists of five cards labeled one of A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2.
 * The relative strength of each card follows this order, where A is the highest, and 2 is the lowest.
 *
 * Every hand is exactly one type. From strongest to weakest, they are:
 * 1. Five of a kind
 * 2. Four of a kind
 * 3. Full house
 * 4. Three of a kind
 * 5. Two pair
 * 6. One pair
 * 7. High card
 *
 * If two hands have the same type, a second ordering rule takes effect. Start by comparing the first card in each hand.
 * If these cards are different, the hand with the stronger first card is considered stronger. If the first card in each
 * hand has the same label, however, then move on to considering the second card in each hand. If they differ, the hand
 * with the higher second card wins; otherwise, continue with the third card in each hand, then the fourth, then the fifth.
 *
 * To play Camel Cards, you are given a list of hands and their corresponding bid (your puzzle input). For example:
 * 32T3K 765
 * T55J5 684
 * KK677 28
 * KTJJT 220
 * QQQJA 483
 *
 * This example shows five hands; each hand is followed by its bid amount. Each hand wins an amount equal to its bid
 * multiplied by its rank, where the weakest hand gets rank 1, the second-weakest hand gets rank 2, and so on up to
 * the strongest hand. Because there are five hands in this example, the strongest hand will have rank 5 and its bid
 * will be multiplied by 5.
 */

data class Hand(
    val cards: List<String>,
    val bid: Int,
    val handRank: Int,
    val firstCardRank: Int,
    val secondCardRank: Int,
    val thirdCardRank: Int,
    val fourthCardRank: Int,
    val fifthCardRank: Int
) // Scuffed

val rankedHands = mapOf(
    "Five of a kind" to 1,
    "Four of a kind" to 2,
    "Full house" to 3,
    "Three of a kind" to 4,
    "Two pair" to 5,
    "One pair" to 6,
    "High card" to 7
)

fun main() {
    fun part1(input: List<String>): Int {
        var totalWinnings = 0

        val rankedCards = mapOf(
            "A" to 1,
            "K" to 2,
            "Q" to 3,
            "J" to 4,
            "T" to 5,
            "9" to 6,
            "8" to 7,
            "7" to 8,
            "6" to 9,
            "5" to 10,
            "4" to 11,
            "3" to 12,
            "2" to 13
        )

        var hands = mutableListOf<Hand>()

        for (line in input) {
            val hand = line.substringBefore(" ")
            val bid = line.substringAfter(" ").toInt()

            val handType = when {
                hand.groupingBy { it }.eachCount().any { it.value == 5 } -> "Five of a kind"
                hand.groupingBy { it }.eachCount().any { it.value == 4 } -> "Four of a kind"
                hand.groupingBy { it }.eachCount()
                    .let { counts -> counts.any { it.value == 3 } && counts.any { it.value == 2 } } -> "Full house"

                hand.groupingBy { it }.eachCount().any { it.value == 3 } -> "Three of a kind"
                hand.groupingBy { it }.eachCount().filter { it.value == 2 }.size == 2 -> "Two pair"
                hand.groupingBy { it }.eachCount().any { it.value == 2 } -> "One pair"
                else -> "High card"
            }

            val handRank = rankedHands[handType]!!

            val firstCardRank = rankedCards[hand[0].toString()]!!
            val secondCardRank = rankedCards[hand[1].toString()]!!
            val thirdCardRank = rankedCards[hand[2].toString()]!!
            val fourthCardRank = rankedCards[hand[3].toString()]!!
            val fifthCardRank = rankedCards[hand[4].toString()]!!

            hands.add(
                Hand(
                    hand.toList().map { it.toString() },
                    bid,
                    handRank,
                    firstCardRank,
                    secondCardRank,
                    thirdCardRank,
                    fourthCardRank,
                    fifthCardRank
                )
            )
        }

        hands = hands.sortedWith(
            compareBy({ it.handRank },
                { it.firstCardRank },
                { it.secondCardRank },
                { it.thirdCardRank },
                { it.fourthCardRank },
                { it.fifthCardRank })
        ).toMutableList()

        for (hand in hands) {
            totalWinnings += hand.bid * (hands.size - hands.indexOf(hand))
        }

        return totalWinnings
    }

    /**
     * Generates all possible combinations of cards in a hand with jokers. Helper function for part 2.
     */
    fun generateCombinations(hand: List<String>, possibleCards: List<String>): List<List<String>> {
        if (!hand.contains("J")) return listOf(hand)

        val combinations = mutableListOf<List<String>>()
        for (card in possibleCards) {
            val newHand = hand.map { if (it == "J") card else it }
            combinations.addAll(generateCombinations(newHand, possibleCards))
        }

        return combinations
    }

    /**
     * --- Part Two ---
     * To make things a little more interesting, the Elf introduces one additional rule.
     * Now, J cards are jokers - wildcards that can act like whatever card would make the hand the strongest type possible.
     *
     * To balance this, J cards are now the weakest individual cards, weaker than 2.
     * The other cards stay in the same order: A, K, Q, T, 9, 8, 7, 6, 5, 4, 3, 2, J.
     */
    fun part2(input: List<String>): Int {
        var totalWinnings = 0

        val rankedCards = mapOf(
            "A" to 1,
            "K" to 2,
            "Q" to 3,
            "T" to 4,
            "9" to 5,
            "8" to 6,
            "7" to 7,
            "6" to 8,
            "5" to 9,
            "4" to 10,
            "3" to 11,
            "2" to 12,
            "J" to 13
        )

        var hands = mutableListOf<Hand>()

        for (line in input) {
            val hand = line.substringBefore(" ")
            val bid = line.substringAfter(" ").toInt()

            val handType = when {
                hand.contains("J") -> {
                    val possibleCards = listOf("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2")
                    val jokerCombinations = generateCombinations(hand.toList().map { it.toString() }, possibleCards)
                    jokerCombinations.map { combination ->
                        when {
                            combination.groupingBy { it }.eachCount().any { it.value == 5 } -> "Five of a kind"
                            combination.groupingBy { it }.eachCount().any { it.value == 4 } -> "Four of a kind"
                            combination.groupingBy { it }.eachCount()
                                .let { counts -> counts.any { it.value == 3 } && counts.any { it.value == 2 } } -> "Full house"
                            combination.groupingBy { it }.eachCount().any { it.value == 3 } -> "Three of a kind"
                            combination.groupingBy { it }.eachCount().filter { it.value == 2 }.size == 2 -> "Two pair"
                            combination.groupingBy { it }.eachCount().any { it.value == 2 } -> "One pair"
                            else -> "High card"
                        }
                    }.minByOrNull { rankedHands[it]!! }!!
                }
                else -> {
                    when {
                        hand.groupingBy { it }.eachCount().any { it.value == 5 } -> "Five of a kind"
                        hand.groupingBy { it }.eachCount().any { it.value == 4 } -> "Four of a kind"
                        hand.groupingBy { it }.eachCount()
                            .let { counts -> counts.any { it.value == 3 } && counts.any { it.value == 2 } } -> "Full house"
                        hand.groupingBy { it }.eachCount().any { it.value == 3 } -> "Three of a kind"
                        hand.groupingBy { it }.eachCount().filter { it.value == 2 }.size == 2 -> "Two pair"
                        hand.groupingBy { it }.eachCount().any { it.value == 2 } -> "One pair"
                        else -> "High card"
                    }
                }
            }

            val handRank = rankedHands[handType]!!

            val firstCardRank = rankedCards[hand[0].toString()]!!
            val secondCardRank = rankedCards[hand[1].toString()]!!
            val thirdCardRank = rankedCards[hand[2].toString()]!!
            val fourthCardRank = rankedCards[hand[3].toString()]!!
            val fifthCardRank = rankedCards[hand[4].toString()]!!

            hands.add(
                Hand(
                    hand.toList().map { it.toString() },
                    bid,
                    handRank,
                    firstCardRank,
                    secondCardRank,
                    thirdCardRank,
                    fourthCardRank,
                    fifthCardRank
                )
            )
        }

        hands = hands.sortedWith(
            compareBy({ it.handRank },
                { it.firstCardRank },
                { it.secondCardRank },
                { it.thirdCardRank },
                { it.fourthCardRank },
                { it.fifthCardRank })
        ).toMutableList()

        for (hand in hands) {
            totalWinnings += hand.bid * (hands.size - hands.indexOf(hand))
        }

        return totalWinnings
    }

    val input = readInput("Day07")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")

}
