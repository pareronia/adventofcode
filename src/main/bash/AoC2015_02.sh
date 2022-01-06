#!/bin/bash
#
# Advent of Code 2015 Day 2
#

part1() {
    local tot=0
    IFS='x'; while read -r l w h; do
        local side1=$((2 * l * w))
        local min="$side1"
        local side2=$((2 * w * h))
        [ "$side2" -lt "$min" ] && min="$side2"
        local side3=$((2 * h * l))
        [ "$side3" -lt "$min" ] && min="$side3"
        ((tot += (side1 + side2 + side3 + min / 2)))
    done < "$1"
    echo "$tot"
    return 0
}

part2() {
    local tot=0
    IFS='x'; while read -r l w h; do
        local min=$((2 * (l + w)))
        local length=$((2 * (w + h)))
        [ "$length" -lt "$min" ] && min="$length"
        local length=$((2 * (h + l)))
        [ "$length" -lt "$min" ] && min="$length"
        ((tot += (min + l * w * h)))
    done < "$1"
    echo "$tot"
    return 0
}

sample1=("2x3x4")
sample2=("1x1x10")

sample="$(part1 <(for line in "${sample1[@]}"; do echo "$line"; done))"
[ "$sample" = 58 ] || { echo "Part 1 sample1 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample2[@]}"; do echo "$line"; done))"
[ "$sample" = 43 ] || { echo "Part 1 sample2 failed: $sample" >&2; exit 1; }
sample="$(part2 <(for line in "${sample1[@]}"; do echo "$line"; done))"
[ "$sample" = 34 ] || { echo "Part 2 sample1 failed: $sample" >&2; exit 1; }
sample="$(part2 <(for line in "${sample2[@]}"; do echo "$line"; done))"
[ "$sample" = 14 ] || { echo "Part 2 sample2 failed: $sample" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2015
day=02
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
