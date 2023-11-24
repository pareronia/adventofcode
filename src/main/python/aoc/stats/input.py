import html
import re
from datetime import timedelta

from . import LeaderBoard
from . import Stats
from .aocd import get_leaderboard_page
from .aocd import get_stats_page
from .aocd import get_user_stats


def get_stats(year: int) -> dict[int, Stats]:
    stats = dict[int, Stats]()
    r = get_stats_page(year)
    lines = re.findall(r'<a.*<span class="stats-both">.*</span>.*</a>', r.text)
    for line in lines:
        m = re.search(r'<a href="/[0-9]{4}/day/([0-9]+)">', line)
        day = int(m.group(1))
        m = re.search(r'<span class="stats-both">([ 0-9]+)</span>', line)
        both = int(m.group(1))
        m = re.search(r'<span class="stats-firstonly">([ 0-9]+)</span>', line)
        first_only = int(m.group(1))
        stats[day] = Stats(both, first_only)
    return stats


def _get_aocd_leaderboard(year: int) -> dict[int, LeaderBoard]:
    def as_str(t: timedelta) -> str:
        if t.days > 0:
            return ">24h"
        else:
            hours = t.seconds // 3600
            minutes = (t.seconds - hours * 3600) // 60
            seconds = t.seconds - hours * 3600 - minutes * 60
            return ":".join(
                str(_).rjust(2, "0") for _ in [hours, minutes, seconds]
            )

    return {
        k[1]: LeaderBoard(
            time_first=as_str(v["a"]["time"]),
            rank_first=v["a"]["rank"],
            score_first=v["a"]["score"],
            time_both=as_str(v["b"]["time"]) if "b" in v else None,
            rank_both=v["b"]["rank"] if "b" in v else None,
            score_both=v["b"]["score"] if "b" in v else None,
        )
        for k, v in get_user_stats(year).items()
    }


def _scrape_leaderboard(year: int) -> dict[int, LeaderBoard]:
    r = get_leaderboard_page(year)
    leaderboards = dict[int, LeaderBoard]()
    if r.text.find("<pre>") == -1:
        return leaderboards
    begin = r.text.index("<pre>")
    end = r.text.index("</pre>")
    to_parse = r.text[begin:end]
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
    try:
        return _get_aocd_leaderboard(year)
    except AttributeError:
        return _scrape_leaderboard(year)
