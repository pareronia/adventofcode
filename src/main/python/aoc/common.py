from prettyprinter import cpprint
from typing import Callable


def clog(c: Callable) -> None:
    if __debug__:
        log(c())


def log(msg) -> None:
    if __debug__:
        cpprint(msg)


def spinner(num: int, period: int = 1000):
    val = num % period
    level = period // 4
    if val == 0:
        ch = "|"
    elif val == level:
        ch = "/"
    elif val == level * 2:
        ch = "-"
    elif val == level * 3:
        ch = "\\"
    else:
        return
    print(ch, end="\r", flush=True)
