from prettyprinter import pprint


def log(msg: str) -> None:
    if __debug__:
        pprint(msg)
