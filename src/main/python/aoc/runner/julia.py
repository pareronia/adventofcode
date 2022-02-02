import os
import logging
import subprocess  # nosec
from . import Result
from .config import config


def julia(year: int, day: int, data: str):
    def run_part(part: int) -> Result:
        file_name = config.julia['day_format'].format(year=year, day=day) \
                + config.julia['ext']
        f = os.path.join(config.root, config.julia['base_dir'], file_name)
        logging.debug(f)
        if not os.path.exists(f):
            return Result.missing()
        completed = subprocess.run(  # nosec
            [config.julia['command'],
             f,
             str(part),
             config.scratch_file],
            text=True,
            capture_output=True,
        )
        return Result.ok(completed.stdout.strip())

    return run_part(1), run_part(2)
