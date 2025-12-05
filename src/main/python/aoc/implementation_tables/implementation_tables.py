#! /usr/bin/env python3

import logging
import sys
from datetime import datetime
from pathlib import Path
from typing import IO

import aoc.calendar as calendar

if __name__ == "__main__":
    from config import Row  # type:ignore[import-not-found]
    from config import config
else:
    from aoc.implementation_tables.config import Row
    from aoc.implementation_tables.config import config

log = logging.getLogger(__name__)
logging.basicConfig(format="%(message)s", level=logging.INFO)


def _build_link(base_dir: str, pattern: str, year: int, day: int) -> str:
    path = Path(base_dir) / pattern.format(year=year, day=day)
    return f"[✓]({path})" if path.exists() else ""


def _build_row(days: list[str]) -> str:
    return "| " + " | ".join(days) + " |"


def _print_year(year: int, rows: list[Row], f: IO[str]) -> None:
    log.debug("Adding %s", year)
    days = calendar.get_days(year)
    print("| " + _build_row([str(day) for day in range(1, days + 1)]), file=f)
    print("| ---" + _build_row(["---" for day in range(1, days + 1)]), file=f)
    for row in rows:
        log.debug("Adding %s", row.language)
        day_links = [
            _build_link(row.base_dir, row.pattern + row.ext, year, day)
            for day in range(1, days + 1)
        ]
        if len("".join(day_links)) == 0:
            continue
        print(
            "| " + row.language + " " + _build_row(day_links),
            file=f,
        )


def _get_rows() -> list[Row]:
    rows = config.get_rows()
    log.debug(rows)
    return rows  # type:ignore[no-any-return]


def main(file_name: str) -> None:
    log.debug("file: %s", file_name)
    rows = _get_rows()
    with Path(file_name).open("r", encoding="utf-8") as f:
        tmp = f.read()
    with Path(file_name).open("w", encoding="utf-8") as f:
        in_table = False
        for line in tmp.splitlines():
            if line.startswith("<!-- @BEGIN:ImplementationsTable"):
                print(line, file=f)
                in_table = True
                year = int(line.split("@")[1].split(":")[2])
                _print_year(year, rows, f)
            elif line.startswith("<!-- @END:ImplementationsTable"):
                print(line, file=f)
                in_table = False
            elif not in_table:
                print(line, file=f)


if __name__ == "__main__":
    now = datetime.now()  # noqa:DTZ005
    for year in range(2015, now.year + int(now.month == 12)):
        _print_year(year, _get_rows(), sys.stdout)
    main("README.md")
