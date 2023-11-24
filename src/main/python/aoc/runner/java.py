import json
import os
import subprocess  # nosec
import socket
import time
from . import Result
from .plugin import Plugin
from .config import config


class Java(Plugin):
    def _java(self, year: int, day: int, data: str) -> tuple[Result, Result]:
        completed = subprocess.run(  # nosec
            [
                config.java["command"],
                "-cp",
                ":".join(config.java["classpath"]),
                config.java["class"],
                str(year),
                str(day),
                data,
            ],
            text=True,
            capture_output=True,
        )
        result_ = completed.stdout.rstrip()
        if result_:
            result = json.loads(result_)
            return (
                Result.ok(
                    result["part1"]["answer"],
                    result["part1"]["duration"],
                ),
                Result.ok(
                    result["part2"]["answer"],
                    result["part2"]["duration"],
                ),
            )
        else:
            return Result.missing(), Result.missing()

    def java(self, year: int, day: int, data: str) -> tuple[Result, Result]:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect(
                (config.java["server"]["host"], config.java["server"]["port"])
            )
            s.send(f"{year}\r\n".encode("UTF-8"))
            s.send(f"{day}\r\n".encode("UTF-8"))
            s.send(f"{data}\r\n".encode("UTF-8"))
            s.send(b"END\r\n")
            rcv_data = s.recv(1024)
        result_ = rcv_data.decode("UTF-8").rstrip()
        self.log.info(f"Result: {result_}")
        result = json.loads(result_)
        if "part1" in result and "part2" in result:
            return (
                Result.ok(
                    result["part1"]["answer"],
                    result["part1"]["duration"],
                ),
                Result.ok(
                    result["part2"]["answer"],
                    result["part2"]["duration"],
                ),
            )
        else:
            return Result.missing(), Result.missing()

    def start_java(self) -> None:
        completed = subprocess.Popen(  # nosec
            [
                config.java["server"]["command"],
                "-cp",
                ":".join(config.java["server"]["classpath"]),
                config.java["server"]["class"],
            ],
        )
        if not completed.pid:
            raise RuntimeError("Could not start Java run server")
        time.sleep(0.5)
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect(
                (config.java["server"]["host"], config.java["server"]["port"])
            )
            s.send(("HELLO" + os.linesep).encode("UTF-8"))
            data = s.recv(1024)
        results = data.decode("UTF-8").rstrip().splitlines()
        if results != ["HELLO"]:
            raise RuntimeError("No response from Java run server")

    def stop_java(self) -> None:
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect(
                (config.java["server"]["host"], config.java["server"]["port"])
            )
            s.send(b"STOP")

    def start(self) -> None:
        self.log.debug("Starting")
        self.start_java()

    def run(self, year: int, day: int, data: str) -> tuple[Result, Result]:
        return self.java(year, day, data)

    def stop(self) -> None:
        self.log.debug("Stopping")
        self.stop_java()
