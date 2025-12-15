#!/bin/bash
#
# Advent of Code 2025 Day 1
#

year=2025
day=01

TOTAL=100
LANDED_ON_ZERO=0
PASSED_BY_ZERO=1

solve() {
    local -i mode="$2"
    local -i dial=50
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        local d=${line:0:1}
        local -i rotation=${line:1}
        if [ "$d" = 'L' ]; then
            rotation=-rotation
        fi
        if ((mode == LANDED_ON_ZERO)); then
            (((dial + rotation) % TOTAL == 0)) && ((ans += 1))
        elif ((mode == PASSED_BY_ZERO)); then
            if ((rotation >= 0)); then
                ((ans += (dial + rotation) / TOTAL))
            else
                ((ans += ((TOTAL - dial) % TOTAL - rotation) / TOTAL))
            fi
        fi
        ((dial += rotation))
        while ((dial < 0)); do
            ((dial += TOTAL))
        done
        while ((dial >= TOTAL)); do
            ((dial -= TOTAL))
        done
    done < "$1"
    echo "$ans"
    return 0
}

part1() {
    solve "$1" LANDED_ON_ZERO
    return 0
}

part2() {
    solve "$1" PASSED_BY_ZERO
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "L68"
            "L30"
            "R48"
            "L5"
            "R60"
            "L55"
            "L1"
            "L99"
            "R14"
            "L82"
        )
    }

    TEST part1 sample 3
    TEST part2 sample 6
}

source "$(dirname "$0")/aoc_main.sh"
