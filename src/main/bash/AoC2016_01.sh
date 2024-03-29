#!/bin/bash
#
# Advent of Code 2016 Day 1
#

year=2016
day=01

solve() {
    local -i part="$1" x=0 y=0 d_x=0 d_y=1
    declare -A seen
    IFS=', ' read -ra steps < "$2"
    for step in "${steps[@]}"; do
        local dir="${step:0:1}"
        local -i amount="${step:1}"
        case "$dir" in
            R)
                local -i new_d_x=$d_y new_d_y=$((-d_x))
                ;;
            L)
                local -i new_d_x=$((-d_y)) new_d_y=$d_x
                ;;
            *)
                fatal 1 "Invalid input"
                ;;
        esac
        d_x=$new_d_x d_y=$new_d_y
        for ((i=1; i <= amount; i++)); do
            x=$((x + d_x)) y=$((y + d_y))
            if [ "$part" = 2 ]; then
                if [ "${seen["$x,$y"]}" = 1 ]; then
                    break 2
                else
                    seen["$x,$y"]=1
                fi
            fi
        done
    done
    echo $(((x < 0 ? -x : x) + (y < 0 ? -y : y)))
    return 0
}

part1() {
    solve 1 "$@"
}

part2() {
    solve 2 "$@"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("R2, L3")
        sample2=("R2, R2, R2")
        sample3=("R5, L5, R5, R3")
        sample4=("R8, R4, R4, R8")
    }

    TEST part1 sample1 5
    TEST part1 sample2 2
    TEST part1 sample3 12
    TEST part2 sample4 4
}

source "$(dirname "$0")/aoc_main.sh"
