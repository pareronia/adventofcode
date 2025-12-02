#!/bin/bash
#
# Advent of Code 2025 Day 1
#

year=2025
day=01

total=100

part1() {
    local -i dial=50
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        d=${line:0:1}
        amt=${line:1}
        if [ "$d" = 'R' ]; then
            ((dial += amt))
            while ((dial >= total)); do
                ((dial -= total))
            done
        else
            ((dial -= amt))
            while ((dial < 0)); do
                ((dial += total))
            done
        fi
        ((dial == 0)) && ((ans++))
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i dial=50
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        d=${line:0:1}
        amt=${line:1}
        if [ "$d" = 'R' ]; then
            div=$((amt / total))
            mod=$((amt % total))
            ((ans += div))
            ((dial + mod >= total)) && ((ans++))
            ((dial += amt))
            while ((dial >= total)); do
                ((dial -= total))
            done
        else
            div=$((-amt / -total))
            mod=$((-amt % -total))
            ((ans += div))
            ((dial != 0 && dial + mod <= 0)) && ((ans++))
            ((dial -= amt))
            while ((dial < 0)); do
                ((dial += total))
            done
        fi
        _debug "$line $dial $ans"
    done < "$1"
    echo "$ans"
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
