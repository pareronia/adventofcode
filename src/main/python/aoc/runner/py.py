import importlib
import time
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

        def run_part(part: int) -> Result:
            if skip_part(part):
                return Result.skipped()
            else:
                call = day_mod.part_1 if part == 1 else day_mod.part_2
                start = time.time()
                answer = call(inputs)
                duration = (time.time() - start) * 1e9
                return Result.ok(answer, duration)

        day_mod_name = config.py["day_format"].format(year=year, day=day)
        self.log.debug(day_mod_name)
        try:
            day_mod = importlib.import_module(day_mod_name)
        except ModuleNotFoundError:
            return Result.missing(), Result.missing()
        inputs = data.splitlines()
        return run_part(1), run_part(2)
