#!/bin/bash
#
# Advent of Code 2022 Day 4
#

year=2022
day=07

declare -A sizes=()

getSizes() {
    local -a path=()
    while read -r line || [ -n "$line" ]; do
        if [[ "$line" =~ ^"$ cd ".* ]]; then
            name="${line:5}"
            [ "$name" == "/" ] && continue
            if [ "$name" == ".." ]; then
                path_len=${#path[@]}
                [ "$path_len" -gt 0 ] && unset "path[$((path_len-1))]"
            else
                path+=("$name")
            fi
        else
            [[ "$line" =~ ^"$".* ]] && continue
            # shellcheck disable=2034
            IFS=' ' read -r tok1 tok2 <<< "$line"
            unset IFS
            [ "$tok1" == "dir" ] && continue
            local -i size="$tok1"
            for (( i = 0; i <= "${#path[@]}"; i++ )); do
                local pp=""
                for (( j = 0; j < i; j++ )); do
                    pp="$pp/${path[$j]}"
                done
                ((sizes["/$pp"]+="$size"))
            done
        fi
    done < "$1"
}

part1() {
    getSizes "$@"
    local -i ans=0
    for size in "${sizes[@]}"; do
        [ "$size" -gt 100000 ] && continue
        ((ans += size))
    done
    echo "$ans"
    return 0
}

part2() {
    getSizes "$@"
    local -i total="${sizes[/]}"
    local -i wanted=$(( 30000000 - (70000000 - total) ))
    # shellcheck disable=2207
    IFS=$'\n' sorted=($(sort --numeric-sort <<< "${sizes[*]}"))
    unset IFS
    for size in "${sorted[@]}"; do
        [ "$size" -lt "$wanted" ] && continue
        echo "$size"
        return 0
    done
    _fatal 1 "Unsolvable"
}

tests() {
    # shellcheck disable=SC2034
    sample=(
        "$ cd /"
        "$ ls"
        "dir a"
        "14848514 b.txt"
        "8504156 c.dat"
        "dir d"
        "$ cd a"
        "$ ls"
        "dir e"
        "29116 f"
        "2557 g"
        "62596 h.lst"
        "$ cd e"
        "$ ls"
        "584 i"
        "$ cd .."
        "$ cd .."
        "$ cd d"
        "$ ls"
        "4060174 j"
        "8033020 d.log"
        "5626152 d.ext"
        "7214296 k"
    )

    TEST part1 sample 95437
    TEST part2 sample 24933642
}

source "$(dirname "$0")/aoc_main.sh"
