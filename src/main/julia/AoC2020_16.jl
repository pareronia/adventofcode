#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2020_16

include("aoc.jl")
using DataStructures

struct Rule
    field::String
    validRanges::Set{Tuple{Int,Int}}
end

struct Ticket
    values::Tuple{Vararg{Int}}
end

struct Matches
    matches::Dict{Rule,Set{Int}}
end

function validate(rule::Rule, value::Int)
    return any(rng[1] <= value <= rng[2] for rng ∈ rule.validRanges)
end

function parseRule(line::String)
    split1 = split(line, ": ")
    ranges = Set(Tuple(parse.(Int, s) for s ∈ split(sp, "-"))
                 for sp ∈ split(split1[2], " or "))
    return Rule(split1[1], ranges)
end

function parseTicket(line::String)
    return Ticket(Tuple([parse.(Int, s) for s ∈ split(line, ",")]))
end

function _parse(input)
    blocks::Vector{Vector{String}} = aoc_to_blocks(input)
    rules = Tuple(parseRule(line) for line ∈ blocks[1])
    my_ticket = parseTicket(blocks[2][2])
    tickets = Tuple(parseTicket(line) for line ∈ blocks[3][2:end])
    return rules, my_ticket, tickets
end

function doesNotMatchAnyRule(value::Int, rules::Tuple{Vararg{Rule}})
    return !any(r -> validate(r, value), rules)
end

function getInvalidValues(ticket::Ticket, rules::Tuple{Vararg{Rule}})
    return Tuple(v for v ∈ ticket.values if doesNotMatchAnyRule(v, rules))
end

function isInvalid(ticket::Ticket, rules::Tuple{Vararg{Rule}})
    return any(doesNotMatchAnyRule(v, rules) for v ∈ ticket.values)
end

function createMatches(tickets::Tuple{Vararg{Ticket}}, rules::Tuple{Vararg{Rule}})
    validTickets = Tuple(t for t ∈ tickets if !isInvalid(t, rules))
    matches = DefaultDict{Rule,Set{Int}}(() -> Set{Int}())
    for rule ∈ rules
        for idx ∈ 1:length(tickets[1].values)
            if all(validate(rule, t.values[idx]) for t ∈ validTickets)
                push!(matches[rule], idx)
            end
        end
    end
    return Matches(matches)
end

function removeMatch(matches::Matches, match::Tuple{Rule,Int})
    rule, idx = match
    delete!(matches.matches, rule)
    foreach(k -> delete!(matches.matches[k], idx), keys(matches.matches))
end

function next(matches::Matches)
    rule = [kv[1] for kv ∈ matches.matches if length(kv[2]) == 1][1]
    idx = [i for i ∈ matches.matches[rule]][1]
    removeMatch(matches, Tuple([rule, idx]))
    return Tuple([rule, idx])
end

function part1(input)
    rules, my_ticket, tickets = _parse(input)
    return sum(v for t ∈ tickets for v ∈ getInvalidValues(t, rules))
end

function part2(input)
    rules, my_ticket, tickets = _parse(input)
    matches = createMatches(tickets, rules)
    ans = 1
    while length(matches.matches) > 0
        rule, idx = next(matches)
        if startswith(rule.field, "departure ")
            ans *= my_ticket.values[idx]
        end
    end
    return ans
end

TEST1 = @aoc_splitlines("""\
class: 1-3 or 5-7
row: 6-11 or 33-44
seat: 13-40 or 45-50

your ticket:
7,1,14

nearby tickets:
7,3,47
40,4,50
55,2,20
38,6,12
""")
TEST2 = @aoc_splitlines("""\
departure date: 0-1 or 4-19
departure time: 0-5 or 8-19
departure track: 0-13 or 16-19

your ticket:
11,12,13

nearby tickets:
3,9,18
15,1,5
5,14,9
""")

function samples()
    @assert part1(TEST1) == 71
    @assert part2(TEST2) == 1716
end

end # module AoC2020_16

aoc_main(@__FILE__, ARGS, 2020, 16)
