# shellcheck shell=bash

E_PARAM_ERR=98
E_ASSERT_FAILED=99
RED='\033[1;31m'
NC='\033[0m' # No Color

# assert
# If condition false, exit from script with appropriate error message.
# https://tldp.org/LDP/abs/html/debugging.html#ASSERT
#
assert () {
    if [ -z "$2" ]; then  #  Not enough parameters passed to assert() function.
        return $E_PARAM_ERR   #  No damage done.
    fi

    local lineno=$2

    local cmd="if [ ! $1 ]; then"
    if [ -n "$3" ]; then
        local msg="$3 \($1\)"
    else
        local msg="$1"
    fi
    cmd+=" echo \"Assertion failed: \"$msg\"\" >&2;"
    cmd+=" echo \"File \"$0\", line $lineno\" >&2;"  # Give name of file and line number.
    cmd+=" exit $E_ASSERT_FAILED;"
    # else
    #   return
    #   and continue executing the script.
    cmd+="fi"
    eval "$cmd"
}

red() {
    if [ -t 1 ]; then 
        echo -e "$RED$*$NC"
    else 
        echo "$*"
    fi
}

fatal() {
    local exitcode="$1"
    shift
    red "**** FATAL: $*" >&2
    exit "$exitcode"
}

TEST() {
    local part="$1"
    local arg="$2[@]"
    local lines=("${!arg}")
    local expected="$3"
    local actual
    actual="$("$part" <(for line in "${lines[@]}"; do echo "$line"; done))"
    local msg="FAILED $part SAMPLE $2: expected '$expected', got '$actual'"
    [ "$actual" != "$expected" ] \
        && fatal "$E_ASSERT_FAILED" "$msg" >&2
}
