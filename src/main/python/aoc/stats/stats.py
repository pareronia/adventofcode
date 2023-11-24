import sys
from datetime import date

from .input import get_stats
from .input import get_leaderboard
from .output import print_stats


def main(arg: str) -> None:
    year = date.today().year if arg is None else int(arg)
    stats = get_stats(year)
    leaderboard = get_leaderboard(year)
    lines = print_stats(year, stats, leaderboard)
    [print(_, file=sys.stdout) for _ in lines]
