#!/bin/bash
#
# Advent of Code 2025 Day 5
#

year=2025
day=05

declare -a merged
declare -a ids

parse_input() {
    declare -a ranges
    local -i split=0
    while read -r line || [ -n "$line" ]; do
        [ -z "$line" ] && {
            split=1
            continue
        }
        ((split == 0)) && {
            ranges+=("$line")
            continue
        }
        ids+=("$line")
    done < "$1"
    # shellcheck disable=SC2207
    IFS=$'\n' sorted=($(sort --numeric-sort <<< "${ranges[*]}"))
    for rng in "${sorted[@]}"; do
        local -i lo="${rng%%-*}"
        local -i hi="${rng:${#lo}+1}"
        if ((${#merged[@]} == 0)); then
            merged+=("$rng")
            continue
        fi
        local last="${merged[-1]}"
        local -i last_lo="${last%%-*}"
        local -i last_hi="${last:${#last_lo}+1}"
        if (((last_lo <= lo && lo <= last_hi) || (\
            last_lo <= hi && hi <= last_hi) || (\
            lo <= last_lo && last_lo <= hi))); then
            local -i new_lo="$last_lo"
            local -i new_hi
            if ((last_hi > hi)); then
                ((new_hi = last_hi))
            else
                ((new_hi = hi))
            fi
            merged[-1]="$new_lo-$new_hi"
        else
            merged+=("$rng")
        fi
    done
    return 0
}

part1() {
    parse_input "$1"
    local -i ans=0
    for val in "${ids[@]}"; do
        for rng in "${merged[@]}"; do
            local -i lo="${rng%%-*}"
            local -i hi="${rng:${#lo}+1}"
            ((lo <= val && val <= hi)) && {
                ((ans++))
                break
            }
        done
    done
    echo "$ans"
    return 0
}

part2() {
    parse_input "$1"
    local -i ans=0
    for rng in "${merged[@]}"; do
        local -i lo="${rng%%-*}"
        local -i hi="${rng:${#lo}+1}"
        ((ans += hi - lo + 1))
    done
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "3-5"
            "10-14"
            "16-20"
            "12-18"
            ""
            "1"
            "5"
            "8"
            "11"
            "17"
            "32"
        )
    }

    TEST part1 sample 3
    TEST part2 sample 14
}

source "$(dirname "$0")/aoc_main.sh"
