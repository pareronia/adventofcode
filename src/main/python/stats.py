import sys
import html
from typing import NamedTuple
import requests
import re
import aocd


STATS_URL = "https://adventofcode.com/{year}/stats"
LEADERBOARD_URL = "https://adventofcode.com/{year}/leaderboard/self"


class Stats(NamedTuple):
    both: int
    first_only: int


def get_stats(year: int):
    stats = dict[int, Stats]()
    r = requests.get(STATS_URL.format(year=year))
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


class LeaderBoard(NamedTuple):
    time_first: str
    rank_first: int
    score_first: int
    time_both: str
    rank_both: int
    score_both: int


def get_session_cookie() -> str:
    u = aocd.models.default_user()
    return u.token


def get_leaderboard(year: int):
    cookies = dict(session=get_session_cookie())
    r = requests.get(LEADERBOARD_URL.format(year=year),
                     cookies=cookies)
    leaderboards = dict[int, LeaderBoard]()
    if r.text.find('<pre>') == -1:
        return leaderboards
    to_parse = r.text[r.text.index('<pre>'):r.text.index('</pre>')]
    start = 0
    idx = to_parse.find("</span>")
    while idx != -1:
        start = idx
        idx = to_parse.find("</span>", idx+1)
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
        leaderboards[day] = LeaderBoard(time_first, rank_first,
                                        score_first, time_both,
                                        rank_both, score_both)
    return leaderboards


def format_time(time: str) -> str:
    if time != ">24h":
        time = time[:-3]
    if time[0] == "0":
        time = time[1:]
    time = time.rjust(5)
    return time


def get_score(year: int):
    stats = get_stats(year)
    leaderboard = get_leaderboard(year)
    print(" " * 53 + " Part 1 ".center(30, '_')
          + " " * 5 + " Part 2 ".center(30, '_'))
    print("")
    for i in range(1, max(stats.keys())+1):
        puzzle = aocd.models.Puzzle(year=year, day=i)
        title = "-- " + puzzle.title + " --"
        title = title.ljust(45)
        if i not in leaderboard:
            continue
        rank_first = leaderboard[i].rank_first
        time_first = format_time(leaderboard[i].time_first)
        first = stats[i].first_only + stats[i].both
        pct_first = (leaderboard[i].rank_first-1) / first * 100 // 0.01 / 100
        if leaderboard[i].rank_both is not None:
            rank_both = leaderboard[i].rank_both
            time_both = format_time(leaderboard[i].time_both)
            pct_both = (leaderboard[i].rank_both-1) / stats[i].both \
                * 100 // 0.01 / 100
            stars = "**"
            f = "{day:3d} {title} :  " \
                + "{time_first}  {rank_first:6d}"\
                + " / {first:6d} = {pct_first:2.2f}"\
                + "  |  "\
                + "{time_both}  {rank_both:6d}"\
                + " / {both:6d} = {pct_both:2.2f}"\
                + "  |  "\
                + "{stars}"
        else:
            rank_both = "--".rjust(6)
            time_both = "-:--".rjust(5)
            pct_both = "--.--"
            stars = "*"
            f = "{day:3d} {title} :  " \
                + "{time_first}  {rank_first:6d}"\
                + " / {first:6d} = {pct_first:2.2f}"\
                + "  |  "\
                + "{time_both}  {rank_both}"\
                + " / {both:6d} = {pct_both}"\
                + "  |  "\
                + "{stars}"
        output = f.format(
            day=i,
            title=title,
            time_first=time_first,
            rank_first=rank_first,
            first=first,
            pct_first=pct_first,
            time_both=time_both,
            rank_both=rank_both,
            both=stats[i].both,
            pct_both=pct_both,
            stars=stars
            )
        print(output)


def get_score_aocd(year: int):
    u = aocd.models.default_user()
    try:
        stats = u.get_stats(year)
        print(stats)
    except AttributeError:
        pass


def main():
    get_score_aocd(int(sys.argv[1]))
    get_score(int(sys.argv[1]))


if __name__ == '__main__':
    main()
