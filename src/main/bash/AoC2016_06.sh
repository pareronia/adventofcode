#!/bin/bash
#
# Advent of Code 2016 Day 6
#

year=2016
day=06

solve() {
    local -i part="$1"; shift
    mapfile -t messages < "$1"
    local ans=""
    local -i width="${#messages[0]}"
    for ((i = 0; i < width; i++)); do
        local -A counters=()
        for message in "${messages[@]}"; do
            ((counters["${message:$i:1}"]++))
        done < "$1"
        local -i min=999999999999999999
        local -i max=-999999999999999999
        local min_idx="" max_idx=""
        for k in "${!counters[@]}"; do
            local -i count="${counters[$k]}"
            [ "$count" -lt "$min" ] && { min="$count"; min_idx="$k"; }
            [ "$count" -gt "$max" ] && { max="$count"; max_idx="$k"; }
        done
        [ "$part" -eq 1 ] && ans+="$max_idx"
        [ "$part" -eq 2 ] && ans+="$min_idx"
    done
    echo "$ans"
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
        sample=(
        "eedadn"
        "drvtee"
        "eandsr"
        "raavrd"
        "atevrs"
        "tsrnev"
        "sdttsa"
        "rasrtv"
        "nssdts"
        "ntnada"
        "svetve"
        "tesnvt"
        "vntsnd"
        "vrdear"
        "dvrsen"
        "enarar"
        )
    }

    TEST part1 sample "easter"
    TEST part2 sample "advent"
}

source "$(dirname "$0")/aoc_main.sh"
