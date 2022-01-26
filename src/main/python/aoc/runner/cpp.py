import os
import logging
import subprocess  # nosec
from . import Result
from .config import config


def cpp(year: int, day: int, data: str):
    file_name = config.cpp['day_format'].format(year=year, day=day)
    f = os.path.join(config.root, config.cpp['base_dir'], file_name)
    logging.debug(f)
    if not os.path.exists(f):
        return Result(False, None), Result(False, None)
    completed = subprocess.run(  # nosec
        [f],
        text=True,
        capture_output=True,
    )
    result1, result2 = completed.stdout.splitlines()
    return Result(True, result1[8:]), Result(True, result2[8:])
