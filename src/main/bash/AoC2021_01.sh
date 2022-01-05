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

sample1="$(part1 "${sample[@]}")"
[ "$sample1" = 7 ] || { echo "Part 1 sample failed: $sample1" >&2; exit 1; }
sample2="$(part2 "${sample[@]}")"
[ "$sample2" = 5 ] || { echo "Part 2 sample failed: $sample2" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2021
day=01
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }
mapfile -t input < "$inputfile"

part1="$(part1 "${input[@]}")"
echo "Part 1: $part1"
part2="$(part2 "${input[@]}")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
