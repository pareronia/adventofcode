import logging


class Plugin:
    @property
    def log(self):
        name = f"{self.__class__.__module__}.{self.__class__.__name__}"
        return logging.getLogger(name)

    def start(self):
        self.log.debug("Starting")

    def run(self, year: int, day: int, data: str):
        pass

    def stop(self):
        self.log.debug("Stopping")
