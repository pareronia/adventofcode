#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd

const MARKED = -1

function _parse(input)
    blocks = aoc_to_blocks(input)
    draws = parse.(Int, split(blocks[1][1], ","))
    boards = Vector{Matrix{Int}}()
    for i ∈ 2:length(blocks)
        grid = Vector{Vector{Int}}()
        for row ∈ blocks[i]
            push!(grid, parse.(Int, split(row)))
        end
        push!(boards, hcat(grid...))
    end
    return draws, boards
end

function solve(draws, boards, stop_count)
    function mark!(board, number)
        for idx ∈ eachindex(board)
            board[idx] == number && (board[idx] = MARKED)
        end
    end
    function win(board)
        for row ∈ eachrow(board)
            all(==(MARKED), row) && return true
        end
        for col ∈ eachcol(board)
            all(==(MARKED), col) && return true
        end
        return false
    end
    winners = falses(length(boards))
    for draw ∈ draws, (i, board) ∈ enumerate(boards)
        winners[i] && continue
        mark!(board, draw)
        win(board) && (winners[i] = true)
        if count(==(true), winners) == stop_count
            return draw * sum(x for x ∈ board if x != MARKED)
        end
    end
    error("Unsolvable")
end

function part1(input)
    draws, boards = _parse(input)
    return solve(draws, boards, 1)
end

function part2(input)
    draws, boards = _parse(input)
    return solve(draws, boards, length(boards))
end

TEST = @aoc_splitlines("""
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
""")

function samples()
    @assert part1(TEST) == 4512
    @assert part2(TEST) == 1924
end

@aoc_main 2021 4
