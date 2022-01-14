#!/bin/bash
#
# Advent of Code 2017 Day 2
#

year=2017
day=02

part1() {
    local -i ans=0
    while read -r row || [ -n "$row" ]; do
        local -i min=999999999999999999
        local -i max=-999999999999999999
        for num in $row; do
            [ "$num" -lt "$min" ] && min=$num
            [ "$num" -gt "$max" ] && max=$num
        done
        ((ans += (max - min)))
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    while read -r row || [ -n "$row" ]; do
        for num1 in $row; do
            for num2 in $row; do
                [ "$num1" = "$num2" ] && continue
                if [ "$num1" -gt "$num2" ]; then
                    [ $((num1 % num2)) = 0 ] && ((ans += num1 / num2))
                else
                    [ $((num2 % num1)) = 0 ] && ((ans += num2 / num1))
                fi
            done
        done
    done < "$1"
    echo $((ans / 2))
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=(
        "5 1 9 5"
        "7 5 3"
        "2 4 6 8"
        )
        sample2=(
        "5 9 2 8"
        "9 4 7 3"
        "3 8 6 5"
        )
    }

    TEST part1 sample1 18
    TEST part2 sample2 9
}

source "$(dirname "$0")/aoc_main.sh"
