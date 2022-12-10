module OCR

A6 = Vector{String}([
    ".##.",
    "#..#",
    "#..#",
    "####",
    "#..#",
    "#..#"
   ])

B6 = Vector{String}([
    "###.",
    "#..#",
    "###.",
    "#..#",
    "#..#",
    "###."
   ])

C6 = Vector{String}([
    ".##.",
    "#..#",
    "#...",
    "#...",
    "#..#",
    ".##."
   ]);

E6 = Vector{String}([
    "####",
    "#...",
    "###.",
    "#...",
    "#...",
    "####"
   ])

F6 = Vector{String}([
    "####",
    "#...",
    "###.",
    "#...",
    "#...",
    "#..."
   ])

G6 = Vector{String}([
    ".##.",
    "#..#",
    "#...",
    "#.##",
    "#..#",
    ".###"
   ])

H6 = Vector{String}([
    "#..#",
    "#..#",
    "####",
    "#..#",
    "#..#",
    "#..#"
   ])

I6 = Vector{String}([
    ".###",
    "..#.",
    "..#.",
    "..#.",
    "..#.",
    ".###"
   ])

J6 = Vector{String}([
    "..##",
    "...#",
    "...#",
    "...#",
    "#..#",
    ".##."
   ])

K6 = Vector{String}([
    "#..#",
    "#.#.",
    "##..",
    "#.#.",
    "#.#.",
    "#..#"
   ])

L6 = Vector{String}([
    "#...",
    "#...",
    "#...",
    "#...",
    "#...",
    "####"
   ])

O6 = Vector{String}([
    ".##.",
    "#..#",
    "#..#",
    "#..#",
    "#..#",
    ".##."
   ])

P6 = Vector{String}([
    "###.",
    "#..#",
    "#..#",
    "###.",
    "#...",
    "#..."
   ])

R6 = Vector{String}([
    "###.",
    "#..#",
    "#..#",
    "###.",
    "#.#.",
    "#..#"
   ])

S6 = Vector{String}([
    ".###",
    "#...",
    "#...",
    ".##.",
    "...#",
    "###."
   ])

U6 = Vector{String}([
    "#..#",
    "#..#",
    "#..#",
    "#..#",
    "#..#",
    ".##."
   ])

Y6 = Vector{String}([
    "#...",
    "#...",
    ".#.#",
    "..#.",
    "..#.",
    "..#."
   ])

Z6 = Vector{String}([
    "####",
    "...#",
    "..#.",
    ".#..",
    "#...",
    "####"
   ])

GLYPHS = Dict{Vector{String},Char}(
    A6 => 'A',
    B6 => 'B',
    C6 => 'C',
    E6 => 'E',
    F6 => 'F',
    G6 => 'G',
    H6 => 'H',
    I6 => 'I',
    J6 => 'J',
    K6 => 'K',
    L6 => 'L',
    O6 => 'O',
    P6 => 'P',
    R6 => 'R',
    S6 => 'S',
    U6 => 'U',
    Y6 => 'Y',
    Z6 => 'Z',
)

function convert_6(grid::Vector{String}, fill::Char, empty::Char)
    glyphs = []
    for line ∈ grid
        push!(glyphs, replace(replace(line, fill => '#'), empty => '.'))
    end
    ans = ""
    for i ∈ 1:5:length(first(glyphs))
        glyph = []
        for line ∈ glyphs
            push!(glyph, line[i:i+3])
        end
        ans *= GLYPHS[glyph]
    end
    return ans
end

end  # module OCR
