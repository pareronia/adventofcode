import itertools
import sys
from abc import ABC
from abc import abstractmethod
from typing import Iterable

from termcolor import colored

from . import Result


class Listener(ABC):
    @abstractmethod
    def start(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        pass

    @abstractmethod
    def elapsed(self, amount: int) -> None:
        pass

    @abstractmethod
    def finished(self) -> None:
        pass

    @abstractmethod
    def finalize(self, time: int, walltime: float) -> None:
        pass

    @abstractmethod
    def finalize_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        pass

    @abstractmethod
    def finalize_part_with_missing(
        self, time: int, part: str, result: Result
    ) -> None:
        pass

    @abstractmethod
    def finalize_part_with_skipped(
        self, time: int, part: str, result: Result
    ) -> None:
        pass

    @abstractmethod
    def finalize_part_with_ok(
        self,
        time: int,
        part: str,
        result: Result,
        expected: str | None,
        correct: bool,
    ) -> None:
        pass

    @abstractmethod
    def stop(self) -> None:
        pass


class CLIListener(Listener):
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

    def start(
        self, year: int, day: int, title: str, plugin: str, user_id: str
    ) -> None:
        self.line = self._format_time(0, self.timeout)
        progress = "{}/{:<2d} - {:<39}   {:>%d}/{:<%d}"
        progress %= (self.plugin_pad, self.user_pad)
        self.progress = progress.format(year, day, title, plugin, user_id)

    def elapsed(self, amount: int) -> None:
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

    def finished(self) -> None:
        if self.progress is not None:
            sys.stderr.write("\r" + " " * len(self.line) + "\r")
            sys.stderr.flush()

    def finalize(self, time: int, walltime: float) -> None:
        if not (self.is_missing and self.hide_missing):
            print(self.line)
        self.is_missing = False
        self.total_time += time
        self.total_walltime += walltime

    def finalize_with_error(
        self, time: int, walltime: float, error: str
    ) -> None:
        self._init_line(time)
        icon, answer = self._get_icon_and_answer(None, None, None, error)
        self.line += f"   {icon} {answer}"
        self.total_time += time
        self.total_walltime += walltime

    def finalize_part_with_missing(
        self, time: int, part: str, result: Result
    ) -> None:
        self.is_missing = True
        if self.hide_missing:
            return
        icon, answer = self._get_icon_and_answer(result, None, None, None)
        self._update_part(time, part, answer, icon)

    def finalize_part_with_skipped(
        self, time: int, part: str, result: Result
    ) -> None:
        icon, answer = self._get_icon_and_answer(result, None, None, None)
        self._update_part(time, part, answer, icon)

    def finalize_part_with_ok(
        self,
        time: int,
        part: str,
        result: Result,
        expected: str | None,
        correct: bool,
    ) -> None:
        icon, answer = self._get_icon_and_answer(
            result, correct, expected, None
        )
        self._update_part(time, part, answer, icon)

    def stop(self) -> None:
        print()
        print(
            "Total run time: {:8.4f}s\nTook: {:8.4f}s".format(
                self.total_time / 1e9, self.total_walltime
            )
        )

    def _init_line(self, time: int) -> None:
        runtime = self._format_time(time, self.timeout)
        self.line = "   ".join([runtime, self.progress])

    def _update_part(
        self, time: int, part: str, answer: str, icon: str
    ) -> None:
        if part == "a":
            self._init_line(time)
            answer = answer.ljust(30)
        self.line += f"   {icon} part {part}: {answer}"

    def _format_time(self, time: int, timeout: float) -> str:
        t = time / 1e9
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

    def _get_icon_and_answer(
        self,
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
            icon = colored("⭕", "light_green")
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
