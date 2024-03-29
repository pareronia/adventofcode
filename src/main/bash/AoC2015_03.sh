#!/bin/bash
#
# Advent of Code 2015 Day 3
#

year=2015
day=03

part1() {
    declare -A visited
    local curr="0,0"
    visited["0,0"]=1
    while read -r -n1 char; do
        OLDIFS="$IFS"; IFS=","
        # shellcheck disable=SC2206
        local fields=($curr)
        IFS="$OLDIFS"
        local x="${fields[0]}"
        local y="${fields[1]}"
        case "$char" in
            ^)
                curr="$x,$((y+1))"
                ;;
            ">")
                curr="$((x+1)),$y"
                ;;
            v)
                curr="$x,$((y-1))"
                ;;
            "<")
                curr="$((x-1)),$y"
                ;;
        esac
        visited["$curr"]=1
    done < "$1"
    echo "${#visited[@]}"
}

part2() {
    declare -A visited
    local currS="0,0"
    local currR="0,0"
    visited["0,0"]=1
    local i=0
    while read -r -n1 char; do
        if [ $((i % 2)) != 0 ]; then
            curr="$currR"
        else
            curr="$currS"
        fi
        OLDIFS="$IFS"; IFS=","
        # shellcheck disable=SC2206
        local fields=($curr)
        IFS="$OLDIFS"
        local x="${fields[0]}"
        local y="${fields[1]}"
        case "$char" in
            ^)
                curr="$x,$((y+1))"
                ;;
            ">")
                curr="$((x+1)),$y"
                ;;
            v)
                curr="$x,$((y-1))"
                ;;
            "<")
                curr="$((x-1)),$y"
                ;;
        esac
        if [ $((i % 2)) != 0 ]; then
            currR="$curr"
        else
            currS="$curr"
        fi
        visited["$curr"]=1
        ((i++))
    done < "$1"
    echo "${#visited[@]}"
}

tests() {
    # shellcheck disable=SC2034
    {
        sample1=(">")
        sample2=("^>v<")
        sample3=("^v^v^v^v^v")
        sample4=("^v")
    }

    TEST part1 sample1 2
    TEST part1 sample2 4
    TEST part1 sample3 2
    TEST part2 sample4 3
    TEST part2 sample2 3
    TEST part2 sample3 11
}

source "$(dirname "$0")/aoc_main.sh"
