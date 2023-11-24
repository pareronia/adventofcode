#!/bin/bash
#
# Advent of Code 2017 Day 5
#

year=2017
day=05

part1() {
    local -i ans=0
    mapfile -t jumps < "$1"
    local -i size="${#jumps[@]}"
    local -i i=0
    while [ "$i" -lt "$size" ]; do
        local -i jump="${jumps[$i]}"
        jumps[i]=$((jump + 1))
        ((i += jump))
        ((ans++))
    done
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    mapfile -t jumps < "$1"
    local -i size="${#jumps[@]}"
    local -i i=0
    while [ "$i" -lt "$size" ]; do
        local -i jump="${jumps[$i]}"
        if [ "$jump" -lt 3 ]; then
            jumps[i]=$((jump + 1))
        else
            jumps[i]=$((jump - 1))
        fi
        ((i += jump))
        ((ans++))
    done
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "0"
        "3"
        "0"
        "1"
        "-3"
        )
    }

    TEST part1 sample 5
    TEST part2 sample 10
}

source "$(dirname "$0")/aoc_main.sh"
