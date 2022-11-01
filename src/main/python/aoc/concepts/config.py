#! /usr/bin/env python3

import os
import yaml
import logging
from collections.abc import Generator
from typing import NamedTuple
from prettyprinter import cpprint

log = logging.getLogger(__name__)


class Day(NamedTuple):
    year: int
    day: int
    tags: list[str]

    def __lt__(self, other):
        if self.year == other.year:
            return self.day < other.day
        return self.year < other.year


class Concepts:
    def get_days(self) -> Generator[Day]:
        for year in self.concepts["year"]:
            for y_num, y_dict in year.items():
                for day in y_dict["day"]:
                    for d_num, d_dict in day.items():
                        yield Day(y_num, d_num, d_dict["tags"])


with open(os.path.join(".", "doc", "concepts.yml"), "r") as f:
    concepts_yaml = f.read()
concepts = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Concepts\n" + concepts_yaml,
    Loader=yaml.Loader,
)
log.debug(concepts.__dict__)


if __name__ == "__main__":
    print(concepts.__dict__)
    cpprint([_ for _ in concepts.get_days()])
