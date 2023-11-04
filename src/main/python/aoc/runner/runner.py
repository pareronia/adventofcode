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
from datetime import datetime
from typing import Any

import pebble
from dateutil.tz import gettz
from termcolor import colored

from . import Result
from .aocd import AocdHelper
from .aocd import Puzzle
from .bash import Bash
from .config import config
from .cpp import Cpp
from .java import Java
from .julia import Julia
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
    # if not users:
    #     print(
    #         "There are no datasets available to use.\n"
    #         "Either export your AOC_SESSION or put some auth "
    #         "tokens into {}".format(AocdHelper._tokens_path()),
    #         file=sys.stderr,
    #     )
    #     sys.exit(1)
    logging.basicConfig(level=getattr(logging, args.log_level))
    plugins = OrderedDict(
        {k: all_plugins[k] for k in args.plugins or all_plugins}
    )
    for p in plugins:
        plugins[p].start()
    try:
        rc = run_for(
            plugins=plugins,
            years=args.years or years,
            days=args.days or days,
            datasets={k: users[k] for k in (args.users or users)},
            timeout=args.timeout,
            autosubmit=not args.no_submit,
            hide_missing=args.hide_missing,
        )
    finally:
        for p in plugins:
            plugins[p].stop()
    sys.exit(rc)


def run_with_timeout(  # type:ignore[no-untyped-def]
    plugin: tuple[str, Plugin],
    timeout: int,
    progress: str | None,
    dt: float = 0.005,
    **kwargs,
) -> tuple[Result, Result, float, str]:
    # TO_DO : multi-process over the different tokens
    spinner = itertools.cycle(r"\|/-")
    pool = pebble.ProcessPool(max_workers=1)
    line = elapsed = format_time(0)
    with pool:
        t0 = time.time()
        future = pool.schedule(plugin[1].run, kwargs=kwargs, timeout=timeout)
        while not future.done():
            if progress is not None:
                line = (
                    "\r" + elapsed + "   " + progress + "   " + next(spinner)
                )
                sys.stderr.write(line)
                sys.stderr.flush()
            time.sleep(dt)
            elapsed = format_time(time.time() - t0, timeout)
        walltime = time.time() - t0
        try:
            result_a, result_b = future.result()
        except Exception as err:
            result_a = Result.ok("")
            result_b = Result.ok("")
            error = repr(err)
        else:
            error = ""
    if progress is not None:
        sys.stderr.write("\r" + " " * len(line) + "\r")
        sys.stderr.flush()
    return result_a, result_b, walltime, error


def format_time(t: float, timeout: float = DEFAULT_TIMEOUT) -> str:
    if t < timeout / 4:
        color = "green"
    elif t < timeout / 2:
        color = "yellow"
    else:
        color = "red"
    if t < 0.001:
        runtime = colored("{: 7.3f}ms".format(t * 1000), color)
    else:
        runtime = colored("{: 8.4f}s".format(t), color)
    return runtime


def run_one(
    year: int,
    day: int,
    input_data: str,
    plugin: tuple[str, Plugin],
    timeout: int = DEFAULT_TIMEOUT,
    progress: str | None = None,
) -> tuple[Result, Result, float, str]:
    prev = os.getcwd()
    scratch = tempfile.mkdtemp(prefix="{}-{:02d}-".format(year, day))
    os.chdir(scratch)
    assert not os.path.exists(config.scratch_file)
    try:
        with open(config.scratch_file, "w") as f:
            f.write(input_data)
        result_a, result_b, walltime, error = run_with_timeout(
            plugin=plugin,
            timeout=timeout,
            year=year,
            day=day,
            data=input_data,
            progress=progress,
        )
    finally:
        os.unlink(config.scratch_file)
        os.chdir(prev)
        os.rmdir(scratch)
    return result_a, result_b, walltime, error


def run_for(
    plugins: OrderedDict[str, Plugin],
    years: Any,
    days: Any,
    datasets: dict[str, str],
    timeout: int = DEFAULT_TIMEOUT,
    autosubmit: bool = True,
    hide_missing: bool = False,
) -> int:
    def _get_icon_and_answer(
        result: Result | None,
        correct: bool | None,
        expected: str | None,
        error: str | None,
    ) -> tuple[str, str]:
        # longest correct answer seen so far has been 32 chars
        cutoff = 50
        if error:
            icon = colored("❌", "red")
            answer = error[:cutoff]
        elif result and result.is_missing:
            icon = "⭕"
            answer = "- missing -"
        elif result and result.is_skipped:
            icon = "⌚"
            answer = "- skipped -"
        elif result and result.answer and correct:
            icon = colored("✅", "green")
            answer = f"{result.answer[:cutoff]}"
        elif result and result.answer and not correct:
            if expected is None:
                icon = colored("?", "magenta")
                correction = "(correct answer unknown)"
            else:
                icon = colored("❌", "red")
                correction = f"(expected: {expected})"
            answer = f"{result.answer[:cutoff]} {correction}"
        else:
            raise ValueError(
                f"Invalid state: {result=} {correct=} {expected=} {error=}"
            )
        return icon, answer

    aoc_now = datetime.now(tz=AOC_TZ)
    log.debug(plugins)
    it = itertools.product(years, days, plugins.items(), datasets)
    userpad = 3
    datasetpad = 8
    n_incorrect = 0
    if plugins:
        userpad = len(max(plugins.keys(), key=len))
    if datasets:
        datasetpad = len(max(datasets, key=len))
    total_time = 0.0
    total_walltime = 0.0
    for year, day, plugin, dataset in it:
        if year == aoc_now.year and day > aoc_now.day:
            continue
        token = datasets[dataset]
        puzzle = Puzzle.create(
            year=year, day=day, token=token, autosubmit=autosubmit
        )
        title = puzzle.title
        progress = "{}/{:<2d} - {:<39}   {:>%d}/{:<%d}"
        progress %= (userpad, datasetpad)
        progress = progress.format(year, day, title, plugin[0], dataset)
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
            result_a, result_b, walltime, error = run_one(
                year=year,
                day=day,
                input_data=input_data,
                plugin=plugin,
                timeout=timeout,
                progress=progress,
            )
        time = (
            0 if result_a.duration is None else result_a.duration / 1e9
        ) + (0 if result_b.duration is None else result_b.duration / 1e9)
        runtime = format_time(time, timeout)
        total_time += time
        total_walltime += walltime
        line = "   ".join([runtime, progress])
        if result_a.is_missing and hide_missing:
            continue
        if error:
            assert result_a.answer == result_b.answer == ""
            n_incorrect += 1
            icon, answer = _get_icon_and_answer(None, None, None, error)
            line += f"   {icon} {answer}"
        else:
            for result, part in zip((result_a, result_b), "ab"):
                if day == 25 and part == "b":
                    # there's no part b on christmas day, skip
                    continue
                if result.is_missing or result.is_skipped:
                    icon, answer = _get_icon_and_answer(
                        result, None, None, None
                    )
                else:
                    expected = puzzle.get_expected(part, result.answer)
                    correct = (
                        expected is not None and str(expected) == result.answer
                    )
                    icon, answer = _get_icon_and_answer(
                        result, correct, expected, None
                    )
                    if not correct:
                        n_incorrect += 1
                if part == "a":
                    answer = answer.ljust(30)
                line += f"   {icon} part {part}: {answer}"
        print(line)
    print()
    print(
        "Total run time: {:8.4f}s\nTook: {:8.4f}s".format(
            total_time, total_walltime
        )
    )
    return n_incorrect


if __name__ == "__main__":
    main()
