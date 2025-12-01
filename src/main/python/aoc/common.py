from __future__ import annotations

import time
from abc import ABC
from abc import abstractmethod
from collections.abc import Callable
from collections.abc import Iterable
from enum import Enum
from enum import unique
from pathlib import Path
from typing import Any
from typing import NamedTuple
from typing import TypeVar
from typing import cast

import aocd
import prettyprinter
import pyperclip
from aoc import my_aocd
from prettyprinter import cpprint
from termcolor import colored


def clog(c: Callable[[], object]) -> None:
    if __debug__:
        log(c())


def log(msg: object) -> None:
    if __debug__:
        cpprint(msg)


def spinner(num: int, period: int = 1000) -> None:
    if not __debug__:
        return
    val = num % period
    level = period // 4
    if val == 0:
        ch = "|"
    elif val == level:
        ch = "/"
    elif val == level * 2:
        ch = "-"
    elif val == level * 3:
        ch = "\\"
    else:
        return
    print(ch, end="\r", flush=True)


InputData = Iterable[str]
type OUTPUT = str | int | None
INPUT = TypeVar("INPUT")


class SolutionBase[INPUT, OUTPUT1: OUTPUT, OUTPUT2: OUTPUT](ABC):
    @unique
    class Part(Enum):
        INPUT = "Input"
        PART_1 = "1"
        PART_2 = "2"

        @classmethod
        def from_str(cls, string: str) -> SolutionBase.Part:
            for v in SolutionBase.Part:
                if v._value_ == string:
                    return v
            msg = "Part should be 'Input', '1' or '2'"
            raise ValueError(msg)

        def __str__(self) -> str:
            return str(self._value_)

    class PartExecution(NamedTuple):
        part: SolutionBase.Part
        answer: Any
        duration: int

        @property
        def duration_as_ms(self) -> float:
            return self.duration / 1_000_000

        def __repr__(self) -> str:
            answer = colored(self.answer, "white", attrs=["bold"])
            if self.duration_as_ms <= 1_000:
                duration = f"{self.duration_as_ms:.3f}"
            elif self.duration_as_ms <= 5_000:
                duration = colored(f"{self.duration_as_ms:.0f}", "yellow")
            else:
                duration = colored(f"{self.duration_as_ms:.0f}", "red")
            return f"Part {self.part}: {answer}, took {duration} ms"

        def to_json(self) -> str:
            return (
                f'{{"part{self.part}": {{"answer": "{self.answer}", '
                f'"duration": {self.duration}}}}}'
            )

    def __init__(self, year: int, day: int) -> None:
        self.year = year
        self.day = day

    @abstractmethod
    def parse_input(self, input_data: InputData) -> INPUT:
        pass

    @abstractmethod
    def samples(self) -> None:
        pass

    @abstractmethod
    def part_1(self, inputs: INPUT) -> OUTPUT1:
        pass

    @abstractmethod
    def part_2(self, inputs: INPUT) -> OUTPUT2:
        pass

    def run(self, args: list[str]) -> None:
        def execute_part(
            part: SolutionBase.Part, f: Callable[[], Any]
        ) -> SolutionBase.PartExecution:
            start = time.time()
            answer = f()
            return SolutionBase.PartExecution(
                part, answer, int((time.time() - start) * 1e9)
            )

        if len(args) == 3:
            part = self.Part.from_str(args[1])
            with Path(args[2]).open(encoding="utf-8") as f:
                inputs: INPUT = self.parse_input(
                    line for line in f.read().rstrip("\r\n").splitlines()
                )
            if part == self.Part.PART_1:
                exec_part = execute_part(part, lambda: self.part_1(inputs))
            elif part == self.Part.PART_2:
                exec_part = execute_part(part, lambda: self.part_2(inputs))
            print(exec_part.to_json())
        else:
            aocd.utils.blocker(until=(self.year, self.day))
            puzzle = aocd.models.Puzzle(self.year, self.day)
            my_aocd.print_header(puzzle.year, puzzle.day)
            if __debug__:
                prettyprinter.install_extras(include=["dataclasses"])
                self.samples()
            exec_input = execute_part(
                self.Part.INPUT,
                lambda: self.parse_input(my_aocd.get_input_data(puzzle)),
            )
            print(f"Input took {exec_input.duration_as_ms:.3f} ms")
            exec_part1 = execute_part(
                self.Part.PART_1, lambda: self.part_1(exec_input.answer)
            )
            print(exec_part1)
            if str(exec_part1.answer) not in ("0", ""):
                pyperclip.copy(exec_part1.answer)
            exec_part2 = execute_part(
                self.Part.PART_2, lambda: self.part_2(exec_input.answer)
            )
            print(exec_part2)
            if str(exec_part2.answer) not in ("0", ""):
                pyperclip.copy(exec_part2.answer)
            my_aocd.check_results(puzzle, exec_part1.answer, exec_part2.answer)


F = TypeVar("F", bound=Callable[..., Any])


def aoc_samples(
    tests: tuple[tuple[str, str, OUTPUT], ...],
) -> Callable[[F], F]:
    def decorator(_: F) -> F:
        def wrapper(_self: SolutionBase[INPUT, OUTPUT, OUTPUT]) -> None:
            for test in tests:
                func, _, expected = test
                input_data = tuple(test[1].splitlines())
                inputs = _self.parse_input(input_data)
                actual = getattr(_self, func)(inputs)
                message = (
                    f"FAILED '{func}'. Expected: '{expected}', was: '{actual}'"
                )
                assert actual == expected, message

        return cast("F", wrapper)

    return decorator
