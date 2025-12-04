#!/bin/bash
#
# Advent of Code 2025 Day 4
#

year=2025
day=04

declare -A grid

read_grid() {
    local -i r=0
    while read -r row || [ -n "$row" ]; do
        for ((c = 0; c < ${#row}; c++)); do
            [ "${row:c:1}" = '@' ] && grid[$r:$c]='@'
        done
        ((r++))
    done < "$1"
}

is_removable() {
    local k="$1"
    local -i r="${k%%:*}"
    local -i c="${k:${#r}+1}"
    local -i cnt=0
    for dr in {-1,0,1}; do
        for dc in {-1,0,1}; do
            [ -n "${grid[$((r+dr)):$((c+dc))]}" ] && ((cnt++))
        done
    done
    ((cnt < 5 )) && return 0 || return 1
}

part1() {
    _debug "part 1"
    read_grid "$1"
    _debug "${#grid[@]} rolls"
    local -i ans=0;
    for k in "${!grid[@]}"; do
        if is_removable "$k"; then
            ((ans++))
        fi
    done
    echo "$ans"
    _debug "part 1 done"
    return 0
}

part2() {
    _debug "part 2"
    read_grid "$1"
    local -i ans=0
    while true; do
        _debug "${#grid[@]} rolls"
        local -i cnt=0;
        for k in "${!grid[@]}"; do
            if is_removable "$k"; then
                ((cnt++))
                unset "grid[$k]"
            fi
        done
        ((cnt == 0)) && break
        ((ans += cnt))
    done
    _debug "part 2 done"
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "..@@.@@@@."
            "@@@.@.@.@@"
            "@@@@@.@.@@"
            "@.@@@@..@."
            "@@.@@@@.@@"
            ".@@@@@@@.@"
            ".@.@.@.@@@"
            "@.@@@.@@@@"
            ".@@@@@@@@."
            "@.@.@@@.@."
        )
    }

    TEST part1 sample 13
    TEST part2 sample 43
}

source "$(dirname "$0")/aoc_main.sh"
