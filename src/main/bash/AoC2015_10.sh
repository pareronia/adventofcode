#!/bin/bash
#
# Advent of Code 2015 Day 10
#

year=2015
day=10

solve() {
    local -i n="$1"
    read -r str < "$2"
    for ((i = 0; i < n; i++)); do
        str=$(fold --width=1 <<< "$str" | uniq --count | tr --delete '\n ')
    done
    echo "$str"
    return 0
}

part1() {
    local ans
    ans="$(solve 40 "$@" | wc --chars)"
    echo $((ans - 1))
    return 0
}

part2() {
    local ans
    ans="$(solve 50 "$@" | wc --chars)"
    echo $((ans - 1))
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "1"
        )
    }

    TEST "solve 5" sample "312211"
}

source "$(dirname "$0")/aoc_main.sh"
