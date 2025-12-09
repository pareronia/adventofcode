#!/bin/bash
#
# Advent of Code 2025 Day 8
#

year=2025
day=08

declare -a boxes
declare -a pairs
declare -a ids
declare -a sz
num_components=0

parse_input() {
    _debug "parsing input"
    while read -r line || [ -n "$line" ]; do
        boxes+=("$line")
    done < "$1"

    for ((i = 0; i < ${#boxes[@]}; i++)); do
        _debug "$i"
        IFS=','
        read -r x1 y1 z1 <<< "${boxes[i]}"
        for ((j = i + 1; j < ${#boxes[@]}; j++)); do
            read -r x2 y2 z2 <<< "${boxes[j]}"
            local -i dx=$((x1 - x2)) dy=$((y1 - y2)) dz=$((z1 - z2))
            local -i d=$((dx * dx + dy * dy + dz * dz))
            pairs+=("$i:$j:$d")
        done
    done
    # shellcheck disable=SC2207
    IFS=$'\n' pairs=($(sort --numeric-sort --field-separator=: --key=3n,3 <<< "${pairs[*]}"))
    return 0
}

dsu_init() {
    _debug "dsu_init"
    local -i size="$1"
    for ((i = 0; i < size; i++)); do
        ids[i]=$i
        sz[i]=1
    done
    num_components="$size"
    return 0
}

dsu_find() {
    local -i p="$1"
    if [ "${ids[$p]}" != "$p" ]; then
        ids[p]="$(dsu_find "${ids[$p]}")"
    fi
    echo "${ids[$p]}"
    return 0
}

dsu_unify() {
    local -i p="$1"
    local -i q="$2"
    local -i root_p
    local -i root_q
    root_p="$(dsu_find "$p")"
    root_q="$(dsu_find "$q")"
    if ((root_p != root_q)); then
        if ((sz[root_p] < sz[root_q])); then
            ((sz[root_q] += sz[root_p]))
            ((ids[root_p] = ids[root_q]))
            ((sz[root_p] = 0))
        else
            ((sz[root_p] += sz[root_q]))
            ((ids[root_q] = ids[root_p]))
            ((sz[root_q] = 0))
        fi
        ((num_components -= 1))
    fi
    return 0
}

solve1() {
    local -i num_pairs="$1"
    parse_input "$2"
    # _vardump boxes
    # _vardump pairs
    dsu_init "${#boxes[@]}"
    for ((i = 0; i < num_pairs; i++)); do
        IFS=':' read -r p1 p2 _ <<< "${pairs[i]}"
        dsu_unify "$p1" "$p2"
    done
    # _debug "$num_components"
    # _vardump ids
    _vardump sz
    local -i ans=1
    # sort --numeric-sort --reverse <<< "${sz[*]}" |
    while read -r s; do
        _debug "=$s="
        ((ans *= s))
    done < <(printf '%s\n' "${sz[@]}" | sort --numeric-sort | tail -3)
    echo "$ans"
    return 0
}

part1() {
    solve1 1000 "$1"
}

part2() {
    echo 0
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
            "162,817,812"
            "57,618,57"
            "906,360,560"
            "592,479,940"
            "352,342,300"
            "466,668,158"
            "542,29,236"
            "431,825,988"
            "739,650,466"
            "52,470,668"
            "216,146,977"
            "819,987,18"
            "117,168,530"
            "805,96,715"
            "346,949,466"
            "970,615,88"
            "941,993,340"
            "862,61,35"
            "984,92,344"
            "425,690,689"
        )
    }

    TEST "solve1 10" sample 40
    TEST part2 sample 0
}

source "$(dirname "$0")/aoc_main.sh"
