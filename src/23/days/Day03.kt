import java.util.regex.Pattern

/**
 * The engine schematic (your puzzle input) consists of a visual representation of the engine.
 * There are lots of numbers and symbols you don't really understand, but apparently any number adjacent to a symbol,
 * even diagonally, is a "part number" and should be included in your sum. (Periods (.) do not count as a symbol.)
 *
 * Here is an example engine schematic:
 *
 * 467..114..
 * ...*......
 * ..35..633.
 * ......#...
 * 617*......
 * .....+.58.
 * ..592.....
 * ......755.
 * ...$.*....
 * .664.598..
 *
 * In this schematic, two numbers are not part numbers because they are not adjacent to a symbol: 114 (top right)
 * and 58 (middle right). Every other number is adjacent to a symbol and so is a part number; their sum is 4361.
 *
 * Of course, the actual engine schematic is much larger.
 * What is the sum of all of the part numbers in the engine schematic?
 */

fun main() {
    fun checkSurroundingSymbols(input: List<String>, string: String, index: Int): Boolean {
        var surroundingSymbolsAreFound = false

        // type safe
        val surroundingSymbols = mutableListOf<Char?>()

        // Left
        if (index - 1 >= 0) surroundingSymbols.add(string[index - 1])

        // Right
        if (index + 1 < string.length) surroundingSymbols.add(string[index + 1])

        // Up
        if (input.indexOf(string) - 1 >= 0) {
            val lineAbove = input[input.indexOf(string) - 1]
            // Middle
            if (index < lineAbove.length) surroundingSymbols.add(lineAbove[index])
            // Left
            if (index - 1 >= 0) surroundingSymbols.add(lineAbove[index - 1])
            // Right
            if (index + 1 < lineAbove.length) surroundingSymbols.add(lineAbove[index + 1])
        }

        // Down
        if (input.indexOf(string) + 1 < input.size) {
            val lineBelow = input[input.indexOf(string) + 1]
            // Middle
            if (index < lineBelow.length) surroundingSymbols.add(lineBelow[index])
            // Left
            if (index - 1 >= 0) surroundingSymbols.add(lineBelow[index - 1])
            // Right
            if (index + 1 < lineBelow.length) surroundingSymbols.add(lineBelow[index + 1])
        }

        for (symbol in surroundingSymbols) {
            if (symbol != null && !symbol.isDigit() && symbol != '.') {
                surroundingSymbolsAreFound = true
                break
            }
        }

        return surroundingSymbolsAreFound
    }

    fun part1(input: List<String>): Int {
        var sum = 0

        // Iterate over each line, then each character in the line
        // If the character is a digit, start a search for surrounding digits
        // After finding all surrounding digits, add them to the total sum if a surrounding symbol is found

        // Problem: indexOf returns the first index of the character, not the current index
        // Solution: Keep track of the indexes of the current digits, then check the surrounding symbols of each index

        for (string in input) {
            var currentNumber = ""
            var currentNumberIndex = 0  // "Current" but not really
            var indexesOfCurrentDigits = mutableListOf<Int>()

            for (char in string) {
                currentNumberIndex += 1
                if (char.isDigit()) {
                    currentNumber += char
                    indexesOfCurrentDigits.add(currentNumberIndex - 1)
                }

                if (char == '.') {
                    continue
                }

                if (currentNumberIndex >= string.length || !(string[currentNumberIndex].isDigit())) {
                    // Valid symbols are everything except for periods and digits
                    var surroundingSymbolsAreFound = false
                    for (index in indexesOfCurrentDigits) {
                        if (checkSurroundingSymbols(input, string, index)) {
                            surroundingSymbolsAreFound = true
                            break
                        }
                    }

                    if (surroundingSymbolsAreFound) {
                        sum += currentNumber.toInt()
                        currentNumber = ""
                        indexesOfCurrentDigits = mutableListOf()
                    } else {
                        currentNumber = ""
                        indexesOfCurrentDigits = mutableListOf()
                    }
                }
            }
        }

        return sum
    }

    fun part2(input: List<String>): Long {
        val gearMap = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        val pattern = Pattern.compile("\\d+")

        for (r in input.indices) {
            val matcher = pattern.matcher(input[r])
            while (matcher.find()) {
                val number = matcher.group().toInt()
                val start = matcher.start()
                val end = matcher.end()

                for (i in r - 1..r + 1) {
                    for (j in start - 1..end) {
                        if (i in input.indices && j in input[i].indices && input[i][j] == '*') {
                            gearMap.getOrPut(Pair(i, j)) { mutableListOf() }.add(number)
                        }
                    }
                }
            }
        }

        return gearMap.values.filter { it.size == 2 }.sumOf { it[0].toLong() * it[1] }
    }


    val input = readInput("Day03")
    print("Part 1: " + part1(input) + "\n")
    print("Part 2: " + part2(input) + "\n")
}