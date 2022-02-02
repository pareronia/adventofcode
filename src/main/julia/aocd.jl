module Aocd

using Printf

function getAocdDir()
    try
        return ENV["AOCD_DIR"]
    catch KeyError
        try
            return joinpath(ENV["HOME"], ".config/aocd")
        catch KeyError
            throw(SystemError("aocd dir not found"))
        end
    end
end

function getToken()
    try
        return ENV["AOC_SESSION"]
    catch KeyError
        lines = readlines(joinpath(getAocdDir(), "token"))
        if isempty(lines)
            throw(SystemError("Missing session id"))
        else
            return lines[1]
        end
    end
end

function getUserIds()
    lines = readlines(joinpath(getAocdDir(), "token2id.json"))
    if isempty(lines)
        throw(SystemError("Missing token2id.json"))
    else
        # Not ideal, but JSON is too slow...
        userIds = Dict()
        for line ∈ lines
            m = match(r""".*"(.*)": "(.*)",?""", line)
            if m != nothing
                userIds[m.captures[1]] = m.captures[2]
            end
        end
        return userIds
    end
end

function getMemoDir()
    return joinpath(getAocdDir(), getUserIds()[getToken()])
end

function dayFormat(year::Int, day::Int)
    return @sprintf("%d_%02d", year, day)
end

function getInputData(year::Int, day::Int)
    return readlines(joinpath(getMemoDir(),
                              dayFormat(year, day) * "_input.txt"))
end

function getAnswer(year::Int, day::Int, partnum::Int)
    part = partnum == 1 ? "a" : "b"
    lines = readlines(joinpath(getMemoDir(),
                               dayFormat(year, day) * part * "_answer.txt"))
    return lines[1]
end

function check(year::Int, day::Int, part1, part2)
    answer1 = getAnswer(year, day, 1)
    fail1 = ""
    if !isempty(answer1) && !isempty(part1) && answer1 != string(part1)
        fail1 = string("Part 1: Expected: ", answer1, ", got: ", part1)
    end
    answer2 = getAnswer(year, day, 2)
    fail2 = ""
    if !isempty(answer2) && !isempty(part2) && answer2 != string(part2)
        fail2 = string("Part 2: Expected: ", answer2, ", got: ", part2)
    end
    if !isempty(fail1) || !isempty(fail2)
        throw(AssertionError(join([fail1, fail2], '\n')))
    end
end

end  # module Aocd
