#!/bin/bash
#
# Advent of Code 2021 Day 1
#

countIncreases() {
    window="$1"
    shift
    depths=("$@")
    size="${#depths[@]}"
    count=0
    for ((i = "$window"; i < size; i++))
    do
        if (("${depths[i]}" > "${depths[i-window]}")); then
            count=$((count + 1))
        fi
    done
    echo "$count"
}

part1() {
    countIncreases 1 "$@"
}

part2() {
    countIncreases 3 "$@"
}

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

inputfile="$(_aocd__inputFile 2021 01)" \
    || { echo "Input file not found: $inputfile"; exit 1; }
mapfile -t input < "$inputfile"

echo "Part 1: $(part1 "${input[@]}")"
echo "Part 2: $(part2 "${input[@]}")"
