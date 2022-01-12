import importlib
from . import Result


def py(year: int, day: int, data: str):
    day_mod_name = "AoC" + str(year) + "_" + str(day).rjust(2, '0')
    try:
        day_mod = importlib.import_module(day_mod_name)
    except ModuleNotFoundError:
        return Result(False, None), Result(False, None)
    inputs = data.splitlines()
    answer_1 = day_mod.part_1(inputs)
    answer_2 = day_mod.part_2(inputs)
    return Result(True, answer_1), Result(True, answer_2)
