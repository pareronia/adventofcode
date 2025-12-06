import html
import logging
import re
from datetime import timedelta

from aoc import calendar
from tqdm import tqdm

from . import LeaderBoard
from . import Stats
from . import Summary
from .aocd import get_leaderboard_page
from .aocd import get_stats_page
from .aocd import get_user_stats

log = logging.getLogger("aoc.stats")


def get_stats(year: int) -> dict[int, Stats]:
    stats = dict[int, Stats]()
    r = get_stats_page(year)
    lines = re.findall(r'<a.*<span class="stats-both">.*</span>.*</a>', r)
    for line in lines:
        m = re.search(r'<a href="/[0-9]{4}/day/([0-9]+)">', line)
        if m is None:
            raise ValueError
        day = int(m.group(1))
        m = re.search(r'<span class="stats-both">([ 0-9]+)</span>', line)
        if m is None:
            raise ValueError
        both = int(m.group(1))
        m = re.search(r'<span class="stats-firstonly">([ 0-9]+)</span>', line)
        if m is None:
            raise ValueError
        first_only = int(m.group(1))
        stats[day] = Stats(both, first_only)
    return stats


def _get_aocd_leaderboard(year: int) -> dict[int, LeaderBoard]:
    def as_str(t: timedelta | int) -> str:
        assert type(t) is timedelta
        if t.days > 0:
            return ">24h"
        hours = t.seconds // 3600
        minutes = (t.seconds - hours * 3600) // 60
        seconds = t.seconds - hours * 3600 - minutes * 60
        return ":".join(
            str(_).rjust(2, "0") for _ in [hours, minutes, seconds]
        )

    def as_int(n: timedelta | int) -> int:
        assert type(n) is int
        return int(n)

    return {
        k[1]: LeaderBoard(
            time_first=as_str(v["a"]["time"]),
            rank_first=as_int(v["a"]["rank"]),
            score_first=as_int(v["a"]["score"]),
            time_both=as_str(v["b"]["time"]) if "b" in v else None,
            rank_both=as_int(v["b"]["rank"]) if "b" in v else None,
            score_both=as_int(v["b"]["score"]) if "b" in v else None,
        )
        for k, v in get_user_stats(year).items()
    }


def _scrape_leaderboard(year: int) -> dict[int, LeaderBoard]:
    r = get_leaderboard_page(year)
    leaderboards = dict[int, LeaderBoard]()
    if r.find("<pre>") == -1:
        return leaderboards
    begin = r.index("<pre>")
    end = r.index("</pre>")
    to_parse = r[begin:end]
    start = 0
    idx = to_parse.find("</span>")
    while idx != -1:
        start = idx
        idx = to_parse.find("</span>", idx + 1)
    start += len("</span>")
    to_parse = to_parse[start:]
    lines = to_parse.splitlines()
    for line in lines:
        if len(line) == 0:
            continue
        splits = line.split()
        day = int(splits[0])
        if not calendar.is_ranked_year(year):
            time_first = html.unescape(splits[1]) if splits[1] != "-" else None
            rank_first = None
            score_first = None
            time_both = html.unescape(splits[2]) if splits[2] != "-" else None
            rank_both = None
            score_both = None
        else:
            time_first = html.unescape(splits[1]) if splits[1] != "-" else None
            rank_first = int(splits[2]) if splits[2] != "-" else None
            score_first = int(splits[3]) if splits[3] != "-" else None
            time_both = html.unescape(splits[4]) if splits[4] != "-" else None
            rank_both = int(splits[5]) if splits[5] != "-" else None
            score_both = int(splits[6]) if splits[6] != "-" else None
        leaderboards[day] = LeaderBoard(
            time_first,
            rank_first,
            score_first,
            time_both,
            rank_both,
            score_both,
        )
    return leaderboards


def get_leaderboard(year: int) -> dict[int, LeaderBoard]:
    if calendar.is_ranked_year(year):
        try:
            return _get_aocd_leaderboard(year)
        except AttributeError:
            pass
    return _scrape_leaderboard(year)


def get_summary() -> Summary:
    def time_first(stats: LeaderBoard) -> int:
        t = stats.time_first
        assert type(t) is str
        if t == ">24h":
            return 24 * 3600
        h, m, s = map(int, t.split(":"))
        return h * 3600 + m * 60 + s

    def time_both(stats: LeaderBoard) -> int:
        t = stats.time_both
        assert type(t) is str
        if t == ">24h":
            return 24 * 3600
        h, m, s = map(int, t.split(":"))
        return h * 3600 + m * 60 + s

    def rank_first(stats: LeaderBoard) -> int:
        assert stats.rank_first is not None
        return stats.rank_first

    def rank_both(stats: LeaderBoard) -> int:
        assert stats.rank_both is not None
        return stats.rank_both

    leaderboards = {
        year: get_leaderboard(year) for year in tqdm([2020, 2021, 2022, 2023])
    }
    for year, leaderboard in leaderboards.items():
        for day, stats in leaderboard.items():
            log.debug((year, day, stats))
    best_time_first = min(
        (
            (year, day, stats)
            for year, leaderboard in leaderboards.items()
            for day, stats in leaderboard.items()
        ),
        key=lambda x: time_first(x[2]),
    )
    best_time_both = min(
        (
            (year, day, stats)
            for year, leaderboard in leaderboards.items()
            for day, stats in leaderboard.items()
        ),
        key=lambda x: time_both(x[2]),
    )
    best_rank_first = min(
        (
            (year, day, stats)
            for year, leaderboard in leaderboards.items()
            for day, stats in leaderboard.items()
        ),
        key=lambda x: rank_first(x[2]),
    )
    best_rank_both = min(
        (
            (year, day, stats)
            for year, leaderboard in leaderboards.items()
            for day, stats in leaderboard.items()
        ),
        key=lambda x: rank_both(x[2]),
    )
    return Summary(
        (
            best_time_first[0],
            best_time_first[1],
            time_first(best_time_first[2]),
        ),
        (
            best_rank_first[0],
            best_rank_first[1],
            rank_first(best_rank_first[2]),
        ),
        (
            best_time_both[0],
            best_time_both[1],
            time_first(best_time_both[2]),
        ),
        (
            best_rank_both[0],
            best_rank_both[1],
            rank_both(best_rank_both[2]),
        ),
    )
