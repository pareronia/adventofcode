#!/bin/bash
#
# Advent of Code 2022 Day 4
#

year=2022
day=04

RE="([0-9]+)-([0-9]+),([0-9]+)-([0-9]+)"

solve() {
    local -i part="$1"
    local -i ans1=0
    local -i ans2=0
    while read -r line || [ -n "$line" ]; do
        [[ "$line" =~ $RE ]]
        local -i a="${BASH_REMATCH[1]}"
        local -i b="${BASH_REMATCH[2]}"
        local -i c="${BASH_REMATCH[3]}"
        local -i d="${BASH_REMATCH[4]}"
        if [[ "$part" == 1 ]]; then
            [[ (a -le c && b -ge d) || (c -le a && d -ge b) ]] && ((ans1++))
        else
            [[ b -ge c && d -ge a ]] && ((ans2++))
        fi
    done < "$2"
    if [[ "$part" == 1 ]]; then
        echo "$ans1"
    else
        echo "$ans2"
    fi
    return 0
}

part1() {
    solve 1 "$1"
    return 0
}

part2() {
    solve 2 "$1"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    sample=(
        "2-4,6-8"
        "2-3,4-5"
        "5-7,7-9"
        "2-8,3-7"
        "6-6,4-6"
        "2-6,4-8"
    )

    TEST part1 sample 2
    TEST part2 sample 4
}

source "$(dirname "$0")/aoc_main.sh"
