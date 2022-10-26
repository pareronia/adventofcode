import importlib
from . import Result
from .plugin import Plugin
from .config import config


class Py(Plugin):
    def run(self, year: int, day: int, data: str):
        def skip_part(part: int) -> bool:
            return (
                "skip" in config.py
                and {"year": year, "day": day, "part": part}
                in config.py["skip"]
            )

        day_mod_name = config.py["day_format"].format(year=year, day=day)
        self.log.debug(day_mod_name)
        try:
            day_mod = importlib.import_module(day_mod_name)
        except ModuleNotFoundError:
            return Result.missing(), Result.missing()
        inputs = data.splitlines()
        result_1 = (
            Result.skipped()
            if skip_part(1)
            else Result.ok(day_mod.part_1(inputs))
        )
        result_2 = (
            Result.skipped()
            if skip_part(2)
            else Result.ok(day_mod.part_2(inputs))
        )
        return result_1, result_2
