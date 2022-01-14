#!/bin/bash
#
# Advent of Code 2016 Day 7
#

year=2016
day=07

HYPERNET="\\[([a-z]*)\\]"
ABBA1="([a-z])([a-z])\\2\\1"
ABBA2="([a-z])\\1\\1\\1"
ABA1="([a-z])[a-z]\\1"
ABA2="([a-z])\\1\\1"

part1() {
    local -i ans=0
    while read -r ip || [ -n "$ip" ]; do
        while true; do
            [[ "$ip" =~ $HYPERNET ]]
            [ "${#BASH_REMATCH[@]}" -eq 0 ] && break
            local replace="${BASH_REMATCH[0]}"
            [[ "$replace" =~ $ABBA1 ]]
            if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
                [[ "${BASH_REMATCH[0]}" =~ $ABBA2 ]]
                [ "${#BASH_REMATCH[@]}" -eq 0 ] && continue 2
            fi
            ip="${ip//"$replace"/_}"
        done
        [[ "$ip" =~ $ABBA1 ]]
        if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
            [[ "${BASH_REMATCH[0]}" =~ $ABBA2 ]]
            [ "${#BASH_REMATCH[@]}" -eq 0 ] && ((ans++))
        fi
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    while read -r ip || [ -n "$ip" ]; do
        local -a hyp=()
        while true; do
            [[ "$ip" =~ $HYPERNET ]]
            [ "${#BASH_REMATCH[@]}" -eq 0 ] && break
            hyp+=("${BASH_REMATCH[1]}")
            ip="${ip//"${BASH_REMATCH[0]}"/}"
        done
        local -A abas=()
        for ((i = 0; i < ${#ip} - 3; i++)); do
            [[ "${ip:$i}" =~ $ABA1 ]]
            if [ "${#BASH_REMATCH[@]}" -gt 0 ]; then
                local aba="${BASH_REMATCH[0]}"
                [[ "$aba" =~ $ABA2 ]]
                [ "${#BASH_REMATCH[@]}" -eq 0 ] && ((abas["$aba"] += 1))
            fi
        done
        for aba in "${!abas[@]}"; do
            local bab="${aba:1:1}${aba:0:1}${aba:1:1}"
            [[ "${hyp[*]}" == *"$bab"* ]] && { ((ans++)); break; }
        done
    done < "$1"
    echo "$ans"
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=(
        "abba[mnop]qrst"
        "abcd[bddb]xyyx"
        "aaaa[qwer]tyui"
        "ioxxoj[asdfgh]zxcvbn"
        "abcox[ooooo]xocba"
        )
        sample2=(
        "aba[bab]xyz"
        "xyx[xyx]xyx"
        "aaa[kek]eke"
        "zazbz[bzb]cdb"
        )
    }

    TEST part1 sample1 2
    TEST part2 sample2 3
}

source "$(dirname "$0")/aoc_main.sh"
