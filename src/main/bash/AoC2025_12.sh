#!/bin/bash
#
# Advent of Code 2025 Day 12
#

year=2025
day=12

part1() {
    declare -a regions shapes
    local -i area=0
    while read -r line || [ -n "$line" ]; do
        if [[ "$line" =~ ^([0-9]): ]]; then
            ((area = 0))
        elif [[ "$line" =~ ^([0-9]+)x([0-9]+):([[:space:]0-9]+)$ ]]; then
            local -i ra=$((BASH_REMATCH[1] * BASH_REMATCH[2]))
            regions+=("$ra${BASH_REMATCH[3]}")
        elif [[ -z "$line" ]]; then
            shapes+=("$area")
        else
            ((area += ${#line}))
        fi
    done < "$1"
    local -i ans=0
    for r in "${regions[@]}"; do
        read -ra rvals <<< "$r"
        local -i max_area=0
        for ((i = 1; i < ${#rvals[@]}; i++)); do
            ((max_area += shapes[i - 1] * rvals[i]))
        done
        ((max_area <= rvals[0])) && ((ans++))
    done
    echo "$ans"
    return 0
}

part2() {
    echo "🎄"
    return 0
}

source "$(dirname "$0")/aoc_main.sh"
