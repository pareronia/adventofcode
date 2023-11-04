import logging

from . import Result


class Plugin:
    @property
    def log(self) -> logging.Logger:
        name = f"{self.__class__.__module__}.{self.__class__.__name__}"
        return logging.getLogger(name)

    def start(self) -> None:
        self.log.debug("Starting")

    def run(self, year: int, day: int, data: str) -> tuple[Result, Result]:
        return (Result.missing(), Result.missing())

    def stop(self) -> None:
        self.log.debug("Stopping")
