#!/bin/bash
#
# Advent of Code 2015 Day 4
#

solve() {
    read -r input < "$1"
    local val="$input"
    local i=0
    local target="$2"
    local size="${#target}"
    while [ "${val:0:size}" != "$target" ]; do
        ((i++))
        local str2hash="$input""$i"
        val="$(echo -n "$str2hash" | md5sum | cut -d' ' -f1)"
        # if [ $((i % 100)) = 0 ]; then
        #     printf "\r%d: %s %s" "$i" "$str2hash" "$val" >&2
        # fi
    done
    echo "$i"
    return 0
}

part1() {
    solve "$1" "00000"
}

part2() {
    solve "$1" "000000"
}

# shellcheck disable=SC2034
{
    sample1=("abcdef")
    sample2=("pqrstuv")
}

# shellcheck source=SCRIPTDIR/aoc.sh
. "$(dirname "$0")/aoc.sh"

TEST part1 sample1 609043
TEST part1 sample2 1048970

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2015
day=04
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
