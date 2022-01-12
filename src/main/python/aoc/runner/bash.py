import os
import logging
import subprocess  # nosec
from . import Result
from .config import root


def bash(year: int, day: int, data: str):
    def run_part(part: int) -> str:
        file_name = "AoC" + str(year) + "_" + str(day).rjust(2, '0') + ".sh"
        f = os.path.join(root, "src", "main", "bash", file_name)
        logging.debug(f)
        if not os.path.exists(f):
            return Result(False, None)
        completed = subprocess.run(  # nosec
            ["bash",
             f,
             str(part),
             "input.txt"],
            text=True,
            capture_output=True,
        )
        return Result(True, completed.stdout.strip())

    return run_part(1), run_part(2)
