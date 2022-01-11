#!/bin/bash
#
# Advent of Code 2021 Day 1
#

year=2021
day=01

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

tests() {
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

    TEST part1 sample 7
    TEST part2 sample 5
}

source "$(dirname "$0")/aoc_main.sh"
