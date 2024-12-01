#! /usr/bin/env python3

import sys
import logging

from .stats import main

logging.basicConfig(level=logging.INFO)
args = sys.argv[1:]
if "-v" in args:
    logging.getLogger("aoc.stats").setLevel(logging.DEBUG)
    args = [a for a in args if a != "-v"]
main(args)
