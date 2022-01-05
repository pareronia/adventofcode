#!/bin/bash
#
# Advent of Code 2015 Day 1
#

part1() {
    [ "$#" = 1 ] || exit 1
    local size="${#1}"
    local count=0
    while read -r -n1 char
    do
        if [ "$char" = ")" ]; then
            ((count++))
        fi
    done < <(echo "$1")
    echo $((size -  2 * count))
}

part2() {
    [ "$#" = 1 ] || exit 1
    local sum=0
    local i=0
    while read -r -n1 char
    do
        if [ "$char" = "(" ]; then
            ((sum++))
        elif [ "$char" = ")" ]; then
            ((sum--))
        else
            exit 1
        fi
        if [ "$sum" = -1 ]; then
            echo $((i + 1))
            return 0
        fi
        ((i++))
    done < <(echo "$1")
}

sample1=("(())")
sample2=("()()")
sample3=("(((")
sample4=("(()(()(")
sample5=("))(((((")
sample6=("())")
sample7=("))(")
sample8=(")))")
sample9=(")())())")
sample10=(")")
sample11=("()())")

sample="$(part1 "${sample1[@]}")"
[ "$sample" = 0 ] || { echo "Part 1 sample1 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample2[@]}")"
[ "$sample" = 0 ] || { echo "Part 1 sample2 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample3[@]}")"
[ "$sample" = 3 ] || { echo "Part 1 sample3 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample4[@]}")"
[ "$sample" = 3 ] || { echo "Part 1 sample4 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample5[@]}")"
[ "$sample" = 3 ] || { echo "Part 1 sample5 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample6[@]}")"
[ "$sample" = -1 ] || { echo "Part 1 sample6 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample7[@]}")"
[ "$sample" = -1 ] || { echo "Part 1 sample7 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample8[@]}")"
[ "$sample" = -3 ] || { echo "Part 1 sample8 failed: $sample" >&2; exit 1; }
sample="$(part1 "${sample9[@]}")"
[ "$sample" = -3 ] || { echo "Part 1 sample9 failed: $sample" >&2; exit 1; }
sample="$(part2 "${sample10[@]}")"
[ "$sample" = 1 ] || { echo "Part 2 sample10 failed: $sample" >&2; exit 1; }
sample="$(part2 "${sample11[@]}")"
[ "$sample" = 5 ] || { echo "Part 2 sample11 failed: $sample" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2015
day=01
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }
mapfile -t input < "$inputfile"

part1="$(part1 "${input[@]}")"
echo "Part 1: $part1"
part2="$(part2 "${input[@]}")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
