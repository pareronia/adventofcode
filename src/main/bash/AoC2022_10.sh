#!/bin/bash
#
# Advent of Code 2022 Day 10
#

year=2022
day=10

ON="$(printf "%b" '\u2592')"
OFF=" "
program=()

parse() {
    local -i x=1
    while read -r line || [ -n "$line" ]; do
        if [[ "$line" == "noop" ]]; then
            program+=("$x")
        else
            program+=("$x")
            program+=("$x")
            ((x += ${line:5}))
        fi
    done < "$1"
}

getPixels() {
    parse "$1"
    local row=""
    for ((i = 0; i < "${#program[@]}"; i++)); do
        local -i check=$((program[i] - i % 40))
        if [[ "$check" -ge -1 && "$check" -le 1 ]]; then
            row+="$ON"
        else
            row+="$OFF"
        fi
        if [[ $((i % 40)) -eq 39 ]]; then
            echo "$row"
            row=""
        fi
    done
}

part1() {
    parse "$1"
    local -i ans=0
    local -a checks=(20 60 100 140 180 220)
    for i in "${checks[@]}"; do
        ((ans += i * program[i - 1]))
    done
    echo "$ans"
    return 0
}

source "$(dirname "$0")/aoc_ocr.sh"

part2() {
    mapfile -t grid < <(getPixels "$1")
    for row in "${grid[@]}"; do
        _debug "$row"
    done
    ocr_convert6 "$ON" "$OFF" "${grid[@]}"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "addx 15"
            "addx -11"
            "addx 6"
            "addx -3"
            "addx 5"
            "addx -1"
            "addx -8"
            "addx 13"
            "addx 4"
            "noop"
            "addx -1"
            "addx 5"
            "addx -1"
            "addx 5"
            "addx -1"
            "addx 5"
            "addx -1"
            "addx 5"
            "addx -1"
            "addx -35"
            "addx 1"
            "addx 24"
            "addx -19"
            "addx 1"
            "addx 16"
            "addx -11"
            "noop"
            "noop"
            "addx 21"
            "addx -15"
            "noop"
            "noop"
            "addx -3"
            "addx 9"
            "addx 1"
            "addx -3"
            "addx 8"
            "addx 1"
            "addx 5"
            "noop"
            "noop"
            "noop"
            "noop"
            "noop"
            "addx -36"
            "noop"
            "addx 1"
            "addx 7"
            "noop"
            "noop"
            "noop"
            "addx 2"
            "addx 6"
            "noop"
            "noop"
            "noop"
            "noop"
            "noop"
            "addx 1"
            "noop"
            "noop"
            "addx 7"
            "addx 1"
            "noop"
            "addx -13"
            "addx 13"
            "addx 7"
            "noop"
            "addx 1"
            "addx -33"
            "noop"
            "noop"
            "noop"
            "addx 2"
            "noop"
            "noop"
            "noop"
            "addx 8"
            "noop"
            "addx -1"
            "addx 2"
            "addx 1"
            "noop"
            "addx 17"
            "addx -9"
            "addx 1"
            "addx 1"
            "addx -3"
            "addx 11"
            "noop"
            "noop"
            "addx 1"
            "noop"
            "addx 1"
            "noop"
            "noop"
            "addx -13"
            "addx -19"
            "addx 1"
            "addx 3"
            "addx 26"
            "addx -30"
            "addx 12"
            "addx -1"
            "addx 3"
            "addx 1"
            "noop"
            "noop"
            "noop"
            "addx -9"
            "addx 18"
            "addx 1"
            "addx 2"
            "noop"
            "noop"
            "addx 9"
            "noop"
            "noop"
            "noop"
            "addx -1"
            "addx 2"
            "addx -37"
            "addx 1"
            "addx 3"
            "noop"
            "addx 15"
            "addx -21"
            "addx 22"
            "addx -6"
            "addx 1"
            "noop"
            "addx 2"
            "addx 1"
            "noop"
            "addx -10"
            "noop"
            "noop"
            "addx 20"
            "addx 1"
            "addx 2"
            "addx 2"
            "addx -6"
            "addx -11"
            "noop"
            "noop"
            "noop"
        )
    }

    TEST part1 sample 13140
    TEST getPixels sample $'▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  \n▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒ \n▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    \n▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     \n▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒\n▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒     '
}

source "$(dirname "$0")/aoc_main.sh"
