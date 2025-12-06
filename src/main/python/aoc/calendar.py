from datetime import UTC
from datetime import datetime
from zoneinfo import ZoneInfo

AOC_TZ = ZoneInfo("America/New_York")


def get_now_aoc() -> datetime:
    return datetime.now(tz=AOC_TZ)


def get_now_utc() -> datetime:
    return datetime.now(tz=UTC)


def valid_years() -> range:
    aoc_now = get_now_aoc()
    return range(2015, aoc_now.year + int(aoc_now.month == 12))


def is_valid_year(year: int) -> bool:
    return year in valid_years()


def get_days(year: int) -> int:
    if not is_valid_year(year):
        raise AssertionError
    return 25 if year < 2025 else 12


def is_ranked_year(year: int) -> bool:
    if not is_valid_year(year):
        raise AssertionError
    return year < 2025


def is_valid_day(year: int, day: int) -> bool:
    valid_day = is_valid_year(year) and day in range(1, get_days(year) + 1)
    aoc_now = get_now_aoc()
    if year == aoc_now.year:
        return valid_day and day <= aoc_now.day
    return valid_day
