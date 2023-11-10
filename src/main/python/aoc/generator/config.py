#! /usr/bin/env python3

import logging
import os
from typing import NamedTuple

import yaml

log = logging.getLogger(__name__)


class Language(NamedTuple):
    language: str
    template: str
    base_dir: str
    pattern: str
    ext: str


class Config:
    def get_languages(self) -> dict[str, Language]:
        rows = dict[str, Language]()
        for row in getattr(self, "generator")["languages"]:
            rows[row["language"]] = Language(
                row["language"],
                row["template"],
                row["base_dir"],
                row["pattern"],
                row["ext"],
            )
        return rows


with open(os.path.join(".", "setup.yml"), "r") as f:
    setup_yaml = f.read()
config = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Config\n" + setup_yaml,
    Loader=yaml.Loader,
)
log.debug(config.__dict__)


if __name__ == "__main__":
    print(config.get_languages())
