#!/bin/bash
#
# Advent of Code 2017 Day 1
#

year=2017
day=01

solve() {
    local -i distance="$1"
    local line="$2"
    local -i ans=0
    local lineline="$line${line:0:$distance}"
    for ((i = 0; i < ${#line}; i++)); do
        local ch="${lineline:$i:1}" 
        local -i ii=$((i + distance))
        [ "$ch" = "${lineline:$ii:1}" ] || continue
        local -i val="$ch"
        ((ans += val))
    done
    echo "$ans"
    return 0
}

part1() {
    read -r line < "$1"
    solve 1 "$line"
}

part2() {
    read -r line < "$1"
    solve $((${#line} / 2)) "$line"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("1122")
        sample2=("1111")
        sample3=("1234")
        sample4=("91212129")
        sample5=("1212")
        sample6=("1221")
        sample7=("123425")
        sample8=("123123")
        sample9=("12131415")
    }

    TEST part1 sample1 3
    TEST part1 sample2 4
    TEST part1 sample3 0
    TEST part1 sample4 9
    TEST part2 sample5 6
    TEST part2 sample6 0
    TEST part2 sample7 4
    TEST part2 sample8 12
    TEST part2 sample9 4
}

source "$(dirname "$0")/aoc_main.sh"
