/**
 * Day 2:
 * Which games would have been possible if the bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes?
 * What is the sum of the IDs of those games?
 *
 * Sample:
 * Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
 * Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
 * Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
 * Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
 * Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
 *
 * Part 2:
 * The power of a set of cubes is equal to the numbers of red, green, and blue cubes multiplied together.
 * The power of the minimum set of cubes in game 1 is 48. In games 2-5 it was 12, 1560, 630, and 36, respectively.
 * Adding up these five powers produces the sum 2286.
 *
 * For each game, find the minimum set of cubes that must have been present. What is the sum of the power of these sets?
 */

val maxRed = 12
val maxGreen = 13
val maxBlue = 14

fun main() {
    fun part1(input: List<String>): Int {
        var sumOfGameIDs = 0

        for (string in input) {
            val game = string.split(":")
            val gameID = game[0].split(" ")[1].toInt()

            var isGameValid = true

            val subsets = game[1].split(";")
            for (subset in subsets) {
                var sumOfRed = 0
                var sumOfGreen = 0
                var sumOfBlue = 0

                val colors = subset.split(",")
                for (color in colors) {
                    val colorParts = color.trim().split(" ")
                    val colorCount = colorParts[0].toInt()
                    val colorName = colorParts[1]

                    when (colorName) {
                        "red" -> sumOfRed += colorCount
                        "green" -> sumOfGreen += colorCount
                        "blue" -> sumOfBlue += colorCount
                    }
                }

                if (sumOfRed > maxRed || sumOfGreen > maxGreen || sumOfBlue > maxBlue) {
                    isGameValid = false
                    break
                }
            }

            if (isGameValid) {
                sumOfGameIDs += gameID
            }
        }

        return sumOfGameIDs
    }


    fun part2(input: List<String>): Int {
        var sumOfPowers = 0

        for (string in input) {
            var maxOfRed = 0
            var maxOfGreen = 0
            var maxOfBlue = 0

            val subsets = string.split(":")[1].split(";")
            for (subset in subsets) {

                val colors = subset.split(",")
                for (color in colors) {
                    val colorParts = color.trim().split(" ")
                    val colorCount = colorParts[0].toInt()
                    val colorName = colorParts[1]

                    when (colorName) {
                        "red" -> {
                            if (colorCount > maxOfRed) {
                                maxOfRed = colorCount
                            }
                        }

                        "green" -> {
                            if (colorCount > maxOfGreen) {
                                maxOfGreen = colorCount
                            }
                        }

                        "blue" -> {
                            if (colorCount > maxOfBlue) {
                                maxOfBlue = colorCount
                            }
                        }
                    }
                }

            }
            sumOfPowers += maxOfRed * maxOfGreen * maxOfBlue

        }

        return sumOfPowers
    }

    val input = readInput("Day02")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")
}
