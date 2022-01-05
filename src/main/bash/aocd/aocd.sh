#!/bin/bash
#

if [ "$AOCD_DIR" ]; then
    _aocd__memodir="$AOCD_DIR"
else
    _aocd__memodir="$HOME/.config/aocd"
fi

if [ "$AOC_SESSION" ]; then
    _aocd__token="$AOC_SESSION"
else
    IFS='' read -r _aocd__token < "$_aocd__memodir/token"
fi

_aocd__userid=$(eval "jq -r '.[\"$_aocd__token\"]' $_aocd__memodir/token2id.json")

_aocd__inputFile() {
    year="$1"
    day="$2"
    [ ${#year} = 4 ] || { echo "Provide year as yyyy"; return 1; }
    [ ${#day} = 2 ] || { echo "Provide day as dd"; return 1; }
    _aocd__inputfile="$_aocd__memodir"/"$_aocd__userid"/"$year"_"$day"_input.txt
    [ -f "$_aocd__inputfile" ] || { echo "$_aocd__inputfile not found"; return 1; }
    echo "$_aocd__inputfile"
}

_aocd__answer() {
    year="$1"
    day="$2"
    [ "$3" = 1 ] && part=a || part=b
    [ ${#year} = 4 ] || { echo "Provide year as yyyy"; return 1; }
    [ ${#day} = 2 ] || { echo "Provide day as dd"; return 1; }
    _aocd__answerfile="$_aocd__memodir"/"$_aocd__userid"/"$year"_"$day""$part"_answer.txt
    [ -f "$_aocd__answerfile" ] || { echo ""; return 0; }
    IFS='' read -r _aocd__answer < "$_aocd__answerfile"
    echo "$_aocd__answer"
    return 0
}

_aocd__check() {
    local year="$1"
    local day="$2"
    _aocd__part1="$3"
    _aocd__part2="$4"
    _aocd__answer1=$(_aocd__answer "$year" "$day" 1) \
        || { echo "$_aocd__answer1"; return 1; }
    if [ -n "$_aocd__part1" ] && [ -n "$_aocd__answer1" ] \
        && [ "$_aocd__part1" != "$_aocd__answer1" ]
    then
        _aocd_check1_fail="Part 1: Expected: $_aocd__answer1, got: $_aocd__part1"
    fi
    _aocd__answer2=$(_aocd__answer "$year" "$day" 2) \
        || { echo "$_aocd__answer2"; return 1; }
    if [ -n "$_aocd__part2" ] && [ -n "$_aocd__answer2" ] \
        && [ "$_aocd__part2" != "$_aocd__answer2" ]
    then
        _aocd_check2_fail="Part 2: Expected: $_aocd__answer2, got: $_aocd__part2"
    fi
    if [ -n "$_aocd_check1_fail" ] || [ -n "$_aocd_check2_fail" ]; then
        printf '%s\n%s' "$_aocd_check1_fail" "$_aocd_check2_fail"
        return 1
    fi
    return 0
}
