"""Copyright (c) 2016 wim glenn.

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
from collections.abc import Callable
from collections.abc import Generator
from contextlib import contextmanager
from pathlib import Path
from typing import Any

import pebble

from aoc import calendar

from . import Result
from .aocd import AocdHelper
from .aocd import Puzzle
from .bash import Bash
from .config import config
from .cpp import Cpp
from .java import Java
from .julia import Julia
from .listener import BenchmarkListener
from .listener import CLIListener
from .listener import JUnitXmlListener
from .listener import Listener
from .listener import Listeners
from .plugin import Plugin
from .py import Py
from .rust import Rust

DEFAULT_TIMEOUT = config.default_timeout
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
    years = calendar.valid_years()
    days = range(1, 26)
    users = AocdHelper.load_users()
    parser = ArgumentParser(description="AoC runner")
    parser.add_argument(
        "-p",
        "--plugins",
        nargs="+",
        choices=all_plugins.keys(),
        default=all_plugins.keys(),
        help=(
            "List of plugins (solvers) to evaluate. "
            "Runs against all available plugins by default."
        ),
    )
    parser.add_argument(
        "-y",
        "--years",
        type=int,
        nargs="+",
        choices=years,
        default=years,
        help="AoC years to run. Runs all available by default.",
    )
    parser.add_argument(
        "-d",
        "--days",
        type=int,
        nargs="+",
        choices=days,
        default=days,
        help="AoC days to run. Runs all days by default.",
    )
    parser.add_argument(
        "-u",
        "--users",
        nargs="+",
        choices=users,
        default=users,
        help="Users to run each plugin with.",
    )
    parser.add_argument(
        "-t",
        "--timeout",
        type=int,
        default=DEFAULT_TIMEOUT,
        help=(
            "Kill a solver if it exceeds this timeout, in seconds "
            "(default: %(default)s)."
        ),
    )
    parser.add_argument(
        "-s",
        "--no-submit",
        action="store_false",
        dest="autosubmit",
        help=(
            "Disable autosubmit. "
            "By default, the runner will submit answers if necessary."
        ),
    )
    parser.add_argument(
        "-m",
        "--hide-missing",
        action="store_true",
        help="Hide missing solvers.",
    )
    parser.add_argument(
        "-v",
        "--verbose",
        action="count",
        help=(
            "Increased logging (-v INFO, -vv DEBUG). "
            "Default level is logging.WARNING."
        ),
    )
    subparsers = parser.add_subparsers()
    parser_bench = subparsers.add_parser("bench")
    parser_bench.add_argument(
        "-s",
        "--slowest",
        type=int,
        default=5,
    )
    args = parser.parse_args()

    if args.verbose is None:
        log_level = logging.WARNING
    elif args.verbose == 1:
        log_level = logging.INFO
    else:
        log_level = logging.DEBUG
    logging.basicConfig(level=log_level)
    plugins = OrderedDict({k: all_plugins[k] for k in args.plugins})
    log.debug(plugins)
    datasets = {k: users[k] for k in args.users}
    cli_listener = CLIListener(
        args.plugins, args.users, args.timeout, args.hide_missing
    )
    junitxml_listener = JUnitXmlListener()
    listeners = [cli_listener, junitxml_listener]
    if "slowest" in args:
        bench = BenchmarkListener()
        listeners.append(bench)

    with use_plugins(plugins):
        rc = run_for(
            plugins=plugins,
            years=args.years,
            days=args.days,
            datasets=datasets,
            timeout=args.timeout,
            autosubmit=args.autosubmit,
            listener=Listeners(listeners),
        )

    if "slowest" in args:
        print()
        by_time = list(bench.get_by_time())
        by_time.reverse()
        for i in range(args.slowest):
            print(
                f"{by_time[i].year}/{by_time[i].day:02}/{by_time[i].part}"
                f"/{by_time[i].plugin}/{by_time[i].user_id}: "
                f"{by_time[i].time / 1e9:6.3f}s"
            )

    sys.exit(rc)


def run_with_timeout(
    callable_: Callable[[int, int, str], tuple[Result, Result]],
    args: dict[str, Any],
    timeout: int,
    listener: Listener,
    dt: float = 0.005,
) -> tuple[Result, Result, float, str]:
    elapsed = 0
    t0 = time.time_ns()
    future = pebble.concurrent.process(daemon=False, timeout=timeout)(
        callable_
    )(**args)
    while not future.done():
        listener.run_elapsed(elapsed)
        time.sleep(dt)
        elapsed = time.time_ns() - t0
    walltime = time.time() - (t0 / 1e9)
    try:
        result_a, result_b = future.result()
    except Exception as err:
        log.exception("Exception in run_with_timeout")
        result_a = Result.ok("")
        result_b = Result.ok("")
        error = repr(err)
    else:
        error = ""
    listener.run_finished()
    return result_a, result_b, walltime, error


@contextmanager
def use_plugins(plugins: dict[str, Plugin]) -> Generator[None]:
    for plugin in plugins.values():
        plugin.start()
    try:
        yield
    finally:
        for plugin in plugins.values():
            plugin.stop()


@contextmanager
def scratch_file(
    name: str, year: int, day: int, input_data: str
) -> Generator[None]:
    prev = Path.cwd()
    scratch = Path(tempfile.mkdtemp(prefix=f"{year}-{day:02d}-"))
    os.chdir(scratch)
    input_path = Path(name)
    assert not input_path.exists()
    try:
        input_path.write_text(input_data, encoding="utf-8")
        yield
    finally:
        input_path.unlink(missing_ok=True)
        os.chdir(prev)
        try:
            scratch.rmdir()
        except Exception as err:  # noqa:BLE001
            log.warning(
                "failed to remove scratch %s (%s: %s)", scratch, type(err), err
            )


def run_for(
    plugins: dict[str, Plugin],
    years: list[int],
    days: list[int],
    datasets: dict[str, str],
    timeout: int,
    autosubmit: bool,
    listener: Listener,
) -> int:
    it = itertools.product(years, days, plugins.items(), datasets)
    n_incorrect = 0
    for year, day, plugin, user_id in it:
        if not calendar.is_valid_day(year, day):
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
                    callable_=plugin[1].run,
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
            for result, part in zip((result_a, result_b), "ab", strict=True):
                if day == calendar.get_days(year) and part == "b":
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
                        result.duration, part, result.answer, expected, correct
                    )
                    if not correct:
                        n_incorrect += 1
        listener.puzzle_finished(time, walltime)
    listener.stop()
    return n_incorrect
