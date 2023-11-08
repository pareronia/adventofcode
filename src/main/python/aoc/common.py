from typing import Any
from typing import Callable
from typing import TypeVar
from typing import cast

import aocd
from aoc import my_aocd
from prettyprinter import cpprint


def clog(c: Callable[[], object]) -> None:
    if __debug__:
        log(c())


def log(msg: object) -> None:
    if __debug__:
        cpprint(msg)


def spinner(num: int, period: int = 1000) -> None:
    if not __debug__:
        return
    val = num % period
    level = period // 4
    if val == 0:
        ch = "|"
    elif val == level:
        ch = "/"
    elif val == level * 2:
        ch = "-"
    elif val == level * 3:
        ch = "\\"
    else:
        return
    print(ch, end="\r", flush=True)


F = TypeVar("F", bound=Callable[..., Any])


def aoc_main(
    year: int,
    day: int,
    part_1: Callable[[tuple[str, ...]], str | int],
    part_2: Callable[[tuple[str, ...]], str | int],
) -> Callable[[F], F]:
    def decorator(func: F) -> F:
        def wrapper(*args: Any) -> Any:
            puzzle = aocd.models.Puzzle(year, day)
            my_aocd.print_header(puzzle.year, puzzle.day)
            func(*args)
            inputs = my_aocd.get_input_data(puzzle)
            result1 = part_1(inputs)
            print(f"Part 1: {result1}")
            result2 = part_2(inputs)
            print(f"Part 2: {result2}")
            my_aocd.check_results(puzzle, result1, result2)

        return cast(F, wrapper)

    return decorator
