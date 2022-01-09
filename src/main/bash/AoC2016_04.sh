#!/bin/bash
#
# Advent of Code 2016 Day 4
#

re1="([-a-z]+)-([0-9]+)\\[([a-z]{5})\\]$"

part1() {
    local -i ans=0
    while read -r room; do
        [[ "$room" =~ $re1 ]]
        local name="${BASH_REMATCH[1]}"
        local -i sector="${BASH_REMATCH[2]}"
        local chksum="${BASH_REMATCH[3]}"
        local -A hist=()
        for ((i = 0; i < ${#name}; i++)); do
            local ch="${name:$i:1}"
            [ "$ch" = "-" ] && continue
            hist["$ch"]=$((hist[$ch] + 1))
        done
        chksum_=$({
            for k in "${!hist[@]}"; do
                echo ${hist[$k]}' - '$k;
            done | sort --key=1rn,2 |
                for ((j = 0; j < 5; j++)); do
                    read -r hh
                    echo -n "${hh: -1}"
                done
                echo ""
        })
        [ "$chksum_" = "$chksum" ] && ((ans += sector))
    done < "$1"
    echo "$ans"
    return 0
}

part2() {
    local -i ans=0
    local re2="[a-z]{9}-[a-z]{6}-[a-z]{7}$"
    while read -r room; do
        [[ "$room" =~ $re1 ]]
        local name="${BASH_REMATCH[1]}"
        local -i sector="${BASH_REMATCH[2]}"
        [[ "$name" =~ $re2 ]]
        [ "${#BASH_REMATCH[@]}" = 0 ] && continue
        local -i shift=$((sector % 26))
        local decrypt=""
        for ((i = 0; i < ${#name}; i++)); do
            local ch="${name:$i:1}"
            if [ "$ch" = "-" ]; then
                decrypt+=" "
            else
                local -i ord
                ord=$(printf '%d' "'$ch")
                ord=$(((ord + shift - 97) % 26 + 97))
                # shellcheck disable=SC2059
                decrypt+=$(printf "\\$(printf '%03o' "$ord")")
            fi
        done
        [ "$decrypt" = "northpole object storage" ] \
            && { echo "$sector"; break; }
    done < "$1"
    return 0
}

# shellcheck disable=SC2034
sample=(
"aaaaa-bbb-z-y-x-123[abxyz]"
"a-b-c-d-e-f-g-h-987[abcde]"
"not-a-real-room-404[oarel]"
"totally-real-room-200[decoy]"
)

# shellcheck source=SCRIPTDIR/aoc.sh
. "$(dirname "$0")/aoc.sh"

TEST part1 sample 1514

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2016
day=04
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || fatal 1 "Input file not found: $inputfile"

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" || fatal 1 "$check"
