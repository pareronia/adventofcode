import itertools
import socket
import sys
from abc import ABC
from abc import abstractmethod
from collections.abc import Iterable
from collections.abc import Iterator
from typing import NamedTuple
from typing import cast

from aoc import calendar
from aoc.termcolor import colored
from junitparser import Attr
from junitparser import Error
from junitparser import Failure
from junitparser import IntAttr
from junitparser import JUnitXml
from junitparser import Skipped
from junitparser import TestCase
from junitparser import TestSuite

from .config import config


class Listener(ABC):
    @abstractmethod
    def puzzle_started(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        pass

    @abstractmethod
    def run_elapsed(self, amount: int) -> None:
        pass

    @abstractmethod
    def run_finished(self) -> None:
        pass

    @abstractmethod
    def puzzle_finished(self, time: int, walltime: float) -> None:
        pass

    @abstractmethod
    def puzzle_finished_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        pass

    @abstractmethod
    def part_missing(self, time: int, part: str) -> None:
        pass

    @abstractmethod
    def part_skipped(self, time: int, part: str) -> None:
        pass

    @abstractmethod
    def part_finished(
        self,
        time: int | None,
        part: str,
        answer: str | None,
        expected: str | None,
        correct: bool,
    ) -> None:
        pass

    @abstractmethod
    def stop(self) -> None:
        pass


class Listeners(Listener):
    def __init__(self, listeners: list[Listener]) -> None:
        self.listeners = listeners

    def puzzle_started(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        for listener in self.listeners:
            listener.puzzle_started(year, day, title, plugin, user_id)

    def run_elapsed(self, amount: int) -> None:
        for listener in self.listeners:
            listener.run_elapsed(amount)

    def run_finished(self) -> None:
        for listener in self.listeners:
            listener.run_finished()

    def puzzle_finished(self, time: int, walltime: float) -> None:
        for listener in self.listeners:
            listener.puzzle_finished(time, walltime)

    def puzzle_finished_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        for listener in self.listeners:
            listener.puzzle_finished_with_error(time, walltime, error)

    def part_missing(self, time: int, part: str) -> None:
        for listener in self.listeners:
            listener.part_missing(time, part)

    def part_skipped(self, time: int, part: str) -> None:
        for listener in self.listeners:
            listener.part_skipped(time, part)

    def part_finished(
        self,
        time: int | None,
        part: str,
        answer: str | None,
        expected: str | None,
        correct: bool,
    ) -> None:
        for listener in self.listeners:
            listener.part_finished(time, part, answer, expected, correct)

    def stop(self) -> None:
        for listener in self.listeners:
            listener.stop()


class CLIListener(Listener):
    # longest correct answer seen so far has been 57 chars
    cutoff = 60

    def __init__(
        self,
        plugins: Iterable[str],
        user_ids: Iterable[str],
        timeout: int,
        hide_missing: bool,
    ) -> None:
        self.timeout = timeout
        self.spinner = itertools.cycle(r"\|/-")
        self.plugin_pad = 3
        self.user_pad = 8
        if plugins:
            self.plugin_pad = len(max(plugins, key=len))
        if user_ids:
            self.user_pad = len(max(user_ids, key=len))
        self.is_missing = False
        self.hide_missing = hide_missing
        self.total_time = 0.0
        self.total_walltime = 0.0

    def puzzle_started(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        self.line = self._format_time(0, self.timeout)
        progress = "{}/{:<2d} - {:<39}   {:>%d}/{:<%d}"
        progress %= (self.plugin_pad, self.user_pad)
        self.progress = progress.format(year, day, title, plugin, user_id)
        self.times = list[int]()

    def run_elapsed(self, amount: int) -> None:
        if self.progress is not None:
            self.line = (
                "\r"
                + self._format_time(amount, self.timeout)
                + "   "
                + self.progress
                + "   "
                + next(self.spinner)
            )
            sys.stderr.write(self.line)
            sys.stderr.flush()

    def run_finished(self) -> None:
        if self.progress is not None:
            sys.stderr.write("\r" + " " * len(self.line) + "\r")
            sys.stderr.flush()

    def puzzle_finished(self, time: int, walltime: float) -> None:
        if not (self.is_missing and self.hide_missing):
            print(self.line)
        self.is_missing = False
        self.total_time += time
        self.total_walltime += walltime

    def puzzle_finished_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        self._init_line(time)
        icon = colored("❌", "red")
        string = error[: CLIListener.cutoff]
        self.line += f"   {icon} {string}"
        self.total_time += time
        self.total_walltime += walltime

    def part_missing(self, time: int, part: str) -> None:
        self.is_missing = True
        if self.hide_missing:
            return
        icon = colored("⭕", "light_green")
        string = "- missing -"
        self._update_part(time, part, string, icon)

    def part_skipped(self, time: int, part: str) -> None:
        icon = "⌚"
        string = "- skipped -"
        self._update_part(time, part, string, icon)

    def part_finished(
        self,
        time: int | None,
        part: str,
        answer: str | None,
        expected: str | None,
        correct: bool,
    ) -> None:
        if answer and correct:
            icon = colored("✅", "green")
            string = f"{answer[: CLIListener.cutoff]}"
        elif answer and not correct:
            if expected is None:
                icon = colored("?", "magenta")
                correction = "(correct answer unknown)"
            else:
                icon = colored("❌", "red")
                correction = f"(expected: {expected})"
            string = f"{answer[: CLIListener.cutoff]} {correction}"
        if time is not None:
            self.times.append(time)
        self._update_part(sum(self.times), part, string, icon)

    def stop(self) -> None:
        print()
        print(
            f"Total run time: {self.total_time / 1e9:8.4f}s\n"
            f"Took: {self.total_walltime:8.4f}s"
        )

    def _init_line(self, time: int) -> None:
        runtime = self._format_time(time, self.timeout)
        self.line = f"{runtime}   {self.progress}"

    def _update_part(
        self, time: int, part: str, answer: str, icon: str
    ) -> None:
        if part == "a":
            self._init_line(time)
            answer = answer.ljust(30)
            self.line_a = f"   {icon} {part}: {answer}"
            self.line += self.line_a
        else:
            self._init_line(time)
            self.line += self.line_a
            self.line += f"   {icon} {part}: {answer}"

    def _format_time(self, time: int, timeout: float) -> str:
        t = time / 1e9
        if t < timeout / 4:
            color = "green"
        elif t < timeout / 2:
            color = "yellow"
        else:
            color = "red"
        if t < 0.001:
            runtime = colored(f"{t * 1000:7.3f}ms", color)
        else:
            runtime = colored(f"{t:8.4f}s", color)
        return cast("str", runtime)


class JUnitXmlListener(Listener):
    def __init__(self) -> None:
        self.suite = TestSuite("Advent of Code")
        self.suite.timestamp = calendar.get_now_utc()
        self.suite.hostname = socket.gethostname()
        TestCase.year = IntAttr("year")
        TestCase.day = IntAttr("day")
        TestCase.part = Attr("part")
        TestCase.title = Attr("title")
        TestCase.plugin = Attr("plugin")
        TestCase.user_id = Attr("user_id")

    def puzzle_started(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        self.year = year
        self.day = day
        self.title = title
        self.plugin = plugin
        self.user_id = user_id

    def run_elapsed(self, amount: int) -> None:
        pass

    def run_finished(self) -> None:
        pass

    def puzzle_finished(self, time: int, walltime: float) -> None:
        pass

    def puzzle_finished_with_error(
        self, _time: int, walltime: float, error: str
    ) -> None:
        case = TestCase(
            name=f"{self.year}/{self.day:02}/{self.plugin}/{self.user_id}"
        )
        case.year = self.year
        case.day = self.day
        case.plugin = self.plugin
        case.user_id = self.user_id
        case.time = walltime
        case.result = [Error(error)]
        self.suite.add_testcase(case)

    def part_missing(self, time: int, part: str) -> None:
        pass

    def part_skipped(self, _time: int, part: str) -> None:
        case = TestCase(
            name=f"{self.year}/{self.day:02}/{part}/{self.plugin}/{self.user_id}"
        )
        case.year = self.year
        case.day = self.day
        case.part = part
        case.plugin = self.plugin
        case.user_id = self.user_id
        case.result = [Skipped()]
        self.suite.add_testcase(case)

    def part_finished(
        self,
        time: int | None,
        part: str,
        answer: str | None,
        expected: str | None,
        correct: bool,
    ) -> None:
        case = TestCase(
            name=f"{self.year}/{self.day:02}/{part}/{self.plugin}/{self.user_id}"
        )
        case.year = self.year
        case.day = self.day
        case.part = part
        case.plugin = self.plugin
        case.user_id = self.user_id
        if time is not None:
            case.time = time / 1e9
        if not correct:
            case.result = [Failure(f"Expected '{expected}', was: '{answer}'")]
        self.suite.add_testcase(case)

    def stop(self) -> None:
        xml = JUnitXml()
        xml.add_testsuite(self.suite)
        xml.write(filepath=config.junitxml["filepath"], pretty=True)


class BenchmarkListener(Listener):
    class PartRun(NamedTuple):
        year: int
        day: int
        part: str
        plugin: str
        user_id: str
        time: int

    def __init__(self) -> None:
        self.part_runs = list[BenchmarkListener.PartRun]()

    def puzzle_started(
        self, year: int, day: int, _title: str, plugin: str, user_id: str
    ) -> None:
        self.year = year
        self.day = day
        self.plugin = plugin
        self.user_id = user_id

    def run_elapsed(self, amount: int) -> None:
        pass

    def run_finished(self) -> None:
        pass

    def puzzle_finished(self, time: int, walltime: float) -> None:
        pass

    def puzzle_finished_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        pass

    def part_missing(self, time: int, part: str) -> None:
        pass

    def part_skipped(self, time: int, part: str) -> None:
        pass

    def part_finished(
        self,
        time: int | None,
        part: str,
        _answer: str | None,
        _expected: str | None,
        _correct: bool,
    ) -> None:
        if time is None:
            return
        self.part_runs.append(
            BenchmarkListener.PartRun(
                year=self.year,
                day=self.day,
                part=part,
                plugin=self.plugin,
                user_id=self.user_id,
                time=time,
            )
        )

    def stop(self) -> None:
        pass

    def get_by_time(self) -> Iterator[PartRun]:
        return (_ for _ in sorted(self.part_runs, key=lambda pr: pr.time))
