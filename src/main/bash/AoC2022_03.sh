#!/bin/bash
#
# Advent of Code 2022 Day 3
#

year=2022
day=03

ord() {
    LC_CTYPE=C printf '%d' "'$1"
}

priority() {
    local -i asc
    asc="$(ord "$1")"
    if [[ asc -gt "$(ord "a")" ]]; then
        echo $((asc - "$(ord "a")" + 1))
    else
        echo $((asc - "$(ord "A")" + 27))
    fi
    return 0
}

part1() {
    local -i ans=0
    while read -r line || [ -n "$line" ]; do
        local -i length=$((${#line} / 2))
        local left=${line:0:$length}
        local right=${line:$length}
        local match=""
        for ((i = 0; i < "$length"; i++)); do
            for ((j = 0; j < "$length"; j++)); do
                if [[ "${left:$i:1}" == "${right:$j:1}" ]]; then
                    match="${left:$i:1}"
                    break 2
                fi
            done
        done
        ((ans += "$(priority "$match")"))
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    mapfile -t lines < "$1"
    for ((x = 0; x < "${#lines[@]}"; x += 3)); do
        local line1="${lines[x]}"
        local line2="${lines[x + 1]}"
        local line3="${lines[x + 2]}"
        local -i len1="${#line1}"
        local -i len2="${#line2}"
        local -i len3="${#line3}"
        local match=""
        for ((i = 0; i < "$len1"; i++)); do
            local m1="${line1:$i:1}"
            for ((j = 0; j < "$len2"; j++)); do
                if [[ "$m1" == "${line2:$j:1}" ]]; then
                    for ((k = 0; k < "$len3"; k++)); do
                        if [[ "$m1" == "${line3:$k:1}" ]]; then
                            match="$m1"
                            break 3
                        fi
                    done
                fi
            done
        done
        ((ans += "$(priority "$match")"))
    done
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    sample=(
        "vJrwpWtwJgWrhcsFMMfFFhFp"
        "jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL"
        "PmmdzqPrVvPwwTWBwg"
        "wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn"
        "ttgJtRGJQctTZtZT"
        "CrZsJsPPZsGzwwsLwLmpwMDw"
    )

    TEST part1 sample 157
    TEST part2 sample 70
}

source "$(dirname "$0")/aoc_main.sh"
