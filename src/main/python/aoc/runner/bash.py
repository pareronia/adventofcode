import json
import os
import subprocess  # nosec
from . import Result
from .plugin import Plugin
from .config import config


class Bash(Plugin):
    def run(self, year: int, day: int, data: str):
        def run_part(part: int) -> Result:
            file_name = (
                config.bash["day_format"].format(year=year, day=day)
                + config.bash["ext"]
            )
            f = os.path.join(config.root, config.bash["base_dir"], file_name)
            self.log.debug(f)
            if not os.path.exists(f):
                return Result.missing()
            if (
                "skip" in config.bash
                and {"year": year, "day": day, "part": part}
                in config.bash["skip"]
            ):
                return Result.skipped()
            completed = subprocess.run(  # nosec
                [config.bash["command"], f, str(part), config.scratch_file],
                text=True,
                capture_output=True,
            )
            result = json.loads(completed.stdout.strip())
            return Result.ok(
                result["part" + str(part)]["answer"],
                result["part" + str(part)]["duration"],
            )

        return run_part(1), run_part(2)
