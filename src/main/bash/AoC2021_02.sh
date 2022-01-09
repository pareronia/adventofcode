#!/bin/bash
#
# Advent of Code 2021 Day 2
#

part1() {
    local ver=0
    local hor=0
    while read -r dir amount; do
        case "$dir" in
            up)
                ((ver -= amount))
                ;;
            down)
                ((ver += amount))
                ;;
            forward)
                ((hor += amount))
                ;;
            *)
                fatal 1 "Invalid input"
                ;;
            esac
    done < "$1"
    echo $((hor * ver))
    return 0
}

part2() {
    local ver=0
    local hor=0
    local aim=0
    while read -r dir amount; do
        case "$dir" in
            up)
                ((aim -= amount))
                ;;
            down)
                ((aim += amount))
                ;;
            forward)
                ((hor += amount))
                ((ver += aim * amount))
                ;;
            *)
                fatal 1 "Invalid input"
                ;;
        esac
    done < "$1"
    echo $((hor * ver))
    return 0
}

# shellcheck disable=SC2034
sample=(
"forward 5"
"down 5"
"forward 8"
"up 3"
"down 8"
"forward 2"
)

# shellcheck source=SCRIPTDIR/aoc.sh
. "$(dirname "$0")/aoc.sh"

TEST part1 sample 150
TEST part2 sample 900

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2021
day=02
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || fatal 1 "Input file not found: $inputfile"

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" || fatal 1 "$check"
