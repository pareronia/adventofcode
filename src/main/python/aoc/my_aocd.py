''' Some aocd util methods '''
import aocd


def to_blocks(inputs: tuple[str]) -> list[list[str]]:
    blocks = list[list[str]]()
    idx = 0
    blocks.append([])
    for input_ in inputs:
        if len(input_) == 0:
            blocks.append([])
            idx += 1
        else:
            blocks[idx].append(input_)
    return blocks


def get_input(year: int, day: int, expected: int = None) -> tuple[str]:
    inputs = aocd.get_data(year=year, day=day).splitlines()
    if expected is not None and len(inputs) != expected:
        raise AssertionError(f"Expected {expected} lines")
    return tuple(inputs)


def print_header(year: int, day: int) -> None:
    s = "AoC {year} Day {day}" \
        " - https://adventofcode.com/{year}/day/{day}" \
        .format(year=year, day=day)
    print("=" * len(s))
    print(s)
    print("=" * len(s))
    puzzle = aocd.models.Puzzle(year, day)
    print(puzzle.title)
    print("=" * len(s))
    print("")
