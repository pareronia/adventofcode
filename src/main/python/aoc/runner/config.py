import os


class Config:
    root: str
    default_timeout: int
    scratch_file: str
    bash: dict
    py: dict
    java: dict

    def __init__(self):
        self.root = os.getcwd()
        self.default_timeout = 60
        self.scratch_file = "input.txt"
        self.bash = {
            'day_format': "AoC{year}_{day:0>2}.sh",
            'base_dir': "src/main/bash",
            'command': "bash",
        }
        self.py = {
            'day_format': "AoC{year}_{day:0>2}"
        }
        self.java = {
            'command': "java",
            'classpath': [os.environ['CLASSPATH'],
                          self.root + "/target/classes"],
            'class': "com.github.pareronia.aocd.Runner",
            'server': {
                'command': "java",
                'classpath': [os.environ['CLASSPATH'],
                              self.root + "/target/classes"],
                'class': "com.github.pareronia.aocd.RunServer",
                'host': "localhost",
                'port': 5555,
            },
        }


config = Config()
