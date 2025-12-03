#!/bin/bash
#
# Advent of Code ${year} Day ${day}
#

year=${year}
day=${day2}

part1() {
    echo 0
    return 0
}

part2() {
    echo 0
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=("TODO")
    }

    TEST part1 sample 0
    TEST part2 sample 0
}

source "$$(dirname "$$0")/aoc_main.sh"
