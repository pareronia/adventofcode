#!/bin/bash
#
# Advent of Code 2021 Day 1
#

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

mapfile -t depths < "$_aocd__inputfile"
size="${#depths[@]}"

countIncreases() {
    window="$1"
    count=0
    for ((i = "$window"; i < size; i++))
    do
        if (("${depths[i]}" > "${depths[i-window]}")); then
            count=$((count + 1))
        fi
    done
    echo "$count"
}

echo "Part 1: $(countIncreases 1)"
echo "Part 2: $(countIncreases 3)"
