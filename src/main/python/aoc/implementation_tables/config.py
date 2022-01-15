#! /usr/bin/env python3

import os
import yaml
import logging
from typing import NamedTuple

log = logging.getLogger(__name__)


class Row(NamedTuple):
    language: str
    base_dir: str
    pattern: str


class Config:
    def get_rows(self) -> list[Row]:
        rows = list[Row]()
        for row in self.implementation_tables['rows']:
            rows.append(Row(row['language'], row['base_dir'], row['pattern']))
        return rows


with open(os.path.join('.', 'setup.yml'), 'r') as f:
    setup_yaml = f.read()
config = yaml.load(  # nosec
    "!!python/object:" + __name__ + ".Config\n" + setup_yaml,
    Loader=yaml.Loader)
log.debug(config.__dict__)


if __name__ == "__main__":
    print(config.get_rows())
