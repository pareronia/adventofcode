# shellcheck shell=bash

main_part() {
    local -i start; local part; local -i end
    start="$(date +%s%N)"
    part="$("part$1" "$2")"
    end="$(date +%s%N)"
    echo "$part $((end - start))"
    return 0
}

source "$(dirname "$0")/aoc.sh"

if [ $# = 0 ]; then
    [ "$(type -t tests)" == "function" ] && tests

    source "$(dirname "$0")/aocd/aocd.sh"

    inputfile="$(_aocd__inputFile "$year" "$day")" \
        || fatal 1 "Input file not found: $inputfile"

    read -ra result1 <<< "$(main_part "1" "$inputfile")"
    echo "Part 1: ${result1[0]}, took: $((result1[1] / 1000000)) ms"
    read -ra result2 <<< "$(main_part "2" "$inputfile")"
    echo "Part 2: ${result2[0]}, took: $((result2[1] / 1000000)) ms"

    check="$(_aocd__check "$year" "$day" "${result1[0]}" "${result2[0]}")" \
        || fatal 1 "$check"
else
    [ $# = 2 ] || fatal 1 "Expected 2 params: <part> <inputfile>"
    [ -f "$2" ] || fatal 1 "Input file not found: $2"
    read -ra result <<< "$(main_part "$1" "$2")"
    printf "{\"part$1\":{\"answer\":\"%s\",\"duration\":%d}}\n" \
        "${result[0]}" "${result[1]}"
fi
exit $?
