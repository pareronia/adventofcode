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
import importlib
import subprocess  # nosec
import pebble
import itertools
import time
import sys
import os
import tempfile
import logging
import json
from argparse import ArgumentParser
from datetime import datetime
from termcolor import colored
from dateutil.tz import gettz
from aocd.models import AOCD_DIR
from aocd.models import Puzzle
from aocd.models import default_user


DEFAULT_TIMEOUT = 60
AOC_TZ = gettz("America/New_York")
log = logging.getLogger(__name__)
root = os.getcwd()


def main():
    aoc_now = datetime.now(tz=AOC_TZ)
    years = range(2015, aoc_now.year + int(aoc_now.month == 12))
    days = range(1, 26)
    path = os.path.join(AOCD_DIR, "tokens.json")
    try:
        with open(path) as f:
            users = json.load(f)
    except IOError:
        users = {"default": default_user().token}
    log_levels = "DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"
    parser = ArgumentParser(description="AoC runner")
    parser.add_argument("-y", "--years", type=int, nargs="+", choices=years)
    parser.add_argument("-d", "--days", type=int, nargs="+", choices=days)
    parser.add_argument("-u", "--users", nargs="+", choices=users)
    parser.add_argument("-t", "--timeout", type=int, default=DEFAULT_TIMEOUT)
    parser.add_argument("--log-level", default="WARNING", choices=log_levels)
    args = parser.parse_args()
    if not users:
        print(
            "There are no datasets available to use.\n"
            "Either export your AOC_SESSION or put some auth "
            "tokens into {}".format(path),
            file=sys.stderr,
        )
        sys.exit(1)
    logging.basicConfig(level=getattr(logging, args.log_level))
    rc = run_for(
        funcs=[py, java],
        years=args.years or years,
        days=args.days or days,
        datasets={k: users[k] for k in (args.users or users)},
        timeout=args.timeout,
    )
    sys.exit(rc)


def run_with_timeout(func, timeout, progress, dt=0.005, **kwargs):
    # TO_DO : multi-process over the different tokens
    spinner = itertools.cycle(r"\|/-")
    pool = pebble.ProcessPool(max_workers=1)
    line = elapsed = format_time(0)
    with pool:
        t0 = time.time()
        future = pool.schedule(func, kwargs=kwargs, timeout=timeout)
        while not future.done():
            if progress is not None:
                line = "\r" + elapsed + "   " + progress \
                        + "   " + next(spinner)
                sys.stderr.write(line)
                sys.stderr.flush()
            time.sleep(dt)
            elapsed = format_time(time.time() - t0, timeout)
        walltime = time.time() - t0
        try:
            a, b = future.result()
        except Exception as err:
            a = b = ""
            error = repr(err)[:50]
        else:
            error = ""
            # longest correct answer seen so far has been 32 chars
            a = str(a)[:50]
            b = str(b)[:50]
    if progress is not None:
        sys.stderr.write("\r" + " " * len(line) + "\r")
        sys.stderr.flush()
    return a, b, walltime, error


def format_time(t, timeout=DEFAULT_TIMEOUT):
    if t < timeout / 4:
        color = "green"
    elif t < timeout / 2:
        color = "yellow"
    else:
        color = "red"
    runtime = colored("{: 7.3f}s".format(t), color)
    return runtime


def run_one(year, day, input_data, func,
            timeout=DEFAULT_TIMEOUT, progress=None):
    prev = os.getcwd()
    scratch = tempfile.mkdtemp(prefix="{}-{:02d}-".format(year, day))
    os.chdir(scratch)
    assert not os.path.exists("input.txt")
    try:
        with open("input.txt", "w") as f:
            f.write(input_data)
        a, b, walltime, error = run_with_timeout(
            func=func,
            timeout=timeout,
            year=year,
            day=day,
            data=input_data,
            progress=progress,
        )
    finally:
        os.unlink("input.txt")
        os.chdir(prev)
        os.rmdir(scratch)
    return a, b, walltime, error


def run_for(funcs, years, days, datasets, timeout=DEFAULT_TIMEOUT):
    aoc_now = datetime.now(tz=AOC_TZ)
    it = itertools.product(years, days, funcs, datasets)
    userpad = 3
    datasetpad = 8
    n_incorrect = 0
    if datasets:
        datasetpad = len(max(datasets, key=len))
    for year, day, func, dataset in it:
        if year == aoc_now.year and day > aoc_now.day:
            continue
        token = datasets[dataset]
        os.environ["AOC_SESSION"] = token
        puzzle = Puzzle(year=year, day=day)
        title = puzzle.title
        progress = "{}/{:<2d} - {:<40}   {:>%d}/{:<%d}"
        progress %= (userpad, datasetpad)
        progress = progress.format(year, day, title, func.__name__, dataset)
        a, b, walltime, error = run_one(
            year=year,
            day=day,
            input_data=puzzle.input_data,
            func=func,
            timeout=timeout,
            progress=progress,
        )
        runtime = format_time(walltime, timeout)
        line = "   ".join([runtime, progress])
        if error:
            assert a == b == ""
            icon = colored("❌", "red")
            n_incorrect += 1
            line += "   {icon} {error}".format(icon=icon, error=error)
        else:
            result_template = "   {icon} part {part}: {answer}"
            for answer, part in zip((a, b), "ab"):
                if day == 25 and part == "b":
                    # there's no part b on christmas day, skip
                    continue
                expected = None
                try:
                    expected = getattr(puzzle, "answer_" + part)
                except AttributeError:
                    pass
                correct = expected is not None and str(expected) == answer
                icon = colored("✅", "green") if correct \
                    else colored("❌", "red")
                correction = ""
                if not correct:
                    n_incorrect += 1
                    if expected is None:
                        icon = colored("?", "magenta")
                        correction = "(correct answer unknown)"
                    else:
                        correction = "(expected: {})".format(expected)
                answer = "{} {}".format(answer, correction)
                if part == "a":
                    answer = answer.ljust(30)
                line += result_template.format(icon=icon, part=part,
                                               answer=answer)
        print(line)
    return n_incorrect


def py(year: int, day: int, data: str):
    day_mod_name = "AoC" + str(year) + "_" + str(day).rjust(2, '0')
    try:
        day_mod = importlib.import_module(day_mod_name)
    except ModuleNotFoundError:
        return None, None
    inputs = data.splitlines()
    return day_mod.part_1(inputs), day_mod.part_2(inputs)


def java(year: int, day: int, data: str):
    global root
    completed = subprocess.run(  # nosec
        ["java",
         "-cp",
         os.environ['CLASSPATH'] + ":" + root + "/target/classes",
         "com.github.pareronia.aocd.Runner",
         str(year), str(day), data],
        text=True,
        capture_output=True,
    )
    results = completed.stdout
    if results:
        return tuple(results.splitlines())
    else:
        return None, None


if __name__ == '__main__':
    main()
