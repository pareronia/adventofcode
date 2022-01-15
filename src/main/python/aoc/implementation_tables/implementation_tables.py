#! /usr/bin/env python3

import sys
import os
import logging
from datetime import datetime
from typing import IO
if __name__ == "__main__":
    from config import Row, config
else:
    from aoc.implementation_tables.config import Row, config

log = logging.getLogger(__name__)


def _build_link(base_dir: str, pattern: str, year: int, day: int) -> str:
    path = os.path.join(base_dir, pattern.format(year=year, day=day))
    return f"[✓]({path})" if os.path.exists(path) else ""


def _build_row(days) -> str:
    return "| " + " | ".join(days) + " |"


def _print_year(year: int, rows: list[Row], f: IO[str]) -> None:
    log.debug(f"Adding {year}")
    print("| " + _build_row(str(day) for day in range(1, 26)), file=f)
    print("| ---" + _build_row("---" for day in range(1, 26)), file=f)
    for row in rows:
        log.debug(f"Adding {row.language}")
        print(
            "| " + row.language + " "
            + _build_row(_build_link(row.base_dir, row.pattern, year, day)
                         for day in range(1, 26)),
            file=f)


def _get_rows() -> list[Row]:
    rows = config.get_rows()
    log.debug(rows)
    return rows


def main(file_name: str) -> None:
    log.debug(f"file: {file_name}")
    rows = _get_rows()
    with open(file_name, 'r') as f:
        tmp = f.read()
    with open(file_name, 'w') as f:
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
            else:
                if not in_table:
                    print(line, file=f)


if __name__ == "__main__":
    now = datetime.now()
    for year in range(2015, now.year + int(now.month == 12)):
        _print_year(year, _get_rows(), sys.stdout)
    main('README.md')
