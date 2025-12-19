from datetime import timedelta
from typing import NamedTuple

STATS_URL = "https://adventofcode.com/{year}/stats"
LEADERBOARD_URL = "https://adventofcode.com/{year}/leaderboard/self"

AocdUserStats = dict[str, dict[str, dict[str, timedelta | int]]]


class LeaderBoard(NamedTuple):
    time_first: str | None
    rank_first: int | None
    score_first: int | None
    time_both: str | None
    rank_both: int | None
    score_both: int | None


class Stats(NamedTuple):
    both: int
    first_only: int


class Summary(NamedTuple):
    best_time_first: tuple[int, int, int]
    best_rank_first: tuple[int, int, int]
    best_time_both: tuple[int, int, int]
    best_rank_both: tuple[int, int, int]
    best_avg_time_first: tuple[int, int]
    best_avg_time_both: tuple[int, int]
