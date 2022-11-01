#! /usr/bin/env python3

import sys
import logging
from collections import defaultdict
from collections.abc import Generator
from prettyprinter import cpprint
from typing import IO

if __name__ == "__main__":
    from config import concepts, Day
else:
    from aoc.concepts.config import concepts, Day


log = logging.getLogger(__name__)


def _get_tags() -> dict[str, Day]:
    tags = defaultdict(list)
    [tags[tag].append(day) for day in _get_days() for tag in day.tags]
    return tags


def _get_days() -> Generator[Day]:
    return concepts.get_days()


def _print_days(days: Generator[Day], f: IO[str]) -> None:
    print("| Day | Concepts |", file=f)
    print("| --- | --- |", file=f)
    for day in sorted(days):
        t = ", ".join(sorted(day.tags))
        print(f"| {day.year}_{day.day:02} | {t} |", file=f)


def _print_tags(tags: dict[str, Day], f: IO[str]) -> None:
    print("| Concept | Days |", file=f)
    print("| --- | --- |", file=f)
    for tag, days in sorted(tags.items()):
        d = ", ".join(f"{day.year}_{day.day:02}" for day in sorted(days))
        print(f"| {tag} | {d} |", file=f)


if __name__ == "__main__":
    cpprint([_ for _ in _get_days()])
    cpprint(_get_tags())
    _print_days(_get_days(), sys.stdout)
    _print_tags(_get_tags(), sys.stdout)
