#!/bin/bash
#
# Advent of Code 2021 Day 1
#

mapfile -t depths
size="${#depths[@]}"
count1=0
count2=0

for ((i = 1; i < size; i++))
do
    if (("${depths[i]}" > "${depths[i-1]}")); then
        count1=$((count1+1))
    fi
done

for ((i = 1; i < ((size - 2)); i++))
do
    sum1=$(("${depths[i]}" + "${depths[i+1]}" + "${depths[i+2]}"))
    sum2=$(("${depths[i-1]}" + "${depths[i]}" + "${depths[i+1]}"))
    if (("$sum1" > "$sum2")); then
        count2=$((count2+1))
    fi
done

echo "Part 1: $count1"
echo "Part 2: $count2"

