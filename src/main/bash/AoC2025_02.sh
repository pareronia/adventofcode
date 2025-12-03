#!/bin/bash
#
# Advent of Code 2025 Day 2
#

year=2025
day=02

solve() {
    local -i ans=0
    IFS=, read -ra ranges < "$1"
    for range in "${ranges[@]}"; do
        IFS='-' read -r lo hi <<< "$range"
        while read -r n; do
            ((ans += n))
        done <<< "$(seq "$lo" "$hi" | grep -E "$2")"
    done
    echo "$ans"
    return 0
}

solve_slow() {
    local -i ans=0
    IFS=, read -ra ranges < "$1"
    for range in "${ranges[@]}"; do
        IFS='-' read -r lo hi <<< "$range"
        _debug "$lo $hi"
        for ((n = lo; n <= hi; n++)); do
            [[ "$n" =~ $2 ]]
            [ "${#BASH_REMATCH[@]}" -ne 0 ] && ((ans += n))
        done
    done
    echo "$ans"
    return 0
}

part1() {
    solve "$1" "^([0-9]+)\\1$"
    return 0
}

part2() {
    solve "$1" "^([0-9]+)\\1+$"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample="11-22,95-115,998-1012,1188511880-1188511890,222220-222224,\
1698522-1698528,446443-446449,38593856-38593862,565653-565659,\
824824821-824824827,2121212118-2121212124"
    }

    TEST part1 sample 1227775554
    TEST part2 sample 4174379265
}

source "$(dirname "$0")/aoc_main.sh"
