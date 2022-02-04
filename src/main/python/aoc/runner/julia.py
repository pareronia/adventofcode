import os
import subprocess  # nosec
from . import Result
from .plugin import Plugin
from .config import config


class Julia(Plugin):
    def run(self, year: int, day: int, data: str):
        def run_part(part: int) -> Result:
            file_name = config.julia['day_format'].format(year=year, day=day) \
                    + config.julia['ext']
            f = os.path.join(config.root, config.julia['base_dir'], file_name)
            self.log.debug(f)
            if not os.path.exists(f):
                return Result.missing()
            completed = subprocess.run(  # nosec
                [config.julia['command'],
                 config.julia['options'],
                 '--',
                 f,
                 str(part),
                 config.scratch_file],
                text=True,
                capture_output=True,
            )
            return Result.ok(completed.stdout.strip())

        return run_part(1), run_part(2)
