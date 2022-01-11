#!/bin/bash
#
# Advent of Code 2021 Day 2
#

year=2021
day=02

part1() {
    local ver=0
    local hor=0
    while read -r dir amount || [ -n "$dir" ]; do
        case "$dir" in
            up)
                ((ver -= amount))
                ;;
            down)
                ((ver += amount))
                ;;
            forward)
                ((hor += amount))
                ;;
            *)
                fatal 1 "Invalid input"
                ;;
            esac
    done < "$1"
    echo $((hor * ver))
    return 0
}

part2() {
    local ver=0
    local hor=0
    local aim=0
    while read -r dir amount || [ -n "$dir" ]; do
        case "$dir" in
            up)
                ((aim -= amount))
                ;;
            down)
                ((aim += amount))
                ;;
            forward)
                ((hor += amount))
                ((ver += aim * amount))
                ;;
            *)
                fatal 1 "Invalid input"
                ;;
        esac
    done < "$1"
    echo $((hor * ver))
    return 0
}

tests() {
    # shellcheck disable=SC2034
    sample=(
    "forward 5"
    "down 5"
    "forward 8"
    "up 3"
    "down 8"
    "forward 2"
    )

    TEST part1 sample 150
    TEST part2 sample 900
}

source "$(dirname "$0")/aoc_main.sh"
