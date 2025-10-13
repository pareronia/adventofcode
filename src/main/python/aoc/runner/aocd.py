from __future__ import annotations

import json
import logging
import os
from typing import Any
from typing import NamedTuple

import aocd
from aocd.exceptions import AocdError
from aocd.models import AOCD_CONFIG_DIR
from aocd.models import User
from aocd.models import default_user

log = logging.getLogger(__name__)


class Puzzle(NamedTuple):
    puzzle: aocd.models.Puzzle
    autosubmit: bool

    @classmethod
    def create(
        cls, token: str, year: int, day: int, autosubmit: bool
    ) -> Puzzle:
        puzzle = aocd.models.Puzzle(year, day, User(token))
        ans = Puzzle(puzzle, autosubmit)
        log.debug(f"Created Puzzle {ans}")
        return ans

    def __repr__(self) -> str:
        return (
            f"Puzzle("
            f"puzzle=aocd.models.Puzzle("
            f"year={self.puzzle.year}, "
            f"day={self.puzzle.day}), "
            f"user=aocd.models.User(token={self.puzzle.user.token}), "
            f"autosubmit={self.autosubmit})"
        )

    def _is_offline_user(self) -> bool:
        return self.token.startswith("offline|")

    def _input_data_file_exists(self) -> bool:
        return os.path.exists(self.puzzle.input_data_fname)

    def _answer_file_exists(self, part: str) -> bool:
        answer_fname = getattr(self.puzzle, f"answer_{part}_fname")
        return os.path.exists(answer_fname)

    def _get_answer(self, part: str) -> str | None:
        return getattr(self.puzzle, "answer_" + part, None)

    def _ok_to_post_answer(self, part: str) -> bool:
        return self.autosubmit and (
            part == "a" or (part == "b" and self.puzzle.answered_a)
        )

    def _post_answer(self, part: str, answer: str) -> None:
        try:
            self.puzzle._submit(answer, part, reopen=False, quiet=True)
        except AocdError as err:
            log.warning("error submitting - %s", err)

    @property
    def token(self) -> str:
        return str(self.puzzle.user.token)

    @property
    def title(self) -> str:
        return str(self.puzzle.title)

    @property
    def input_data(self) -> str | None:
        if self._is_offline_user() and not self._input_data_file_exists():
            return None
        input_data = self.puzzle.input_data
        if input_data is None or str(input_data) == "":
            raise RuntimeError(
                f"Missing input data for "
                f"{self.token}/{self.puzzle.year}/{self.puzzle.day}"
            )
        return str(input_data)

    def get_expected(
        self,
        part: str,
        answer: str | None,
    ) -> str | None:
        if self._is_offline_user() and not self._answer_file_exists(part):
            return None

        expected = self._get_answer(part)
        if (
            expected is None
            and answer is not None
            and self._ok_to_post_answer(part)
        ):
            self._post_answer(part, answer)
            expected = self._get_answer(part)
        return expected


class AocdHelper:
    @classmethod
    def _tokens_path(cls) -> str:
        return os.path.join(AOCD_CONFIG_DIR, "tokens.json")

    @classmethod
    def load_users(cls) -> dict[str, str] | Any:
        path = cls._tokens_path()
        try:
            with open(path) as f:
                users = json.load(f)
        except IOError:
            users = {"default": default_user().token}
        return users
