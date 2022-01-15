import os
import logging
import subprocess  # nosec
from . import Result
from .config import config


def bash(year: int, day: int, data: str):
    def run_part(part: int) -> str:
        file_name = config.bash['day_format'].format(year=year, day=day)
        f = os.path.join(config.root, config.bash['base_dir'], file_name)
        logging.debug(f)
        if not os.path.exists(f):
            return Result(False, None)
        completed = subprocess.run(  # nosec
            [config.bash['command'],
             f,
             str(part),
             config.scratch_file],
            text=True,
            capture_output=True,
        )
        return Result(True, completed.stdout.strip())

    return run_part(1), run_part(2)