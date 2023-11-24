#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_11

include("aoc.jl")

struct Monkey
    items::Vector{Int}
    operation::Function
    test::Int8
    throw_true::Int8
    throw_false::Int8
end

function _parse(input)
    function add(n::Int)
        return x -> x + n
    end
    
    function mul(n::Int)
        return x -> x * n
    end

    function square(n::Int)
        return n * n
    end

    function parse_monkey(block)
        items = parse.(Int, m.match for m ∈ eachmatch(r"([0-9]+)", block[2]))
        splits = split(split(block[3], " = ")[2])
        if splits[3] == "old"
            operation = square
        elseif splits[2] == "+"
            operation = add(parse(Int, splits[3]))
        elseif splits[2] == "*"
            operation = mul(parse(Int, splits[3]))
        end
        test = parse(Int, last(split(block[4])))
        throw_true = parse(Int, last(split(block[5]))) + 1
        throw_false = parse(Int, last(split(block[6]))) + 1
        return Monkey(items, operation, test, throw_true, throw_false)
    end

    return [parse_monkey(block) for block ∈ aoc_to_blocks(input)]
end


function round(monkeys, counter, manage)
    for (i, monkey) ∈ enumerate(monkeys)
        for item ∈ monkey.items
            level = manage(monkey.operation(item))
            if level % monkey.test == 0
                push!(monkeys[monkey.throw_true].items, level)
            else
                push!(monkeys[monkey.throw_false].items, level)
            end
        end
        counter = merge(+, counter, Dict(i => length(monkey.items)))
        empty!(monkey.items)
    end
    return counter
end

function solve(monkeys, rounds, manage)
    counter = Dict{Int, Int}()
    for _ ∈ 1:rounds
        counter = round(monkeys, counter, manage)
    end
    return prod(last(sort(collect(values(counter))), 2))
end

function part1(input)
    monkeys = _parse(input)
    return solve(monkeys, 20, x -> x ÷ 3)
end

function part2(input)
    monkeys = _parse(input)
    mod = prod(monkey.test for monkey ∈ monkeys)
    return solve(monkeys, 10_000, x -> x % mod)
end

TEST = @aoc_splitlines("""
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
""")

function samples()
    @assert part1(TEST) == 10_605
    @assert part2(TEST) == 2_713_310_158
end

end # module AoC2022_11

aoc_main(@__FILE__, ARGS, 2022, 11)
