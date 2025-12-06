import datetime
import logging
from typing import cast

import aocd
import urllib3
from aocd.models import USER_AGENT
from cachier import cachier

from . import LEADERBOARD_URL
from . import STATS_URL
from . import AocdUserStats

log = logging.getLogger("aoc.stats")


def _get_url(url: str) -> str:
    user = aocd.models.default_user()
    http = urllib3.PoolManager()
    headers = USER_AGENT | {"Cookie": f"session={user.token}"}
    return http.request(
        "GET", url, headers=headers, redirect=True
    ).data.decode("utf-8")


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc,untyped-decorator]
def get_user_stats(year: int) -> AocdUserStats:
    log.debug("Getting aocd user stats")
    user = aocd.models.default_user()
    return cast("AocdUserStats", user.get_stats(year))


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc,untyped-decorator]
def get_leaderboard_page(year: int) -> str:
    log.debug("Getting leaderboard page")
    url = LEADERBOARD_URL.format(year=year)
    return _get_url(url)


@cachier(stale_after=datetime.timedelta(minutes=15))  # type:ignore[misc,untyped-decorator]
def get_stats_page(year: int) -> str:
    log.debug("Getting stats page")
    url = STATS_URL.format(year=year)
    return _get_url(url)
