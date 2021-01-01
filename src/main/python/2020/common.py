from prettyprinter import cpprint


def log(msg) -> None:
    if __debug__:
        cpprint(msg)
