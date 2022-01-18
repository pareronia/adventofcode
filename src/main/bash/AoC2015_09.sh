#!/bin/bash
#
# Advent of Code 2015 Day 9
#

year=2015
day=09

permutations() {
    local -i n="$1"; shift 
    a=("$@")
    [ "$n" -eq 1 ] && { echo "${a[*]}"; return 0; }
    local -i n_1=$((n - 1))
    local -i i=0
    for ((i = 0; i < n_1; i++)); do
        permutations $n_1 "${a[@]}"
        if [ $((n % 2)) = 0 ]; then
            local temp="${a[$n_1]}"
            a[$n_1]="${a[$i]}"
            a[$i]="$temp"
        else
            local temp="${a[$n_1]}"
            a[$n_1]="${a[0]}"
            a[0]="$temp"
        fi
    done
    permutations "$n_1" "${a[@]}"
    return 0
}

solve() {
    local -i part="$1"; shift
    local -A places=()
    local -A distances=()
    while read -r distance || [ -n "$distance" ]; do
        [[ "$distance" =~ (.+)' to '(.+)' = '([0-9]+) ]]
        [ "${#BASH_REMATCH[@]}" -eq 0 ] && fatal 1 panic
        local pl1="${BASH_REMATCH[1]}"
        local pl2="${BASH_REMATCH[2]}"
        ((places[$pl1]++))
        ((places[$pl2]++))
        distances["$pl1,$pl2"]="${BASH_REMATCH[3]}"
        distances["$pl2,$pl1"]="${BASH_REMATCH[3]}"
    done < "$1"
    local -i min=999999999999999999
    local -i max=-999999999999999999
    # local -i cnt=1
    local -i size="${#places[@]}"
    mapfile -t ps < <(permutations "$size" "${!places[@]}")
    for p in "${ps[@]}"; do
        # printf "\r%d: %s" "$cnt" "$p" >&2
        local -a pp=()
        for x in $p; do
            pp+=("$x")
        done
        local -i sum=0
        for ((i = 0; i < size - 1; i++)); do
            ((sum += distances["${pp[i]},${pp[i+1]}"]))
        done
        [ "$sum" -lt "$min" ] && min="$sum"
        [ "$sum" -gt "$max" ] && max="$sum"
        # ((cnt++))
    done
    if [ "$part" -eq 1 ]; then
        echo "$min"
    else
        echo "$max"
    fi
    return 0
}

part1() {
    solve 1 "$@"
}

part2() {
    solve 2 "$@"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "London to Dublin = 464"
        "London to Belfast = 518"
        "Dublin to Belfast = 141"
        )
    }

    TEST part1 sample 605
    TEST part2 sample 982
}

source "$(dirname "$0")/aoc_main.sh"
