from typing import NamedTuple

STATS_URL = "https://adventofcode.com/{year}/stats"
LEADERBOARD_URL = "https://adventofcode.com/{year}/leaderboard/self"


class LeaderBoard(NamedTuple):
    time_first: str
    rank_first: int
    score_first: int
    time_both: str
    rank_both: int
    score_both: int


class Stats(NamedTuple):
    both: int
    first_only: int
