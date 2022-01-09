#!/bin/bash
#
# Advent of Code 2016 Day 3
#

part1() {
    local -i valid=0
    while read -r t; do
        OLDIFS="$IFS"; IFS=' '
        # shellcheck disable=SC2206
        local -a fields=($t)
        IFS="$OLDIFS"
        local -i t1="${fields[0]}" t2="${fields[1]}" t3="${fields[2]}"
        ((t1 + t2 > t3 && t1 + t3 > t2 && t2 + t3 > t1)) && ((valid++))
    done < "$1"
    echo "$valid"
}

part2() {
    local -i valid=0
    while read -r line1 && read -r line2 && read -r line3; do
        OLDIFS="$IFS"; IFS=' '
        # shellcheck disable=SC2206
        local -a t1=($line1) t2=($line2) t3=($line3)
        IFS="$OLDIFS"
        ((t1[0] + t2[0] > t3[0] && t1[0] + t3[0] > t2[0] \
            && t2[0] + t3[0] > t1[0])) && ((valid++))
        ((t1[1] + t2[1] > t3[1] && t1[1] + t3[1] > t2[1] \
            && t2[1] + t3[1] > t1[1])) && ((valid++))
        ((t1[2] + t2[2] > t3[2] && t1[2] + t3[2] > t2[2] \
            && t2[2] + t3[2] > t1[2])) && ((valid++))
    done < "$1"
    echo "$valid"
}

# shellcheck disable=SC2034
{
    sample1=("5 10 25")
    sample2=("3 4 5")
    sample3=(
    "5 3 6"
    "10 4 8"
    "25 5 10"
    )
}

# shellcheck source=SCRIPTDIR/aoc.sh
. "$(dirname "$0")/aoc.sh"

TEST part1 sample1 0
TEST part1 sample2 1
TEST part2 sample3 2

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2016
day=03
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || fatal 1 "Input file not found: $inputfile"

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" || fatal 1 "$check"
