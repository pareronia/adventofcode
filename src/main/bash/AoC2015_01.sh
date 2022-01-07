#!/bin/bash
#
# Advent of Code 2015 Day 1
#

part1() {
    local count=0
    local i=0
    while read -r -n1 char
    do
        if [ "$char" = ")" ]; then
            ((count++))
            ((i++))
        elif [ "$char" = "(" ]; then
            ((i++))
        fi
    done < "$1"
    echo $((i -  2 * count))
}

part2() {
    local sum=0
    local i=0
    while read -r -n1 char
    do
        if [ "$char" = "(" ]; then
            ((i++))
            ((sum++))
        elif [ "$char" = ")" ]; then
            ((i++))
            ((sum--))
        fi
        if [ "$sum" = -1 ]; then
            echo "$i"
            return 0
        fi
    done < "$1"
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

sample="$(part1 <(for line in "${sample1[@]}"; do echo "$line"; done))"
[ "$sample" = 0 ] || { echo "Part 1 sample1 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample2[@]}"; do echo "$line"; done))"
[ "$sample" = 0 ] || { echo "Part 1 sample2 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample3[@]}"; do echo "$line"; done))"
[ "$sample" = 3 ] || { echo "Part 1 sample3 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample4[@]}"; do echo "$line"; done))"
[ "$sample" = 3 ] || { echo "Part 1 sample4 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample5[@]}"; do echo "$line"; done))"
[ "$sample" = 3 ] || { echo "Part 1 sample5 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample6[@]}"; do echo "$line"; done))"
[ "$sample" = -1 ] || { echo "Part 1 sample6 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample7[@]}"; do echo "$line"; done))"
[ "$sample" = -1 ] || { echo "Part 1 sample7 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample8[@]}"; do echo "$line"; done))"
[ "$sample" = -3 ] || { echo "Part 1 sample8 failed: $sample" >&2; exit 1; }
sample="$(part1 <(for line in "${sample9[@]}"; do echo "$line"; done))"
[ "$sample" = -3 ] || { echo "Part 1 sample9 failed: $sample" >&2; exit 1; }
sample="$(part2 <(for line in "${sample10[@]}"; do echo "$line"; done))"
[ "$sample" = 1 ] || { echo "Part 2 sample10 failed: $sample" >&2; exit 1; }
sample="$(part2 <(for line in "${sample11[@]}"; do echo "$line"; done))"
[ "$sample" = 5 ] || { echo "Part 2 sample11 failed: $sample" >&2; exit 1; }

# shellcheck source=SCRIPTDIR/aocd/aocd.sh
. "$(dirname "$0")/aocd/aocd.sh"

year=2015
day=01
inputfile="$(_aocd__inputFile "$year" "$day")" \
    || { echo "Input file not found: $inputfile" >&2; exit 1; }

part1="$(part1 "$inputfile")"
echo "Part 1: $part1"
part2="$(part2 "$inputfile")"
echo "Part 2: $part2"

check="$(_aocd__check "$year" "$day" "$part1" "$part2")" \
    || { echo "$check" >&2; exit 1; }
