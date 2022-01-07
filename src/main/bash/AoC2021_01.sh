#!/bin/bash
#
# Advent of Code 2021 Day 1
#

countIncreases() {
    window="$1"
    mapfile -t depths < "$2"
    count=0
    for ((i = "$window"; i < "${#depths[@]}"; i++))
    do
        if (("${depths[i]}" > "${depths[i-window]}")); then
            ((count++))
        fi
    done
    echo "$count"
}

part1() {
    countIncreases 1 "$1"
}

part2() {
    countIncreases 3 "$1"
}

# shellcheck disable=SC2034
sample=(
199
200
208
210
200
207
240
269
260
263
)

# shellcheck source=SCRIPTDIR/aoc.sh
. "$(dirname "$0")/aoc.sh"

TEST part1 sample 7
TEST part2 sample 5

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2021
day=01
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
