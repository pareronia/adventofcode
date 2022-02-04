import os
import subprocess  # nosec
import socket
import time
from . import Result
from .plugin import Plugin
from .config import config


class Java(Plugin):
    def _java(self, year: int, day: int, data: str):
        completed = subprocess.run(  # nosec
            [config.java['command'],
             "-cp",
             ":".join(config.java['classpath']),
             config.java['class'],
             str(year), str(day), data],
            text=True,
            capture_output=True,
        )
        results = completed.stdout.splitlines()
        if results:
            return Result.ok(results[0]), Result.ok(results[1])
        else:
            return Result.missing(), Result.missing()

    def java(self, year: int, day: int, data: str):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((config.java['server']['host'],
                       config.java['server']['port']))
            s.send(f'{year}\r\n'.encode('UTF-8'))
            s.send(f'{day}\r\n'.encode('UTF-8'))
            s.send(f'{data}\r\n'.encode('UTF-8'))
            s.send(b'END\r\n')
            data = s.recv(1024)
        results = data.decode('UTF-8').rstrip().splitlines()
        self.log.info(f"Results: {results}")
        if results:
            return Result.ok(results[0]), Result.ok(results[1])
        else:
            return Result.missing(), Result.missing()

    def start_java(self):
        completed = subprocess.Popen(  # nosec
            [config.java['server']['command'],
             "-cp",
             ":".join(config.java['server']['classpath']),
             config.java['server']['class'],
             ],
        )
        if not completed.pid:
            raise RuntimeError("Could not start Java run server")
        time.sleep(0.5)
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((config.java['server']['host'],
                       config.java['server']['port']))
            s.send(('HELLO' + os.linesep).encode('UTF-8'))
            data = s.recv(1024)
        results = data.decode('UTF-8').rstrip().splitlines()
        if results != ["HELLO"]:
            raise RuntimeError("No response from Java run server")

    def stop_java(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((config.java['server']['host'],
                       config.java['server']['port']))
            s.send(b'STOP')

    def start(self):
        self.log.debug("Starting")
        self.start_java()

    def run(self, year: int, day: int, data: str):
        return self.java(year, day, data)

    def stop(self):
        self.log.debug("Stopping")
        self.stop_java()
