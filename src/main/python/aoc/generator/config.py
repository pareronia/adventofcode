#! /usr/bin/env python3

import logging
import os
from typing import NamedTuple

import yaml
from prettyprinter import cpprint

log = logging.getLogger(__name__)


class Template(NamedTuple):
    source: str
    destination: str


class Language(NamedTuple):
    language: str
    templates: list[Template]


class Config:
    def get_languages(self) -> dict[str, Language]:
        return {
            lang["language"]: Language(
                lang["language"],
                [
                    Template(template["source"], template["destination"])
                    for template in lang["templates"]
                ],
            )
            for lang in getattr(self, "generator")["languages"]
        }


with open(os.path.join(".", "setup.yml"), "r") as f:
    setup_yaml = f.read()
config = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Config\n" + setup_yaml,
    Loader=yaml.Loader,
)
log.debug(config.__dict__)


if __name__ == "__main__":
    cpprint(config.get_languages())
