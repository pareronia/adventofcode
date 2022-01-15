#!/bin/bash
#
# Advent of Code 2017 Day 4
#

year=2017
day=04

alfa="a b c d e f g h i j k l m n o p q r s t u v w x y z"

part1() {
    local -i ans=0
    while read -r pp || [ -n "$pp" ]; do
        local -A wc1=()
        for word in $pp; do
            wc1[$word]=$((wc1[$word] + 1))
        done
        for k in "${!wc1[@]}"; do
            [ "${wc1[$k]}" -gt 1 ] && continue 2
        done
        ((ans++))
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    while read -r pp || [ -n "$pp" ]; do
        local -i wc2=0
        local -A lcs=()
        for word in $pp; do
            ((wc2++))
            local -A lc=()
            for ((i = 0; i < "${#word}"; i++)); do
                local ch="${word:$i:1}"
                lc["$ch"]=$((lc[$ch] + 1))
            done
            local val=""
            for letter in $alfa; do
                val+="$letter${lc[$letter]}"
            done
            lcs["$val"]=$((lcs[$val] + 1))
        done
        [ "${#lcs[@]}" -eq "$wc2" ] && ((ans++))
    done < "$1"
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=("aa bb cc dd ee")
        sample2=("aa bb cc dd aa")
        sample3=("aa bb cc dd aaa")
        sample4=("abcde fghij")
        sample5=("abcde xyz ecdab")
        sample6=("a ab abc abd abf abj")
        sample7=("iiii oiii ooii oooi oooo")
        sample8=("oiii ioii iioi iiio")
    }

    TEST part1 sample1 1
    TEST part1 sample2 0
    TEST part1 sample3 1
    TEST part2 sample4 1
    TEST part2 sample5 0
    TEST part2 sample6 1
    TEST part2 sample7 1
    TEST part2 sample8 0
}

source "$(dirname "$0")/aoc_main.sh"
