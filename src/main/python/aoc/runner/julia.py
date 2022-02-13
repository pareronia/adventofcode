import os
import time
import subprocess  # nosec
import socket
from . import Result
from .plugin import Plugin
from .config import config

proc: subprocess.Popen


class Julia(Plugin):

    def _julia(self, year: int, day: int, data: str):
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

    def start_julia(self):
        global proc
        proc = subprocess.Popen(  # nosec
            config.julia['server']['command'],
            stderr=subprocess.DEVNULL)
        retries = 10
        cnt = 0
        while cnt < retries:
            try:
                with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                    s.settimeout(1.0)
                    s.connect((config.julia['server']['host'],
                               config.julia['server']['port']))
                    s.send(('HELLO' + os.linesep).encode('UTF-8'))
                    data = s.recv(1024)
                    results = data.decode('UTF-8').rstrip().splitlines()
                    if results != ["HELLO"]:
                        raise RuntimeError("No response from Julia")
                    break
            except ConnectionRefusedError:
                cnt += 1
                time.sleep(1)
                continue
            except TimeoutError:
                cnt += 1
                continue
        if cnt == retries:
            raise RuntimeError("No response from Julia")

    def stop_julia(self):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
            s.connect((config.julia['server']['host'],
                       config.julia['server']['port']))
            s.send(b'STOP')
        # time.sleep(0.5)
        global proc
        if proc is not None:
            try:
                proc.wait(timeout=1)
            except subprocess.TimeoutExpired:
                proc.terminate()

    def start(self):
        self.log.debug("Starting")
        self.start_julia()

    def run(self, year: int, day: int, data: str):
        def run_part(part: int) -> Result:
            input_file = os.path.abspath(config.scratch_file)
            with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
                s.connect((config.julia['server']['host'],
                           config.julia['server']['port']))
                s.send(f'{year}\r\n'.encode('UTF-8'))
                s.send(f'{day}\r\n'.encode('UTF-8'))
                s.send(f'{part}\r\n'.encode('UTF-8'))
                s.send(f'{input_file}\r\n'.encode('UTF-8'))
                s.send(b'END\r\n')
                data = s.recv(1024)
            result = data.decode('UTF-8').rstrip()
            self.log.debug(f"Result: {result}")
            if result:
                return Result.ok(result)
            else:
                return Result.missing()

        return run_part(1), run_part(2)

    def stop(self):
        self.log.debug("Stopping")
        self.stop_julia()
