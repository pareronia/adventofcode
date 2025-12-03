#!/bin/bash
#
# Advent of Code 2025 Day 3
#

year=2025
day=03

solve_line() {
    local line="$1"
    local -i digits="$2"
    local -i pos=0

    for ((i = digits; i > 0; i--)); do
        best=0
        for ((j = pos; j < ${#line} - i + 1; j++)); do
            if ((${line:j:1} > best)); then
                best=${line:j:1}
                ((pos = j + 1))
            fi
        done
        echo -n "$best"
    done
    return 0
}

solve() {
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        ((ans += $(solve_line "$line" "$2")))
    done < "$1"
    echo "$ans"
    return 0
}

part1() {
    solve "$1" 2
    return 0
}

part2() {
    solve "$1" 12
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "987654321111111"
            "811111111111119"
            "234234234234278"
            "818181911112111"
        )
    }

    TEST part1 sample 357
    TEST part2 sample 3121910778619
}

source "$(dirname "$0")/aoc_main.sh"
