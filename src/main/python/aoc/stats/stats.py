import sys
from datetime import date

from . import input
from . import output


def main(args: list[str]) -> None:
    if len(args) == 0:
        if date.today().month == 12:
            print_year(date.today().year)
    elif args[0] == "all":
        print_summary()
    else:
        print_year(int(args[0]))


def print_year(year: int) -> None:
    stats = input.get_stats(year)
    leaderboard = input.get_leaderboard(year)
    lines = output.print_stats(year, stats, leaderboard)
    [print(_, file=sys.stdout) for _ in lines]


def print_summary() -> None:
    print(input.get_summary())
