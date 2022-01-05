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
