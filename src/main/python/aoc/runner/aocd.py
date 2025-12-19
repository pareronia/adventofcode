import logging
from typing import TYPE_CHECKING
from typing import Any
from typing import NamedTuple
from typing import Self
from typing import cast

if TYPE_CHECKING:
    from pathlib import Path

import aocd
from aocd.exceptions import AocdError
from aocd.models import User

log = logging.getLogger(__name__)


class Puzzle(NamedTuple):
    puzzle: aocd.models.Puzzle
    autosubmit: bool

    @classmethod
    def create(cls, token: str, year: int, day: int, autosubmit: bool) -> Self:
        puzzle = aocd.models.Puzzle(year, day, User(token))
        ans = cls(puzzle, autosubmit)
        log.debug("Created Puzzle %s", ans)
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
        input_data_path = cast("Path", self.puzzle.input_data_path)
        return input_data_path.exists()

    def _answer_file_exists(self, part: str) -> bool:
        answer_path = cast("Path", getattr(self.puzzle, f"answer_{part}_path"))
        return answer_path.exists()

    def _get_answer(self, part: str) -> str | None:
        return getattr(self.puzzle, "answer_" + part, None)

    def _ok_to_post_answer(self, part: str) -> bool:
        return self.autosubmit and (
            part == "a" or (part == "b" and self.puzzle.answered_a)
        )

    def _post_answer(self, part: str, answer: str) -> None:
        try:
            self.puzzle._submit(answer, part, reopen=False, quiet=True)  # noqa:SLF001
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
            msg = (
                "Missing input data for "
                f"{self.token}/{self.puzzle.year}/{self.puzzle.day}"
            )
            raise RuntimeError(msg)
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
    def load_users(cls) -> dict[str, str] | Any:  # noqa:ANN401
        return aocd.models._load_users()  # noqa:SLF001
