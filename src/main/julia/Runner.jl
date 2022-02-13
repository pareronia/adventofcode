module Runner

include("aocd.jl")
using .Aocd
using Sockets
using Logging

mods = Dict{Tuple{Int, Int}, Symbol}()
closed = false;

function run(year::Int, day::Int, part::Int, input::Vector{String})
    @debug "run called: $year, $day, $part"
    if (year, day) ∉ keys(mods)
        return
    end
    mod = mods[(year, day)]
    @eval begin
        if $part == 1
            ans = @timed Runner.$mod.part1($input)
        else
            ans = @timed Runner.$mod.part2($input)
        end
    end
    return ans.value
end

function server()
    Logging.global_logger(ConsoleLogger(stderr, Logging.Info))
    return @task begin
         server = listen(5556)
         while !closed
             @debug "Server waiting..."
             sock = accept(server)
             @debug "Client connected"
             req = Vector{String}()
             @async while isopen(sock)
                 str = strip(readline(sock, keep=true))
                 @debug "Received: '$str'"
                 if str == "HELLO"
                     write(sock, "HELLO")
                     close(sock)
                 elseif str == "END"
                     @debug "Ready: $req"
                     try
                         year, day, part, inputfile = req
                         @debug "Params: $year, $day, $part, $inputfile"
                         ans = run(parse(Int, year),
                                   parse(Int, day),
                                   parse(Int, part),
                                   readlines(inputfile)
                                  )
                         @debug "Result: $ans"
                         write(sock, isnothing(ans) ? "" : string(ans))
                     catch e
                         @error e
                     finally
                         close(sock)
                     end
                 elseif str == "STOP"
                     @debug "Stopping server"
                     closed = true
                     close(sock)
                     break
                 else
                     push!(req, str)
                 end
             end
         end
         close(server)
         notify(ready)
     end
end

function run_all()
    results = []
    for ((year, day), mod) ∈ mods
        @show year, day
        @eval begin
            input = Aocd.get_input_data($year, $day)
            time1 = @timed println(Runner.$mod.part1(input))
            input = Aocd.get_input_data($year, $day)
            time2 = @timed println(Runner.$mod.part2(input))
            push!(results, time1)
            push!(results, time2)
        end
    end
    println(sum(r.time for r ∈ results))
end

for (root, dirs, files) ∈ walkdir(".")
    for file ∈ files
        m = match(r"^AoC(20[0-9]{2})_([0-2][0-9])\.jl$", file)
        if isnothing(m)
            continue
        end
        year = parse(Int, m.captures[1])
        day = parse(Int, m.captures[2])
        mod = file[begin:findfirst(==('.'), file) - 1]
        mods[(year, day)] = Symbol(mod)
        include(file)
    end
end

end # module Runner

using .Runner

t = Runner.server()
ready = Condition()
schedule(t); wait(ready)
