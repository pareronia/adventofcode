import aocd
import requests
from aocd.models import USER_AGENT

from . import LEADERBOARD_URL
from . import STATS_URL


def _get_url(url: str):
    user = aocd.models.default_user()
    cookies = {"session": user.token}
    return requests.get(url, cookies=cookies, headers=USER_AGENT)


def get_user_stats(year: int) -> dict:
    user = aocd.models.default_user()
    return user.get_stats(year)


def get_leaderboard_page(year: int):
    url = LEADERBOARD_URL.format(year=year)
    return _get_url(url)


def get_stats_page(year: int):
    url = STATS_URL.format(year=year)
    return _get_url(url)
