module Aocd

using JSON
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
        json = JSON.parse(join(lines))
        return json
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

end  # module Aocd
