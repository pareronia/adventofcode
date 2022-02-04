import importlib
from . import Result
from .plugin import Plugin
from .config import config


class Py(Plugin):
    def run(self, year: int, day: int, data: str):
        day_mod_name = config.py['day_format'].format(year=year, day=day)
        self.log.debug(day_mod_name)
        try:
            day_mod = importlib.import_module(day_mod_name)
        except ModuleNotFoundError:
            return Result.missing(), Result.missing()
        inputs = data.splitlines()
        answer_1 = day_mod.part_1(inputs)
        answer_2 = day_mod.part_2(inputs)
        return Result.ok(answer_1), Result.ok(answer_2)
