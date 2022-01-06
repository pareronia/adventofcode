#!/bin/bash
#
# Advent of Code 2021 Day 2
#

part1() {
    local commands=("$@")
    local ver=0
    local hor=0
    for ((i = 0; i < "${#commands[@]}"; i++)); do
        read -ra arr <<< "${commands[i]}"
        local dir="${arr[0]}"
        local amount="${arr[1]}"
        if [ "$dir" = "up" ]; then
            ver=$((ver - amount))
        elif [ "$dir" = "down" ]; then
            ver=$((ver + amount))
        elif [ "$dir" = "forward" ]; then
            hor=$((hor + amount))
        else
            exit 1
        fi
    done
    echo $((hor * ver))
    return 0
}

part2() {
    local commands=("$@")
    local ver=0
    local hor=0
    local aim=0
    for ((i = 0; i < "${#commands[@]}"; i++)); do
        read -ra arr <<< "${commands[i]}"
        local dir="${arr[0]}"
        local amount="${arr[1]}"
        if [ "$dir" = "up" ]; then
            aim=$((aim - amount))
        elif [ "$dir" = "down" ]; then
            aim=$((aim + amount))
        elif [ "$dir" = "forward" ]; then
            hor=$((hor + amount))
            ver=$((ver + aim * amount))
        else
            exit 1;
        fi
    done
    echo $((hor * ver))
    return 0
}

sample=(
"forward 5"
"down 5"
"forward 8"
"up 3"
"down 8"
"forward 2"
)

sample1="$(part1 "${sample[@]}")"
[ "$sample1" = 150 ] || { echo "Part 1 sample failed: $sample1" >&2; exit 1; }
sample2="$(part2 "${sample[@]}")"
[ "$sample2" = 900 ] || { echo "Part 2 sample failed: $sample2" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2021
day=02
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }
mapfile -t input < "$inputfile"

part1="$(part1 "${input[@]}")"
echo "Part 1: $part1"
part2="$(part2 "${input[@]}")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
