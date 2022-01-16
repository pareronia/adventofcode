# shellcheck shell=bash

# shellcheck disable=SC2034
{
    declare -a ocr_A6=(
    ".##."
    "#..#"
    "#..#"
    "####"
    "#..#"
    "#..#"
    )
    declare -a ocr_B6=(
    "###."
    "#..#"
    "###."
    "#..#"
    "#..#"
    "###."
    )
    declare -a ocr_C6=(
    ".##."
    "#..#"
    "#..."
    "#..."
    "#..#"
    ".##."
    )
    declare -a ocr_E6=(
    "####"
    "#..."
    "###."
    "#..."
    "#..."
    "####"
    )
    declare -a ocr_F6=(
    "####"
    "#..."
    "###."
    "#..."
    "#..."
    "#..."
    )
    declare -a ocr_G6=(
    ".##."
    "#..#"
    "#..."
    "#.##"
    "#..#"
    ".###"
    )
    declare -a ocr_H6=(
    "#..#"
    "#..#"
    "####"
    "#..#"
    "#..#"
    "#..#"
    )
    declare -a ocr_I6=(
    ".###"
    "..#."
    "..#."
    "..#."
    "..#."
    ".###"
    )
    declare -a ocr_J6=(
    "..##"
    "...#"
    "...#"
    "...#"
    "#..#"
    ".##."
    )
    declare -a ocr_K6=(
    "#..#"
    "#.#."
    "##.."
    "#.#."
    "#.#."
    "#..#"
    )
    declare -a ocr_L6=(
    "#..."
    "#..."
    "#..."
    "#..."
    "#..."
    "####"
    )
    declare -a ocr_O6=(
    ".##."
    "#..#"
    "#..#"
    "#..#"
    "#..#"
    ".##."
    )
    declare -a ocr_P6=(
    "###."
    "#..#"
    "#..#"
    "###."
    "#..."
    "#..."
    )
    declare -a ocr_R6=(
    "###."
    "#..#"
    "#..#"
    "###."
    "#.#."
    "#..#"
    )
    declare -a ocr_S6=(
    ".###"
    "#..."
    "#..."
    ".##."
    "...#"
    "###."
    )
    declare -a ocr_U6=(
    "#..#"
    "#..#"
    "#..#"
    "#..#"
    "#..#"
    ".##."
    )
    declare -a ocr_Y6=(
    "#..."
    "#..."
    ".#.#"
    "..#."
    "..#."
    "..#."
    )
    declare -a ocr_Z6=(
    "####"
    "...#"
    "..#."
    ".#.."
    "#..."
    "####"
    )
}

declare ocr_letters6="A6 B6 C6 E6 F6 G6 H6 I6 J6 K6 L6 O6 P6 R6 S6 U6 Y6 Z6"

ocr_find6() {
    for l in $ocr_letters6; do
        local name="ocr_${l}[@]"
        local -a letter=("${!name}")
        if [ "$*" = "${letter[*]}" ]; then
            echo "${name:4:1}"
            return 0
        fi
    done
    echo "Not found"
    exit 1
}

ocr_convert6() {
    local fill="$1"
    shift
    local empty="$1"
    shift
    local -i size="${#1}"
    local -a glyphs=()
    for x in "$@"; do
        local glyph="${x//$empty/.}"
        glyph="${glyph//"$fill"/\#}"
        glyphs+=("$glyph")
    done
    for ((i = 0; i < "$size"; i += 5)); do
        local -a glyph=()
        for x in "${glyphs[@]}"; do
            glyph+=("${x:i:4}")
        done
        printf "%s" "$(ocr_find6 "${glyph[@]}")"
    done
}

declare -a ocr_glyphs6=(
" **  ***   **  **** ****  **  *  *  ***   ** *  * *     **  ***  ***   *** *  * *   ***** "
"*  * *  * *  * *    *    *  * *  *   *     * * *  *    *  * *  * *  * *    *  * *   *   * "
"*  * ***  *    ***  ***  *    ****   *     * **   *    *  * *  * *  * *    *  *  * *   *  "
"**** *  * *    *    *    * ** *  *   *     * * *  *    *  * ***  ***   **  *  *   *   *   "
"*  * *  * *  * *    *    *  * *  *   *  *  * * *  *    *  * *    * *     * *  *   *  *    "
"*  * ***   **  **** *     *** *  *  ***  **  *  * ****  **  *    *  * ***   **    *  **** "
)
[ "$(ocr_convert6 "*" " " "${ocr_glyphs6[@]}")" = "ABCEFGHIJKLOPRSUYZ" ] \
    || exit 1
