#!/bin/bash
#
# Advent of Code 2016 Day 8
#

year=2016
day=08

ON="$(printf "%b" '\u2592')"
OFF=" "
declare -i width=50
declare -i height=6

solve() {
    local -a grid=()
    for ((i = 0; i < height; i++)); do
        local row=""
        for ((j = 0; j < width; j++)); do
            row+="$OFF"
        done
        grid+=("$row")
    done
    while read -r instruction || [ -n "$instruction" ]; do
        local re1="^rect ([0-9]*)x([0-9]*)$"
        [[ "$instruction" =~ $re1 ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            local -i a="${BASH_REMATCH[1]}"
            local -i b="${BASH_REMATCH[2]}"
            local on_part=""
            for ((i = 0; i < a; i++)); do
                on_part+="$ON"
            done
            for ((i = 0; i < b; i++)); do
                local row="${grid[$i]}"
                grid[$i]="$on_part""${row:$a}"
            done
            continue
        fi
        local re2="^rotate row y=([0-9]*) by ([0-9]*)$"
        [[ "$instruction" =~ $re2 ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            local -i a="${BASH_REMATCH[1]}"
            local -i b="${BASH_REMATCH[2]}"
            local row="${grid[$a]}"
            grid[$a]="${row:$width-$b}""${row:0:$width-$b}"
            continue
        fi
        local re3="^rotate column x=([0-9]*) by ([0-9]*)$"
        [[ "$instruction" =~ $re3 ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            local -i a="${BASH_REMATCH[1]}"
            local -i b="${BASH_REMATCH[2]}"
            local col=""
            for ((i = 0; i < height; i++)); do
                local row="${grid[$i]}"
                col+=${row:$a:1}
            done
            col="${col:$height-$b}""${col:0:$height-$b}"
            for ((i = 0; i < height; i++)); do
                local row="${grid[$i]}"
                grid[$i]="${row:0:$a}""${col:$i:1}""${row:$a+1}"
            done
            continue
        fi
        fatal 1 "Invalid input"
    done < "$1"
    for row in "${grid[@]}"; do
        echo "$row"
    done
    return 0
}

part1() {
    local -i ans=0
    mapfile -t grid < <(solve "$@")
    for row in "${grid[@]}"; do
        for ((i = 0; i < ${#row}; i++)); do
            [ "${row:$i:1}" = "$ON" ] && ((ans++))
        done
    done
    echo "$ans"
    return 0
}

source "$(dirname "$0")/aoc_ocr.sh"

part2() {
    mapfile -t grid < <(solve "$@")
    for row in "${grid[@]}"; do
        echo "$row" >&2
    done
    ocr_convert6 "$ON" "$OFF" "${grid[@]}"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "rect 3x2"
        "rotate column x=1 by 1"
        "rotate row y=0 by 4"
        "rotate column x=1 by 1"
        )
    }

    TEST part1 sample 6
}

source "$(dirname "$0")/aoc_main.sh"
