import importlib
import logging
from . import Result
from .config import config


def py(year: int, day: int, data: str):
    day_mod_name = config.py['day_format'].format(year=year, day=day)
    logging.debug(day_mod_name)
    try:
        day_mod = importlib.import_module(day_mod_name)
    except ModuleNotFoundError:
        return Result(False, None), Result(False, None)
    inputs = data.splitlines()
    answer_1 = day_mod.part_1(inputs)
    answer_2 = day_mod.part_2(inputs)
    return Result(True, answer_1), Result(True, answer_2)
