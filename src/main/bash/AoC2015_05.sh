#!/bin/bash
#
# Advent of Code 2015 Day 5
#

year=2015
day=05

part1() {
    local -i cnt=0
    while read -r input; do
        local re="([a-z])\1"
        [[ "$input" =~ $re ]]
        [ "${#BASH_REMATCH[@]}" -ge 1 ] || continue
        local re="(ab|cd|pq|xy)"
        [[ "$input" =~ $re ]]
        [ "${#BASH_REMATCH[@]}" = 0 ] || continue
        local -i vowels=0
        local -i i=0
        while [ "$i" -lt "${#input}" ]; do 
            case "${input:$i:1}" in
                a|e|i|o|u)
                    ((vowels++))
                    ;;
            esac
            ((i++))
        done
        [ "$vowels" -ge 3 ] || continue
        ((cnt++))
    done < "$1"
    echo "$cnt"
}

part2() {
    local -i cnt=0
    while read -r input; do
        local re="([a-z]{2})[a-z]*\1"
        [[ "$input" =~ $re ]]
        [ "${#BASH_REMATCH[@]}" -ge 1 ] || continue
        local re="([a-z])[a-z]\1"
        [[ "$input" =~ $re ]]
        [ "${#BASH_REMATCH[@]}" -ge 1 ] || continue
        ((cnt++))
    done < "$1"
    echo "$cnt"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("ugknbfddgicrmopn")
        sample2=("aaa")
        sample3=("jchzalrnumimnmhp")
        sample4=("haegwjzuvuyypxyu")
        sample5=("dvszwmarrgswjxmb")
        sample6=("qjhvhtzxzqqjkmpb")
        sample7=("xxyxx")
        sample8=("uurcxstgmygtbstg")
        sample9=("ieodomkazucvgmuy")
        sample10=("xyxy")
    }

    TEST part1 sample1 1
    TEST part1 sample2 1
    TEST part1 sample3 0
    TEST part1 sample4 0
    TEST part1 sample5 0
    TEST part2 sample6 1
    TEST part2 sample7 1
    TEST part2 sample8 0
    TEST part2 sample9 0
    TEST part2 sample10 1
}

source "$(dirname "$0")/aoc_main.sh"
