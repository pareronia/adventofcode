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
    basename=$(basename "$0")
    echo "$_aocd__memodir/$_aocd__userid/${basename:3:7}_input.txt"
}

_aocd__inputfile=$(_aocd__inputFile)
