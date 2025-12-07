#!/bin/bash
#
# Advent of Code 2025 Day 7
#

year=2025
day=07

solve() {
    local -i part="$1"

    declare -a dp
    local -i ans1=0
    local -i first=1
    while read -r line || [ -n "$line" ]; do
        if ((first == 1)); then
            for ((i = 0; i < "${#line}"; i++)); do
                if [ "${line:i:1}" = 'S' ]; then
                    dp+=(1)
                else
                    dp+=(0)
                fi
            done
            ((first = 0))
        else
            for ((i = 0; i < "${#line}"; i++)); do
                if [ "${line:i:1}" = '^' ]; then
                    ((dp[i] > 0)) && ((ans1++))
                    ((dp[i - 1] += dp[i]))
                    ((dp[i + 1] += dp[i]))
                    ((dp[i] = 0))
                fi
            done
        fi
    done < "$2"

    if ((part == 1)); then
        echo "$ans1"
    else
        local -i ans2=0
        for n in "${dp[@]}"; do
            ((ans2 += n))
        done
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
    {
        sample=(
            ".......S......."
            "..............."
            ".......^......."
            "..............."
            "......^.^......"
            "..............."
            ".....^.^.^....."
            "..............."
            "....^.^...^...."
            "..............."
            "...^.^...^.^..."
            "..............."
            "..^...^.....^.."
            "..............."
            ".^.^.^.^.^...^."
            "..............."
        )
    }

    TEST part1 sample 21
    TEST part2 sample 40
}

source "$(dirname "$0")/aoc_main.sh"
