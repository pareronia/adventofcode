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
                exit 1
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
                exit 1
                ;;
        esac
    done < "$1"
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

sample1="$(part1 <(for line in "${sample[@]}"; do echo "$line"; done))"
[ "$sample1" = 150 ] || { echo "Part 1 sample failed: $sample1" >&2; exit 1; }
sample2="$(part2 <(for line in "${sample[@]}"; do echo "$line"; done))"
[ "$sample2" = 900 ] || { echo "Part 2 sample failed: $sample2" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2021
day=02
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
