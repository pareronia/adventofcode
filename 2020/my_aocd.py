''' Some aocd util methods '''
import aocd


def get_input_as_list(year: int, day: int, expected: int = None) -> list[str]:
    inputs = aocd.get_data(year=year, day=day).splitlines()
    if expected is not None and len(inputs) != expected:
        raise AssertionError(f"Expected {expected} lines")
    return inputs


def get_input_as_tuple(year: int, day: int,
                       expected: int = None) -> tuple[str]:
    return tuple(get_input_as_list(year, day, expected))


def get_input_as_ints_tuple(year: int, day: int,
                            expected: int) -> tuple[int]:
    return tuple(int(s) for s in get_input_as_list(year, day, expected))


def print_header(year: int, day: int) -> None:
    print("====================================================")
    print(f"AoC {year} Day {day} - https://adventofcode.com/{year}/day/{day}")
    print("====================================================")
    print("")
