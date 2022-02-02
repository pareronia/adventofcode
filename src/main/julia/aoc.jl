macro aoc_main(year::Int, day::Int)
    function main(args)
        if isempty(args)
            @time begin
                samples()
                input = Aocd.getInputData(year, day)
                ans1 = @timed part1(input)
                println("Part 1: $(ans1.value), took: $(ans1.time * 1000) ms")
                ans2 = @timed part2(input)
                println("Part 2: $(ans2.value), took: $(ans2.time * 1000) ms")
                Aocd.check(year, day, ans1.value, ans2.value)
            end
        else
            if args[1] == "1"
                println(part1(readlines(args[2])))
            else
                println(part2(readlines(args[2])))
            end
        end
    end

    main(ARGS)
end

macro aoc_splitlines(lines::String)
    split(rstrip(lines, ['\r', '\n']), r"\r?\n")
end
