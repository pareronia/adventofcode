#!/bin/bash
#
# Advent of Code 2015 Day 4
#

year=2015
day=04

solve() {
    read -r input < "$1"
    local val="$input"
    local i=0
    local target="$2"
    local size="${#target}"
    while [ "${val:0:size}" != "$target" ]; do
        ((i++))
        local str2hash="$input""$i"
        val="$(echo -n "$str2hash" | md5sum | cut -d' ' -f1)"
        # if [ $((i % 100)) = 0 ]; then
        #     printf "\r%d: %s %s" "$i" "$str2hash" "$val" >&2
        # fi
    done
    echo "$i"
    return 0
}

part1() {
    solve "$1" "00000"
}

part2() {
    solve "$1" "000000"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("abcdef")
        sample2=("pqrstuv")
    }

    TEST part1 sample1 609043
    TEST part1 sample2 1048970
}

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
source "$(dirname "$0")/aoc_main.sh"
