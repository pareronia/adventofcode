import datetime
import sys

import dateutil

from . import input as stats_input
from . import output as stats_output


def main(args: list[str]) -> None:
    now = datetime.datetime.now(tz=dateutil.tz.gettz("America/New_York"))
    if len(args) == 0:
        if now.month == 12:
            print_year(now.year)
    elif args[0] == "all":
        print_summary()
    else:
        print_year(int(args[0]))


def print_year(year: int) -> None:
    stats = stats_input.get_stats(year)
    leaderboard = stats_input.get_leaderboard(year)
    lines = stats_output.print_stats(year, stats, leaderboard)
    [print(_, file=sys.stdout) for _ in lines]


def print_summary() -> None:
    print(stats_input.get_summary())
