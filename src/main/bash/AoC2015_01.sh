#!/bin/bash
#
# Advent of Code 2015 Day 1
#

year=2015
day=01

part1() {
    local count=0
    local i=0
    while read -r -n1 char
    do
        if [ "$char" = ")" ]; then
            ((count++))
            ((i++))
        elif [ "$char" = "(" ]; then
            ((i++))
        fi
    done < "$1"
    echo $((i -  2 * count))
    return 0
}

part2() {
    local sum=0
    local i=0
    while read -r -n1 char
    do
        if [ "$char" = "(" ]; then
            ((i++))
            ((sum++))
        elif [ "$char" = ")" ]; then
            ((i++))
            ((sum--))
        fi
        if [ "$sum" = -1 ]; then
            echo "$i"
            return 0
        fi
    done < "$1"
    fatal 1 "Unsolvable"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("(())")
        sample2=("()()")
        sample3=("(((")
        sample4=("(()(()(")
        sample5=("))(((((")
        sample6=("())")
        sample7=("))(")
        sample8=(")))")
        sample9=(")())())")
        sample10=(")")
        sample11=("()())")
    }

    TEST part1 sample1 0
    TEST part1 sample2 0
    TEST part1 sample3 3
    TEST part1 sample4 3
    TEST part1 sample5 3
    TEST part1 sample6 -1
    TEST part1 sample7 -1
    TEST part1 sample8 -3
    TEST part1 sample9 -3
    TEST part2 sample10 1
    TEST part2 sample11 5
}

source "$(dirname "$0")/aoc_main.sh"
