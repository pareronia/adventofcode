from collections.abc import Iterable
from enum import Enum
from enum import unique
from typing import Self


@unique
class ANSIColor(Enum):
    color_name: str | None

    def __new__(cls, value: str, color_name: str | None = None) -> Self:
        obj = object.__new__(cls)
        obj._value_ = value
        obj.color_name = color_name
        return obj

    RESET = 0
    BOLD = 1
    RED = ("31", "red")
    GREEN = ("32", "green")
    YELLOW = ("33", "yellow")
    MAGENTA = ("35", "magenta")
    WHITE = ("37", "white")
    LIGHT_GREEN = ("32;1", "light_green")

    @classmethod
    def from_name(cls, name: str) -> Self:
        for v in ANSIColor:
            if v.color_name is not None and v.color_name == name:
                return v
        raise ValueError

    @property
    def ansi(self) -> str:
        return f"\x1b[{self.value}m"


def colored(string: str, name: str, attrs: Iterable[str] | None = None) -> str:
    color = ANSIColor.from_name(name)
    bold = (
        ANSIColor.BOLD.ansi if (attrs is not None and "bold" in attrs) else ""
    )
    return f"{bold}{color.ansi}{string}{ANSIColor.RESET.ansi}"
