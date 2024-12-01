#! /usr/bin/env python3

import logging
import sys
from collections import defaultdict
from typing import IO
from typing import Iterator

from prettyprinter import cpprint

if __name__ == "__main__":
    from config import Day  # type:ignore
    from config import concepts
else:
    from aoc.concepts.config import Day
    from aoc.concepts.config import concepts


log = logging.getLogger(__name__)


def _get_tags() -> dict[str, Day]:
    tags = defaultdict(list)
    for day in _get_days():
        for tag in day.tags:
            tags[tag].append(day)
    return tags


def _get_days() -> Iterator[Day]:
    for _ in concepts.get_days():
        yield _


def _print_days(days: Iterator[Day], f: IO[str]) -> None:
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


def main(file_name: str) -> None:
    log.debug(f"file: {file_name}")
    with open(file_name, "r", encoding="utf-8") as f:
        tmp = f.read()
    with open(file_name, "w", encoding="utf-8") as f:
        in_days_table, in_tags_table = False, False
        for line in tmp.splitlines():
            if line.startswith("<!-- @BEGIN:DaysTable"):
                print(line, file=f)
                in_days_table = True
                _print_days(_get_days(), f)
            elif line.startswith("<!-- @BEGIN:TagsTable"):
                print(line, file=f)
                in_tags_table = True
                _print_tags(_get_tags(), f)
            elif line.startswith("<!-- @END:DaysTable"):
                print(line, file=f)
                in_days_table = False
            elif line.startswith("<!-- @END:TagsTable"):
                print(line, file=f)
                in_tags_table = False
            else:
                if not in_days_table and not in_tags_table:
                    print(line, file=f)


if __name__ == "__main__":
    cpprint(_ for _ in _get_days())
    cpprint(_get_tags())
    _print_days(_get_days(), sys.stdout)
    _print_tags(_get_tags(), sys.stdout)
