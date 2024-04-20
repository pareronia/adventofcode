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
        time = time.rjust(5)
        return time

    first_only_fmt = (
        "{day:3d} {title} :  "
        + "{time_first}  {rank_first:6d}"
        + " / {first:6d} = {pct_first:5.2f}"
        + "  |  "
        + "{time_both}  {rank_both:6d}"
        + " / {both:6d} = {pct_both:5.2f}"
        + "  |  "
        + "{stars}"
    )
    both_fmt = (
        "{day:3d} {title} :  "
        + "{time_first}  {rank_first:6d}"
        + " / {first:6d} = {pct_first:5.2f}"
        + "  |  "
        + "{time_both}  {rank_both}"
        + " / {both:6d} = {pct_both}"
        + "  |  "
        + "{stars}"
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
        assert rank_first is not None
        time_first = format_time(leaderboard[i].time_first)
        first = stats[i].first_only + stats[i].both
        pct_first = (rank_first - 1) / first * 100 // 0.01 / 100
        rank_both = leaderboard[i].rank_both
        if rank_both is not None:
            time_both = format_time(leaderboard[i].time_both)
            pct_both = (rank_both - 1) / stats[i].both * 100 // 0.01 / 100
            stars = "**"
            fmt = first_only_fmt
        else:
            time_both = "-:--".rjust(5)
            stars = "*"
            fmt = both_fmt
        output = fmt.format(
            day=i,
            title=title,
            time_first=time_first,
            rank_first=rank_first,
            first=first,
            pct_first=pct_first,
            time_both=time_both,
            rank_both=rank_both or "--".rjust(6),
            both=stats[i].both,
            pct_both=pct_both or "--:--",
            stars=stars,
        )
        lines.append(output)
    return lines
