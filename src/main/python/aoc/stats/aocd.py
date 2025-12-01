import datetime
import logging
from typing import cast

import aocd
import requests
from aocd.models import USER_AGENT
from cachier import cachier

from . import LEADERBOARD_URL
from . import STATS_URL
from . import AocdUserStats

log = logging.getLogger("aoc.stats")


def _get_url(url: str) -> requests.Response:
    user = aocd.models.default_user()
    cookies = {"session": user.token}
    return requests.get(url, cookies=cookies, headers=USER_AGENT, timeout=30)


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc]
def get_user_stats(year: int) -> AocdUserStats:
    log.debug("Getting aocd user stats")
    user = aocd.models.default_user()
    return cast("AocdUserStats", user.get_stats(year))


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc]
def get_leaderboard_page(year: int) -> requests.Response:
    log.debug("Getting leaderboard page")
    url = LEADERBOARD_URL.format(year=year)
    return _get_url(url)


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc]
def get_stats_page(year: int) -> requests.Response:
    log.debug("Getting stats page")
    url = STATS_URL.format(year=year)
    return _get_url(url)
