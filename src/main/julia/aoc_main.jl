using Printf

function aoc_main(script, args, year, day)
    if abspath(PROGRAM_FILE) != script
        return nothing
    end
    @time begin
        ds = @sprintf("AoC%d_%02d", year, day)
        modsymbol = Symbol(ds)
        part1symbol = Symbol("part1")
        part2symbol = Symbol("part2")
        input = Aocd.get_input_data(year, day)
        @eval $modsymbol.samples()
        ans1 = @timed @eval $modsymbol.$part1symbol($input)
        println("Part 1: $(ans1.value), took: $(ans1.time * 1000) ms")
        ans2 = @timed @eval $modsymbol.$part2symbol($input)
        println("Part 2: $(ans2.value), took: $(ans2.time * 1000) ms")
        Aocd.check(year, day, ans1.value, ans2.value)
    end
end
