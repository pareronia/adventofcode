from typing import NamedTuple


class KnotHash:

    SEED = [_ for _ in range(256)]
    PAD = [17, 31, 73, 47, 23]

    class State(NamedTuple):
        elements: list[int]
        lengths: list[int]
        cur: int
        skip: int

    @classmethod
    def bin_string(cls, input_: str) -> str:
        return KnotHash._to_bin_string(
            KnotHash._calculate(KnotHash._padded_lengths(input_))
        )

    @classmethod
    def hex_string(cls, input_: str) -> str:
        return KnotHash._to_hex_string(
            KnotHash._calculate(KnotHash._padded_lengths(input_))
        )

    @classmethod
    def _padded_lengths(cls, input_: str) -> list[int]:
        lengths = [ord(_) for _ in input_]
        lengths.extend(KnotHash.PAD)
        return lengths

    @classmethod
    def _calculate(cls, lengths: list[int]) -> list[int]:
        elements = [_ for _ in KnotHash.SEED]
        state = KnotHash.State(elements, lengths, 0, 0)
        for _ in range(64):
            state = KnotHash.round(state)
        dense = [0] * 16
        for i in range(16):
            for j in range(i * 16, i * 16 + 16, 1):
                dense[i] ^= state.elements[j]
        return dense

    @classmethod
    def round(cls, state: State) -> State:
        elements = [_ for _ in state.elements]
        cur = state.cur
        skip = state.skip
        for length in state.lengths:
            elements = KnotHash._reverse(elements, cur, length)
            cur = (cur + length + skip) % len(elements)
            skip += 1
        return KnotHash.State(elements, state.lengths, cur, skip)

    @classmethod
    def _reverse(
        cls, elements: list[int], start: int, length: int
    ) -> list[int]:
        size = len(elements)
        for i in range(length // 2):
            first = (start + i) % size
            second = (start + length - 1 - i) % size
            temp = elements[first]
            elements[first] = elements[second]
            elements[second] = temp
        return elements

    @classmethod
    def _to_hex_string(cls, dense: list[int]) -> str:
        return "".join(("%0.2X" % i).lower() for i in dense)

    @classmethod
    def _to_bin_string(cls, dense: list[int]) -> str:
        return "".join(format(i, "08b") for i in dense)
