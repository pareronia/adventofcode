import os
import atexit
import subprocess  # nosec
import socket
import logging
import time
from . import Result
from .config import config


log = logging.getLogger(__name__)


def _java(year: int, day: int, data: str):
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
        return Result(True, results[0]), Result(True, results[1])
    else:
        return Result(False, None), Result(False, None)


def java(year: int, day: int, data: str):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((config.java['server']['host'],
                   config.java['server']['port']))
        s.send(f'{year}\r\n'.encode('UTF-8'))
        s.send(f'{day}\r\n'.encode('UTF-8'))
        s.send(f'{data}\r\n'.encode('UTF-8'))
        s.send(b'END\r\n')
        data = s.recv(1024)
    results = data.decode('UTF-8').rstrip().splitlines()
    log.info(f"Results: {results}")
    if results:
        return Result(True, results[0]), Result(True, results[1])
    else:
        return Result(False, None), Result(False, None)


def start_java():
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


@atexit.register
def stop_java():
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
        s.connect((config.java['server']['host'],
                   config.java['server']['port']))
        s.send(b'STOP')
