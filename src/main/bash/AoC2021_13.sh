#!/bin/bash
#
# Advent of Code 2021 Day 13
#

year=2021
day=13

ON="$(printf "%b" '\u2592')"
OFF=" "

declare -A points=()

solve() {
    local -i part="$1"; shift
    while read -r line || [ -n "$line" ]; do
        [[ "$line" =~ ^([0-9]+),([0-9]+)$ ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            local -i x="${BASH_REMATCH[1]}"
            local -i y="${BASH_REMATCH[2]}"
            points["$line"]="$ON"
            continue
        fi
        [[ "$line" =~ ^'fold along '([xy])=([0-9]+)$ ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            local axis="${BASH_REMATCH[1]}"
            local -i amount="${BASH_REMATCH[2]}"
            for k in "${!points[@]}"; do
                local -i x="${k%,*}"
                local -i y="${k#*,}"
                if [ "$axis" = "x" ] && [ "$x" -gt "$amount" ]; then
                    local new_k="$((x - 2 * (x - amount))),$y"
                elif [ "$axis" = "y" ] && [ "$y" -gt "$amount" ]; then
                    local new_k="$x,$((y - 2 * (y - amount)))"
                else
                    continue
                fi
                points["$new_k"]="$ON"
                unset "points[""$k""]"
            done
            [ "$part" -eq 1 ] && break
        fi
    done < "$1"
    return 0
}

part1() {
    points=()
    solve 1 "$@"
    echo "${#points[@]}"
    return 0
}

solve2() {
    points=()
    solve 2 "$@"
    local -i max_x=-999999999999999999
    local -i max_y=-999999999999999999
    for k in "${!points[@]}"; do
        local -i x="${k%,*}"
        local -i y="${k#*,}"
        [ "$x" -gt "$max_x" ] && max_x="$x"
        [ "$y" -gt "$max_y" ] && max_y="$y"
    done
    local -a grid=()
    for ((y = 0; y <= max_y; y++)); do
        local row=""
        for ((x = 0; x <= max_x + 1; x++)); do
            if [ -n "${points["$x,$y"]}" ]; then
                row+="$ON"
            else
                row+="$OFF"
            fi
        done
        grid+=("$row")
    done
    for row in "${grid[@]}"; do
        echo "$row"
    done
    return 0
}

source "$(dirname "$0")/aoc_ocr.sh"

part2() {
    mapfile -t grid < <(solve2 "$@")
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
        "6,10"
        "0,14"
        "9,10"
        "0,3"
        "10,4"
        "4,11"
        "6,0"
        "6,12"
        "4,1"
        "0,13"
        "10,12"
        "3,4"
        "3,0"
        "8,4"
        "1,10"
        "2,14"
        "8,10"
        "9,0"
        ""
        "fold along y=7"
        "fold along x=5"
        )
    }

    TEST part1 sample 17
    TEST solve2 sample $'▒▒▒▒▒ \n▒   ▒ \n▒   ▒ \n▒   ▒ \n▒▒▒▒▒ '
}

source "$(dirname "$0")/aoc_main.sh"
