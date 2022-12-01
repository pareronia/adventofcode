#!/bin/bash
#
# Advent of Code 2022 Day 1
#

year=2022
day=01

solve() {
    count="$1"
    local -a sums
    local -i sum=0
    while read -r line; do
        if [ -z "$line" ]; then
            sums+=("$sum")
            sum=0
        else
            ((sum += line))
        fi
    done < "$2"
    sums+=("$sum")
    # shellcheck disable=2207
    IFS=$'\n' sorted=($(sort --numeric-sort --reverse <<< "${sums[*]}"))
    unset IFS
    local -i ans=0
    for ((i = 0; i < "$count"; i++)); do
        ((ans += sorted[i]))
    done
    echo "$ans"
}

part1() {
    solve 1 "$1"
}

part2() {
    solve 3 "$1"
}

tests() {
    # shellcheck disable=SC2034
    sample=(
        "1000"
        "2000"
        "3000"
        ""
        "4000"
        ""
        "5000"
        "6000"
        ""
        "7000"
        "8000"
        "9000"
        ""
        "10000"
    )

    TEST part1 sample 24000
    TEST part2 sample 45000
}

source "$(dirname "$0")/aoc_main.sh"
