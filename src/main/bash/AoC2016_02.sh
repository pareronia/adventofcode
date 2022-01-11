#!/bin/bash
#
# Advent of Code 2016 Day 2
#

year=2016
day=02

# shellcheck disable=SC2034
declare -A keypad1=(
["-1,1"]=1
["0,1"]=2
["1,1"]=3
["-1,0"]=4
["0,0"]=5
["1,0"]=6
["-1,-1"]=7
["0,-1"]=8
["1,-1"]=9
)
# shellcheck disable=SC2034
declare -A keypad2=(
["2,2"]=1
["1,1"]=2
["2,1"]=3
["3,1"]=4
["0,0"]=5
["1,0"]=6
["2,0"]=7
["3,0"]=8
["4,0"]=9
["1,-1"]=A
["2,-1"]=B
["3,-1"]=C
["2,-2"]=D
)

solve() {
    local -n keypad="$1"
    local -i x=0 y=0 new_x=0 new_y=0
    while read -r button || [ -n "$button" ]; do
        for ((i = 0; i < "${#button}"; i++)); do
            local dir="${button:i:1}"
            case "$dir" in
                U)
                    new_x=$x new_y=$((y + 1))
                    ;;
                D)
                    new_x=$x new_y=$((y - 1))
                    ;;
                L)
                    new_x=$((x - 1)) new_y=$y
                    ;;
                R)
                    new_x=$((x + 1)) new_y=$y
                    ;;
                *)
                    fatal 1 "Invalid input"
                    ;;
            esac
            local val="${keypad[$new_x,$new_y]}"
            [ -n "$val" ] && x=new_x y=new_y
        done
        echo -n "${keypad[$x,$y]}"
    done < "$2"
    echo ""
    return 0
}

part1() {
    solve "keypad1" "$@"
}

part2() {
    solve "keypad2" "$@"
}

tests() {
    # shellcheck disable=SC2034
    sample=(
    ULL
    RRDDD
    LURDL
    UUUUD
    )

    TEST part1 sample "1985"
    TEST part2 sample "5DB3"
}

source "$(dirname "$0")/aoc_main.sh"
