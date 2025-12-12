#!/bin/bash
#
# Advent of Code 2025 Day 11
#

year=2025
day=11

declare -A edges
declare -A cache

create_graph() {
    while read -r line || [ -n "$line" ]; do
        local node="${line%%:*}"
        local neighbours="${line:${#node}+2}"
        edges[$node]="$neighbours"
    done < "$1"
    return 0
}

dfs() {
    local node="$1"
    [[ -n "${cache[$node]}" ]] && {
        return 0
    }
    [ "$node" = "$end" ] && {
        ((cache["$node"] += 1))
        return 0
    }
    IFS=' ' read -ra neighbours <<< "${edges[$node]}"
    local n
    for n in "${neighbours[@]}"; do
        dfs "$n"
        ((cache["$node"] += cache["$n"]))
    done
    return 0
}

count_all_paths() {
    cache=()
    local start="$1"
    local end="$2"
    dfs "$start"
    echo "${cache[$start]}"
    return 0
}

part1() {
    create_graph "$1"
    count_all_paths "you" "out"
    return 0
}

part2() {
    create_graph "$1"
    local -i dac2fft
    dac2fft=$(count_all_paths "dac" "fft")
    if ((dac2fft == 0)); then
        echo $(($(count_all_paths "svr" "fft") * \
        $(count_all_paths "fft" "dac") * \
        $(count_all_paths "dac" "out")))
    else
        echo $(($(count_all_paths "svr" "dac") * \
        dac2fft * \
        $(count_all_paths "fft" "out")))
    fi
    return 0
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=(
            "aaa: you hhh"
            "you: bbb ccc"
            "bbb: ddd eee"
            "ccc: ddd eee fff"
            "ddd: ggg"
            "eee: out"
            "fff: out"
            "ggg: out"
            "hhh: ccc fff iii"
            "iii: out"
        )
        sample2=(
            "svr: aaa bbb"
            "aaa: fft"
            "fft: ccc"
            "bbb: tty"
            "tty: ccc"
            "ccc: ddd eee"
            "ddd: hub"
            "hub: fff"
            "eee: dac"
            "dac: fff"
            "fff: ggg hhh"
            "ggg: out"
            "hhh: out"
        )
    }

    TEST part1 sample1 5
    TEST part2 sample2 2
}

source "$(dirname "$0")/aoc_main.sh"
