#!/bin/bash
#
# Advent of Code 2022 Day 2
#

year=2022
day=02

WIN=6
DRAW=3
LOSS=0

solve() {
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        ((ans += vals["$line"]))
    done < "$1"
    echo "$ans"
    return 0
}

part1() {
    local -A vals=(
        [A X]=$DRAW+1
        [A Y]=$WIN+2
        [A Z]=$LOSS+3
        [B X]=$LOSS+1
        [B Y]=$DRAW+2
        [B Z]=$WIN+3
        [C X]=$WIN+1
        [C Y]=$LOSS+2
        [C Z]=$DRAW+3
    )
    solve "$1"
    return 0
}

part2() {
    local -A vals=(
        [A X]=$LOSS+3
        [A Y]=$DRAW+1
        [A Z]=$WIN+2
        [B X]=$LOSS+1
        [B Y]=$DRAW+2
        [B Z]=$WIN+3
        [C X]=$LOSS+2
        [C Y]=$DRAW+3
        [C Z]=$WIN+1
    )
    solve "$1"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    sample=(
        "A Y"
        "B X"
        "C Z"
    )

    TEST part1 sample 15
    TEST part2 sample 12
}

source "$(dirname "$0")/aoc_main.sh"
