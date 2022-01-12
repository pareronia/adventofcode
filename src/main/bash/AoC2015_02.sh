#!/bin/bash
#
# Advent of Code 2015 Day 2
#

year=2015
day=02

part1() {
    local tot=0
    IFS='x'; while read -r l w h || [ -n "$l" ]; do
        local side1=$((2 * l * w))
        local min="$side1"
        local side2=$((2 * w * h))
        [ "$side2" -lt "$min" ] && min="$side2"
        local side3=$((2 * h * l))
        [ "$side3" -lt "$min" ] && min="$side3"
        ((tot += (side1 + side2 + side3 + min / 2)))
    done < "$1"
    echo "$tot"
    return 0
}

part2() {
    local tot=0
    IFS='x'; while read -r l w h || [ -n "$l" ]; do
        local min=$((2 * (l + w)))
        local length=$((2 * (w + h)))
        [ "$length" -lt "$min" ] && min="$length"
        local length=$((2 * (h + l)))
        [ "$length" -lt "$min" ] && min="$length"
        ((tot += (min + l * w * h)))
    done < "$1"
    echo "$tot"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("2x3x4")
        sample2=("1x1x10")
    }

    TEST part1 sample1 58
    TEST part1 sample2 43
    TEST part2 sample1 34
    TEST part2 sample2 14
}

source "$(dirname "$0")/aoc_main.sh"
