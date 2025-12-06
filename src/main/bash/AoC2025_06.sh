#!/bin/bash
#
# Advent of Code 2025 Day 6
#

year=2025
day=06

BY_ROWS=1
# shellcheck disable=SC2034
BY_COLUMNS=2

solve() {
    local -i mode="$1"
    declare -a lines
    declare -a ops
    declare -a terms

    while IFS='' read -r line || [ -n "$line" ]; do
        if [[ "$line" =~ ^[\\+\\*] ]]; then
            local -i width="${#line}"
            for ((i = 0; i < width; i++)); do
                local ch="${line:i:1}"
                [ "$ch" = ' ' ] && continue
                ops+=("$ch")
            done
        else
            lines+=("$line ")
        fi
    done < "$2"

    for ((i = 0; i < ${#ops[@]}; i++)); do
        if [ "${ops[i]}" = '*' ]; then
            terms[i]=1
        else
            terms[i]=0
        fi
    done

    if ((mode == BY_ROWS)); then
        for line in "${lines[@]}"; do
            local -i i=0
            while read -r n; do
                if [ "${ops[i]}" = '*' ]; then
                    terms[i]=$((terms[i] * n))
                else
                    terms[i]=$((terms[i] + n))
                fi
                ((i++))
            done <<< "$(echo "$line" | grep -Eo "[0-9]+")"
        done
    else
        local n=''
        local -i i=0
        for ((c = 0; c < ${#lines[0]}; c++)); do
            for ((r = 0; r < ${#lines[@]}; r++)); do
                n="$n${lines[r]:c:1}"
            done
            n=${n// /}
            if [ -z "$n" ]; then
                ((i++))
            else
                if [ "${ops[i]}" = '*' ]; then
                    terms[i]=$((terms[i] * n))
                else
                    terms[i]=$((terms[i] + n))
                fi
            fi
            n=''
        done
    fi

    local -i ans=0
    for term in "${terms[@]}"; do
        ((ans += term))
    done
    echo "$ans"
    return 0
}

part1() {
    solve BY_ROWS "$1"
    return 0
}

part2() {
    solve BY_COLUMNS "$1"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "123 328  51 64 "
            " 45 64  387 23 "
            "  6 98  215 314"
            "*   +   *   +  ")
    }

    TEST part1 sample 4277556
    TEST part2 sample 3263827
}

source "$(dirname "$0")/aoc_main.sh"
