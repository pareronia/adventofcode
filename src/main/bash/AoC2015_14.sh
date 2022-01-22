#!/bin/bash
#
# Advent of Code 2015 Day 14
#

year=2015
day=14

declare re="([A-Za-z]+) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds\\."

solve1() {
    local -i time="$1"
    local -i max=-999999999999999999
    while read -r line || [ -n "$line" ]; do
        [[ "$line" =~ $re ]] || fatal 1 "panic"
        local -i speed="${BASH_REMATCH[2]}"
        local -i go="${BASH_REMATCH[3]}"
        local -i stop="${BASH_REMATCH[4]}"
        local -i period_distance=$((speed * go))
        local -i period_time=$((go + stop))
        local -i periods=$((time / period_time))
        local -i left=$((time % period_time))
        local -i distance=0
        if ((left >= go)); then
            distance=$((periods * period_distance + period_distance))
        else
            distance=$((periods * period_distance + speed * left))
        fi
        ((distance > max)) && ((max = distance))
    done < "$2"
    echo "$max"
    return 0
}

part1() {
    solve1 2503 "$@"
}

solve2() {
    local -i time="$1"
    mapfile -t lines < "$2"
    local -A points=()
    for ((i = 1; i <= time; i++)); do
        local -i max=-999999999999999999
        local -A distances=()
        for line in "${lines[@]}"; do
            [[ "$line" =~ $re ]] || fatal 1 "panic"
            local name="${BASH_REMATCH[1]}"
            local -i speed="${BASH_REMATCH[2]}"
            local -i go="${BASH_REMATCH[3]}"
            local -i stop="${BASH_REMATCH[4]}"
            local -i period_distance=$((speed * go))
            local -i period_time=$((go + stop))
            local -i periods=$((i / period_time))
            local -i left=$((i % period_time))
            local -i distance=0
            if ((left >= go)); then
                distance=$((periods * period_distance + period_distance))
            else
                distance=$((periods * period_distance + speed * left))
            fi
            distances[$name]=$distance
            ((distance > max)) && ((max = distance))
        done
        for k in "${!distances[@]}"; do
            ((distances[$k] == max)) && ((points[$k]++))
        done
    done
    local -i max=-999999999999999999
    for k in "${!points[@]}"; do
        ((points[$k] > max)) && ((max = points[$k]))
    done
    echo "$max"
    return 0
}

part2() {
    solve2 2503 "$@"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample=(
        "Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds."
        "Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds."
        )
    }

    TEST "solve1 1000" sample 1120
    TEST "solve2 1000" sample 689
}

source "$(dirname "$0")/aoc_main.sh"
