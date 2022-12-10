#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_10

include("aoc.jl")
include("ocr.jl")

FILL = '#'
EMPTY = ' '

function _parse(input)
    xs = Vector{Int8}()
    x = 1
    for line ∈ input
        if line == "noop"
            push!(xs, x)
        else
            push!(xs, x)
            push!(xs, x)
            x += parse(Int8, line[6:end])
        end
    end
    return xs
end

function get_pixels(input)
    function draw(cycles, x)
        return (cycles - 1) % 40 ∈ x-1:x+1 ? FILL : EMPTY
    end
    pixels = join((draw(i, x) for (i, x) ∈ enumerate(_parse(input))), "")
    return [pixels[i:i+39] for i ∈ 1:40:240]
end

function part1(input)
    program = _parse(input)
    return sum(i * program[i] for i ∈ (20, 60, 100, 140, 180, 220))
end

function part2(input)
    return OCR.convert_6(get_pixels(input), FILL, EMPTY)
end

TEST = @aoc_splitlines("""
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
""")
RESULT = @aoc_splitlines("""
##  ##  ##  ##  ##  ##  ##  ##  ##  ##  
###   ###   ###   ###   ###   ###   ### 
####    ####    ####    ####    ####    
#####     #####     #####     #####     
######      ######      ######      ####
#######       #######       #######     
""")

function samples()
    @assert part1(TEST) == 13_140
    @assert get_pixels(TEST) == RESULT
end

end # module AoC2022_10

aoc_main(@__FILE__, ARGS, 2022, 10)
