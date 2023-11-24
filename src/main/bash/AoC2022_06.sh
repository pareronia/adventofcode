#!/bin/bash
#
# Advent of Code 2022 Day 6
#

year=2022
day=06

solve() {
    read -r line < "$1"
    local -i size="$2"
    local -i len="${#line}"
    for ((i = "$size"; i < "$len"; i++)); do
        local -A chars=()
        local test="${line:$i-$size:$size}"
        for ((j = 0; j < "$size"; j++)); do
            chars+=(["${test:$j:1}"]=1)
        done
        if [[ "${#chars[@]}" -eq "$size" ]]; then
            echo "$i"
            return 0
        fi
    done
    fatal 1 "Unsolvable"
}

part1() {
    solve "$1" 4
}

part2() {
    solve "$1" 14
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
        sample2=("bvwbjplbgvbhsrlpgdmjqwftvncz")
        sample3=("nppdvjthqldpwncqszvftbrmjlhg")
        sample4=("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
        sample5=("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")
    }

    TEST part1 sample1 7
    TEST part1 sample2 5
    TEST part1 sample3 6
    TEST part1 sample4 10
    TEST part1 sample5 11
    TEST part2 sample1 19
    TEST part2 sample2 23
    TEST part2 sample3 23
    TEST part2 sample4 29
    TEST part2 sample5 26
}

source "$(dirname "$0")/aoc_main.sh"
