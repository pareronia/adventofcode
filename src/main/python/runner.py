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
import atexit
import importlib
import subprocess  # nosec
import socket
import pebble
import itertools
import time
import sys
import os
import tempfile
import logging
import json
from argparse import ArgumentParser
from collections import OrderedDict
from datetime import datetime
from termcolor import colored
from dateutil.tz import gettz
from aocd.exceptions import AocdError
from aocd.models import AOCD_CONFIG_DIR
from aocd.models import Puzzle
from aocd.models import default_user


DEFAULT_TIMEOUT = 60
AOC_TZ = gettz("America/New_York")
log = logging.getLogger(__name__)
root = os.getcwd()


def main():
    aoc_now = datetime.now(tz=AOC_TZ)
    plugins = OrderedDict([(ep.__name__, ep) for ep in all_entry_points])
    years = range(2015, aoc_now.year + int(aoc_now.month == 12))
    days = range(1, 26)
    path = os.path.join(AOCD_CONFIG_DIR, "tokens.json")
    try:
        with open(path) as f:
            users = json.load(f)
    except IOError:
        users = {"default": default_user().token}
    log_levels = "DEBUG", "INFO", "WARNING", "ERROR", "CRITICAL"
    parser = ArgumentParser(description="AoC runner")
    parser.add_argument("-p", "--plugins", nargs="+", choices=plugins)
    parser.add_argument("-y", "--years", type=int, nargs="+", choices=years)
    parser.add_argument("-d", "--days", type=int, nargs="+", choices=days)
    parser.add_argument("-u", "--users", nargs="+", choices=users)
    parser.add_argument("-t", "--timeout", type=int, default=DEFAULT_TIMEOUT)
    parser.add_argument("-s", "--no-submit", action="store_true",
                        help="disable autosubmit")
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
        plugins=args.plugins or list(plugins),
        years=args.years or years,
        days=args.days or days,
        datasets={k: users[k] for k in (args.users or users)},
        timeout=args.timeout,
        autosubmit=not args.no_submit,
    )
    sys.exit(rc)


def run_with_timeout(entry_point, timeout, progress, dt=0.005, **kwargs):
    # TO_DO : multi-process over the different tokens
    spinner = itertools.cycle(r"\|/-")
    pool = pebble.ProcessPool(max_workers=1)
    line = elapsed = format_time(0)
    with pool:
        t0 = time.time()
        future = pool.schedule(entry_point, kwargs=kwargs, timeout=timeout)
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


def run_one(year, day, input_data, entry_point,
            timeout=DEFAULT_TIMEOUT, progress=None):
    prev = os.getcwd()
    scratch = tempfile.mkdtemp(prefix="{}-{:02d}-".format(year, day))
    os.chdir(scratch)
    assert not os.path.exists("input.txt")
    try:
        with open("input.txt", "w") as f:
            f.write(input_data)
        a, b, walltime, error = run_with_timeout(
            entry_point=entry_point,
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


def run_for(plugins, years, days, datasets, timeout=DEFAULT_TIMEOUT,
            autosubmit=True):
    aoc_now = datetime.now(tz=AOC_TZ)
    entry_points = {ep.__name__: ep
                    for ep in all_entry_points
                    if ep.__name__ in plugins}
    it = itertools.product(years, days, plugins, datasets)
    userpad = 3
    datasetpad = 8
    n_incorrect = 0
    if entry_points:
        userpad = len(max(entry_points, key=len))
    if datasets:
        datasetpad = len(max(datasets, key=len))
    for year, day, plugin, dataset in it:
        if year == aoc_now.year and day > aoc_now.day:
            continue
        token = datasets[dataset]
        entry_point = entry_points[plugin]
        os.environ["AOC_SESSION"] = token
        puzzle = Puzzle(year=year, day=day)
        title = puzzle.title
        progress = "{}/{:<2d} - {:<40}   {:>%d}/{:<%d}"
        progress %= (userpad, datasetpad)
        progress = progress.format(year, day, title, plugin, dataset)
        a, b, walltime, error = run_one(
            year=year,
            day=day,
            input_data=puzzle.input_data,
            entry_point=entry_point,
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
                    post = part == "a" or (part == "b" and puzzle.answered_a)
                    if autosubmit and post:
                        try:
                            puzzle._submit(answer, part,
                                           reopen=False, quiet=True)
                        except AocdError as err:
                            log.warning("error submitting - %s", err)
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


def _java(year: int, day: int, data: str):
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


def java(year: int, day: int, data: str):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(('localhost', 5555))
        s.send(f'{year}\r\n'.encode('UTF-8'))
        s.send(f'{day}\r\n'.encode('UTF-8'))
        s.send(f'{data}\r\n'.encode('UTF-8'))
        s.send(b'END\r\n')
        data = s.recv(1024)
    results = data.decode('UTF-8').rstrip().splitlines()
    log.info(f"Results: {results}")
    if results:
        return tuple(results)
    else:
        return None, None


def start_java():
    completed = subprocess.Popen(  # nosec
        ["java",
         "-cp",
         os.environ['CLASSPATH'] + ":" + root + "/target/classes",
         "com.github.pareronia.aocd.RunServer",
         ],
    )
    if not completed.pid:
        raise RuntimeError("Could not start Java run server")
    time.sleep(0.5)
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(('localhost', 5555))
        s.send(('HELLO' + os.linesep).encode('UTF-8'))
        data = s.recv(1024)
    results = data.decode('UTF-8').rstrip().splitlines()
    if results != ["HELLO"]:
        raise RuntimeError("No response from Java run server")


@atexit.register
def stop_java():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect(('localhost', 5555))
        s.send(b'STOP')


if __name__ == '__main__':
    start_java()
    all_entry_points = [py, java]
    main()
