# bin/bash

# This script is a workaround for the fact that the Advent of Code website
# doesn't want puzzle inputs on GitHub.

for i in {1..24}
do
    wget https://adventofcode.com/2023/day/$i/input -O ./src/main/resources/day$i.txt
done