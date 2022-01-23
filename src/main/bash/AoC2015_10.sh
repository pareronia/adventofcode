#!/bin/bash
#
# Advent of Code 2015 Day 10
#

year=2015
day=10

look_and_say() {
    local string="$1"
    local -i i=0
    local -i size="${#string}"
    while ((i < size)); do
        local digit="${string:$i:1}"
        local -i j=0
        local -i idx=i
        while ((idx < size)); do
            [ "${string:$idx:1}" != "$digit" ] && break
            ((j++))
            idx=$((i + j))
        done
        echo -n "$j$digit"
        ((i += j))
    done
    return 0
}

solve() {
    local -i n="$1"
    read -r string < "$2"
    for ((i = 0; i < n; i++)); do
        _debug "$i: ${#string}"
        string="$(look_and_say "$string")"
    done
    echo "$string"
    return 0
}

part1() {
    local ans
    ans="$(solve 40 "$@")"
    echo "${#ans}"
    return 0
}

part2() {
    local ans
    ans="$(solve 50 "$@")"
    echo "${#ans}"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "1"
        )
    }

    TEST "solve 5" sample "312211"
}

source "$(dirname "$0")/aoc_main.sh"
