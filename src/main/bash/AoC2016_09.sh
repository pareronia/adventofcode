#!/bin/bash
#
# Advent of Code 2016 Day 9
#

year=2016
day=09

solve() {
    local -i part="$1"
    local in="$2"
    local -i ans=0
    local -i in_marker=0
    local marker=""
    local -i i=0
    while ((i < "${#in}")); do 
        local ch="${in:$i:1}"
        if [ "$ch" = "(" ]; then
            in_marker=1
        elif [ "$ch" = ")" ]; then
            re="^([0-9]+)x([0-9]+)$"
            [[ "$marker" =~ $re ]]
            [ "${#BASH_REMATCH[@]}" -eq 0 ] && fatal 1 "panic"
            local -i skip="${BASH_REMATCH[1]}"
            local -i repeat="${BASH_REMATCH[2]}"
            if [ "$part" -eq 2 ]; then
                local skipped="${in:$i+1:$skip}"
                ((ans += repeat * $(solve 2 "$skipped")))
            else
                ((ans += repeat * skip))
            fi
            ((i += skip))
            marker=""
            in_marker=0
        else
            if [ "$in_marker" -eq 1 ]; then
                marker+="$ch"
            else
                ((ans++))
            fi
        fi
        ((i++))
    done
    echo "$ans"
    return 0
}

part1() {
    read -r in < "$1"
    solve 1 "$in"
}

part2() {
    read -r in < "$1"
    solve 2 "$in"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("ADVENT")
        sample2=("A(1x5)BC")
        sample3=("(3x3)XYZ")
        sample4=("A(2x2)BCD(2x2)EFG")
        sample5=("(6x1)(1x3)A")
        sample6=("X(8x2)(3x3)ABCY")
        sample7=("(27x12)(20x12)(13x14)(7x10)(1x12)A")
        sample8=("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN")
    }

    TEST part1 sample1 6
    TEST part1 sample2 7
    TEST part1 sample3 9
    TEST part1 sample4 11
    TEST part1 sample5 6
    TEST part1 sample6 18
    TEST part2 sample3 9
    TEST part2 sample6 20
    TEST part2 sample7 241920
    TEST part2 sample8 445
}

source "$(dirname "$0")/aoc_main.sh"
