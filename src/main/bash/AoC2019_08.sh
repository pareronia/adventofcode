#!/bin/bash
#
# Advent of Code 2019 Day 8
#

year=2019
day=08

declare -i width=25
declare -i height=6

part1() {
    local -A cnt0s=()
    while read -r -n$((width * height)) layer && [ -n "$layer" ]; do
        local -i cnt0=0
        for ((j = 0; j < width * height; j++)); do
            local ch="${layer:$j:1}"
            [ "$ch" = "0" ] && ((cnt0++))
        done
        cnt0s[$layer]=$cnt0
    done < "$1"
    local -i min=999999999999999999
    local min_idx=""
    for k in "${!cnt0s[@]}"; do
        local -i count="${cnt0s[$k]}"
        [ "$count" -lt "$min" ] && { min="$count"; min_idx="$k"; }
    done
    local -i cnt1=0 cnt2=0
    for ((j = 0; j < width * height; j++)); do
        local ch="${min_idx:$j:1}"
        [ "$ch" = "1" ] && ((cnt1++))
        [ "$ch" = "2" ] && ((cnt2++))
    done
    echo $((cnt1 * cnt2))
    return 0
}

source "$(dirname "$0")/aoc_ocr.sh"

solve2() {
    local -i width="$1"; shift
    local -i height="$1"; shift
    local -A img=()
    while read -r -n$((width * height)) layer && [ -n "$layer" ]; do
        for ((j = 0; j < width * height; j++)); do
            local ch="${layer:$j:1}"
            [ -n "${img[$j]}" ] && continue
            [ "$ch" = "2" ] && continue
            img[$j]="$ch"
        done
    done < "$1"
    for ((i = 0; i < height; i++)); do
        local line=""
        for ((j = 0; j < width; j++)); do
            local -i idx=$((i * width + j))
            line+="${img[$idx]}"
        done
        echo "$line"
    done
}

part2() {
    mapfile -t lines < <(solve2 "$width" "$height" "$@")
    for line in "${lines[@]}"; do
        for ((i = 0; i < ${#line}; i++)); do
            ch="${line:$i:1}"
            [ "$ch" = "1" ] && printf "%b" '\u2592' >&2
            [ "$ch" = "0" ] && echo -n " " >&2
        done
        echo "" >&2
    done
    ocr_convert6 "1" "0" "${lines[@]}"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=("0222112222120000")
    }

    TEST "solve2 2 2" sample $'01\n10'
}

source "$(dirname "$0")/aoc_main.sh"
