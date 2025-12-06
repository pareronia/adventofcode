import unittest

from aoc import termcolor


class ANSIColorTest(unittest.TestCase):
    def test_colors(self) -> None:
        print(termcolor.colored("red", "red"))
        print(termcolor.colored("green", "green"))
        print(termcolor.colored("yellow", "yellow"))
        print(termcolor.colored("magenta", "magenta"))
        print(termcolor.colored("white", "white"))
        print(termcolor.colored("light_green", "light_green"))
        print(termcolor.colored("bold white", "white", attrs=["bold"]))


if __name__ == "__main__":
    unittest.main()
