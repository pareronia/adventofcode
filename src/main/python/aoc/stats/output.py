import aocd

from . import LeaderBoard
from . import Stats


def print_stats(
    year: int, stats: dict[int, Stats], leaderboard: dict[int, LeaderBoard]
) -> list[str]:
    def format_time(time: str | None) -> str:
        assert time is not None
        if time != ">24h":
            time = time[:-3]
        if time[0] == "0":
            time = time[1:]
        return time.rjust(5)

    fmt = (
        "{day:3d} {title} :  "
        "{time_first}  {rank_first}"
        " / {first:6d} = {pct_first}"
        "  |  "
        "{time_both}  {rank_both}"
        " / {both:6d} = {pct_both}"
        "  |  "
        "{stars}"
    )
    lines = []
    lines.append(
        " " * 53
        + " Part 1 ".center(30, "_")
        + " " * 5
        + " Part 2 ".center(30, "_")
    )
    lines.append("")
    for i in range(1, max(stats.keys()) + 1):
        puzzle = aocd.models.Puzzle(year=year, day=i)
        title = "-- " + puzzle.title + " --"
        title = title.ljust(45)
        if i not in leaderboard:
            continue
        rank_first = leaderboard[i].rank_first
        time_first = format_time(leaderboard[i].time_first)
        first = stats[i].first_only + stats[i].both
        if rank_first is not None:
            pct_first = (rank_first - 1) / first * 100 // 0.01 / 100
        else:
            pct_first = None
        rank_both = leaderboard[i].rank_both
        if leaderboard[i].time_both is not None:
            time_both = format_time(leaderboard[i].time_both)
            if rank_both is not None:
                pct_both = (rank_both - 1) / stats[i].both * 100 // 0.01 / 100
            else:
                pct_both = None
            stars = "**"
        else:
            time_both = "-:--".rjust(5)
            stars = "*"
        f_rank_first = (
            f"{rank_first:6d}" if rank_first is not None else "--".rjust(6)
        )
        f_rank_both = (
            f"{rank_both:6d}" if rank_both is not None else "--".rjust(6)
        )
        f_pct_first = (
            f"{pct_first:5.2f}" if pct_first is not None else "--".rjust(5)
        )
        f_pct_both = (
            f"{pct_both:5.2f}" if pct_both is not None else "--".rjust(5)
        )
        output = fmt.format(
            day=i,
            title=title,
            time_first=time_first,
            first=first,
            rank_first=f_rank_first,
            pct_first=f_pct_first,
            time_both=time_both,
            rank_both=f_rank_both,
            both=stats[i].both,
            pct_both=f_pct_both,
            stars=stars,
        )
        lines.append(output)
    return lines
