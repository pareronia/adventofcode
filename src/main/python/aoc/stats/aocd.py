from typing import cast

import aocd
import requests
from aocd.models import USER_AGENT

from . import LEADERBOARD_URL
from . import STATS_URL
from . import AocdUserStats


def _get_url(url: str) -> requests.Response:
    user = aocd.models.default_user()
    cookies = {"session": user.token}
    return requests.get(url, cookies=cookies, headers=USER_AGENT, timeout=30)


def get_user_stats(year: int) -> AocdUserStats:
    user = aocd.models.default_user()
    return cast(AocdUserStats, user.get_stats(year))


def get_leaderboard_page(year: int) -> requests.Response:
    url = LEADERBOARD_URL.format(year=year)
    return _get_url(url)


def get_stats_page(year: int) -> requests.Response:
    url = STATS_URL.format(year=year)
    return _get_url(url)
