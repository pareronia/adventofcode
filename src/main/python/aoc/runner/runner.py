"""
Copyright (c) 2016 wim glenn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
"""
import itertools
import logging
import os
import sys
import tempfile
import time
from argparse import ArgumentParser
from collections import OrderedDict
from contextlib import contextmanager
from datetime import datetime
from typing import Any
from typing import Callable
from typing import Generator

import pebble
from dateutil.tz import gettz

from . import Result
from .aocd import AocdHelper
from .aocd import Puzzle
from .bash import Bash
from .config import config
from .cpp import Cpp
from .java import Java
from .julia import Julia
from .listener import CLIListener
from .listener import JUnitXmlListener
from .listener import Listener
from .listener import Listeners
from .plugin import Plugin
from .py import Py
from .rust import Rust

DEFAULT_TIMEOUT = config.default_timeout
AOC_TZ = gettz("America/New_York")
log = logging.getLogger(__name__)
all_plugins = OrderedDict(
    {
        "py": Py(),
        "java": Java(),
        "bash": Bash(),
        "cpp": Cpp(),
        "julia": Julia(),
        "rust": Rust(),
    }
)


def main() -> None:
    aoc_now = datetime.now(tz=AOC_TZ)
    years = range(2015, aoc_now.year + int(aoc_now.month == 12))
    days = range(1, 26)
    users = AocdHelper.load_users()
    log_levels = "DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"
    parser = ArgumentParser(description="AoC runner")
    parser.add_argument(
        "-p", "--plugins", nargs="+", choices=all_plugins.keys()
    )
    parser.add_argument("-y", "--years", type=int, nargs="+", choices=years)
    parser.add_argument("-d", "--days", type=int, nargs="+", choices=days)
    parser.add_argument("-u", "--users", nargs="+", choices=users)
    parser.add_argument("-t", "--timeout", type=int, default=DEFAULT_TIMEOUT)
    parser.add_argument(
        "-s", "--no-submit", action="store_true", help="disable autosubmit"
    )
    parser.add_argument(
        "-m", "--hide-missing", action="store_true", help="hide missing"
    )
    parser.add_argument("--log-level", default="WARNING", choices=log_levels)
    args = parser.parse_args()

    logging.basicConfig(level=getattr(logging, args.log_level))
    plugins = OrderedDict(
        {k: all_plugins[k] for k in args.plugins or all_plugins}
    )
    log.debug(plugins)
    datasets = {k: users[k] for k in (args.users or users)}
    timeout = args.timeout
    hide_missing = args.hide_missing
    cli_listener = CLIListener(
        plugins.keys(), datasets.keys(), timeout, hide_missing
    )
    junitxml_listener = JUnitXmlListener()

    with use_plugins(plugins):
        rc = run_for(
            plugins=plugins,
            years=[_ for _ in args.years or years],
            days=[_ for _ in args.days or days],
            datasets=datasets,
            timeout=timeout,
            autosubmit=not args.no_submit,
            listener=Listeners([cli_listener, junitxml_listener]),
        )

    sys.exit(rc)


def run_with_timeout(
    callable: Callable[[int, int, str], tuple[Result, Result]],
    args: dict[str, Any],
    timeout: int,
    listener: Listener,
    dt: float = 0.005,
) -> tuple[Result, Result, float, str]:
    # TO_DO : multi-process over the different tokens
    pool = pebble.ProcessPool(max_workers=1)
    elapsed = 0
    with pool:
        t0 = time.time_ns()
        # future = pool.schedule(plugin.run, kwargs=kwargs, timeout=timeout)
        future = pool.schedule(callable, kwargs=args, timeout=timeout)
        while not future.done():
            listener.run_elapsed(elapsed)
            time.sleep(dt)
            elapsed = time.time_ns() - t0
        walltime = time.time() - (t0 / 1e9)
        try:
            result_a, result_b = future.result()
        except Exception as err:
            log.error(err)
            result_a = Result.ok("")
            result_b = Result.ok("")
            error = repr(err)
        else:
            error = ""
    listener.run_finished()
    return result_a, result_b, walltime, error


@contextmanager
def use_plugins(plugins: dict[str, Plugin]) -> Generator[None, None, None]:
    for p in plugins:
        plugins[p].start()
    try:
        yield
    finally:
        for p in plugins:
            plugins[p].stop()


@contextmanager
def scratch_file(
    name: str, year: int, day: int, input_data: str
) -> Generator[None, None, None]:
    prev = os.getcwd()
    scratch = tempfile.mkdtemp(prefix="{}-{:02d}-".format(year, day))
    os.chdir(scratch)
    assert not os.path.exists(name)
    try:
        with open(name, "w") as f:
            f.write(input_data)
        yield
    finally:
        os.unlink(name)
        os.chdir(prev)
        os.rmdir(scratch)


def run_for(
    plugins: dict[str, Plugin],
    years: list[int],
    days: list[int],
    datasets: dict[str, str],
    timeout: int,
    autosubmit: bool,
    listener: Listener,
) -> int:
    aoc_now = datetime.now(tz=AOC_TZ)
    it = itertools.product(years, days, plugins.items(), datasets)
    n_incorrect = 0
    for year, day, plugin, user_id in it:
        if year == aoc_now.year and day > aoc_now.day:
            continue
        token = datasets[user_id]
        puzzle = Puzzle.create(token, year, day, autosubmit)
        listener.puzzle_started(year, day, puzzle.title, plugin[0], user_id)
        walltime = 0.0
        input_data = puzzle.input_data
        if input_data is None:
            result_a, result_b, walltime, error = (
                Result.missing(),
                Result.missing(),
                0,
                None,
            )
        else:
            with scratch_file(config.scratch_file, year, day, input_data):
                result_a, result_b, walltime, error = run_with_timeout(
                    callable=plugin[1].run,
                    args={"year": year, "day": day, "data": input_data},
                    timeout=timeout,
                    listener=listener,
                )
        time = (0 if result_a.duration is None else result_a.duration) + (
            0 if result_b.duration is None else result_b.duration
        )
        if error:
            assert result_a.answer == result_b.answer == ""
            n_incorrect += 1
            listener.puzzle_finished_with_error(time, walltime, error)
        else:
            for result, part in zip((result_a, result_b), "ab"):
                if day == 25 and part == "b":
                    # there's no part b on christmas day, skip
                    continue
                if result.is_missing:
                    listener.part_missing(time, part)
                elif result.is_skipped:
                    listener.part_skipped(time, part)
                else:
                    expected = puzzle.get_expected(part, result.answer)
                    correct = (
                        expected is not None and str(expected) == result.answer
                    )
                    listener.part_finished(
                        time, part, result.answer, expected, correct
                    )
                    if not correct:
                        n_incorrect += 1
        listener.puzzle_finished(time, walltime)
    listener.stop()
    return n_incorrect
