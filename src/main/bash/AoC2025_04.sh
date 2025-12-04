#!/bin/bash
#
# Advent of Code 2025 Day 4
#

year=2025
day=04

declare -a grid
H=0
W=0

find_removable() {
    local -a ans_removable=()
    for ((r = 0; r < H; r++)); do
        for ((c = 0; c < W; c++)); do
            if [ "${grid[r]:c:1}" = '@' ]; then
                local -i cnt=0
                for dr in {-1,0,1}; do
                    for dc in {-1,0,1}; do
                        local -i rr=$((r + dr))
                        local -i cc=$((c + dc))
                        ((rr == r && cc == c)) && continue
                        ((rr < 0 || rr >= H)) && continue
                        ((cc < 0 || cc >= W)) && continue
                        if [ "${grid[rr]:cc:1}" = '@' ]; then
                            ((cnt++))
                        fi
                    done
                done
                if [ "$cnt" -lt 4 ]; then
                    ans_removable+=("$r:$c")
                fi
            fi
        done
    done
    for row in "${ans_removable[@]}"; do
        echo "$row"
    done
}

part1() {
    mapfile -t grid < "$@"
    H=${#grid[@]}
    W=${#grid[0]}
    mapfile -t removable < <(find_removable)
    echo "${#removable[@]}"
    return 0
}

part2() {
    mapfile -t grid < "$@"
    H=${#grid[@]}
    W=${#grid[0]}
    local -i ans=0
    while true; do
        mapfile -t removable < <(find_removable)
        ((${#removable[@]} == 0)) && break
        ((ans += ${#removable[@]}))
        _debug "${#removable[@]}"
        for rem in "${removable[@]}"; do
            IFS=':' read -ra rc <<< "$rem"
            r="${rc[0]}"
            c="${rc[1]}"
            row="${grid[r]:0:c}.${grid[r]:c+1}"
            grid[r]="$row"
        done
        _debug "updated grid"
    done
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
