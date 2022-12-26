#!/bin/bash
#
# Advent of Code 2022 Day 25
#

year=2022
day=25

declare -A decode=([0]=0 [1]=1 [2]=2 [-]=-1 [=]=-2)
declare -A encode=([0]="0,0" [1]="1,0" [2]="2,0" [3]="=,1" [4]="-,1" [5]="0,1")

part1() {
    local -i total
    while read -r line || [ -n "$line" ]; do
        local -i subtotal=0
        for ((i = 0; i < "${#line}"; i++)); do
            coefficient="${decode[${line:i:1}]}"
            exponent=$((${#line} - 1 - i))
            ((subtotal += coefficient * (5 ** exponent)))
        done
        ((total += subtotal))
    done < "$1"
    local ans=""
    for (( ; ; )); do
        pair="${encode[$((total % 5))]}"
        digit="${pair%,*}"
        carry="${pair#*,}"
        ans="$digit$ans"
        total=$((total / 5 + carry))
        if [[ "$total" = 0 ]]; then
            echo "$ans"
            return 0
        fi
    done
    _fatal 1 "Unsolvable"
}

part2() {
    echo "0"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "1=-0-2"
            "12111"
            "2=0="
            "21"
            "2=01"
            "111"
            "20012"
            "112"
            "1=-1="
            "1-12"
            "12"
            "1="
            "122"
        )
    }

    TEST part1 sample "2=-1=0"
}

source "$(dirname "$0")/aoc_main.sh"
