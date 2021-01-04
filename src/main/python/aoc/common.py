from prettyprinter import cpprint


def log(msg) -> None:
    if __debug__:
        cpprint(msg)


def spinner(num: int):
    if num % 1000 == 0:
        ch = "|"
    elif num % 1000 == 250:
        ch = "/"
    elif num % 1000 == 500:
        ch = "-"
    elif num % 1000 == 750:
        ch = "\\"
    else:
        return
    print(ch, end="\r", flush=True)
