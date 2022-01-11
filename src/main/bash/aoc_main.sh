# shellcheck shell=bash

main() {
    source "$(dirname "$0")/aocd/aocd.sh"

    inputfile="$(_aocd__inputFile "$year" "$day")" \
        || fatal 1 "Input file not found: $inputfile"

    part1="$(part1 "$inputfile")"
    echo "Part 1: $part1"
    part2="$(part2 "$inputfile")"
    echo "Part 2: $part2"

    check="$(_aocd__check "$year" "$day" "$part1" "$part2")" || fatal 1 "$check"
}

source "$(dirname "$0")/aoc.sh"

if [ $# = 0 ]; then
    [ "$(type -t tests)" == "function" ] && tests
    main
else
    [ $# = 2 ] || fatal $E_PARAM_ERR "Expected 2 params: <part> <inputfile>"
    [ -f "$2" ] || fatal $E_PARAM_ERR "Input file not found: $2"
    "part$1" "$2"
fi
exit $?
