import unittest

from aoc.intcode import IntCode


class TestIntCode(unittest.TestCase):
    inp: list[int]
    out: list[int]

    def setUp(self) -> None:
        self.inp = []
        self.out = []

    def _run(
        self, prog: list[int], inp: list[int] = [], out: list[int] = []
    ) -> list[int]:
        int_code = IntCode(prog)
        int_code.run(inp, out)
        return list(int_code.get_program())

    def test(self) -> None:
        ans = self._run([1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50])
        self.assertEqual(
            ans,
            [
                3500,
                9,
                10,
                70,
                2,
                3,
                11,
                0,
                99,
                30,
                40,
                50,
            ],
        )

        ans = self._run([1, 0, 0, 0, 99])
        self.assertEqual(ans, [2, 0, 0, 0, 99])

        ans = self._run([2, 3, 0, 3, 99])
        self.assertEqual(ans, [2, 3, 0, 6, 99])

        ans = self._run([2, 4, 4, 5, 99, 0])
        self.assertEqual(ans, [2, 4, 4, 5, 99, 9801])

        ans = self._run([1, 1, 1, 4, 99, 5, 6, 0, 99])
        self.assertEqual(ans, [30, 1, 1, 4, 2, 5, 6, 0, 99])

    def test_7(self) -> None:
        prog = [
            109,
            1,
            204,
            -1,
            1001,
            100,
            1,
            100,
            1008,
            100,
            16,
            101,
            1006,
            101,
            0,
            99,
        ]
        self._run(prog, self.inp, self.out)
        self.assertEqual(self.out, prog)

    def test_8(self) -> None:
        self._run(
            [1102, 34915192, 34915192, 7, 4, 7, 99, 0], self.inp, self.out
        )
        self.assertEqual(self.out, [34_915_192 * 34_915_192])

    def test_9(self) -> None:
        self._run([104, 1125899906842624, 99], self.inp, self.out)
        self.assertEqual(self.out, [1_125_899_906_842_624])

    def test_expand(self) -> None:
        ans = self._run([1001, 5, 10, 7, 99], self.inp, self.out)
        self.assertEqual(ans, [1001, 5, 10, 7, 99, 0, 0, 10])

    def test_input_output(self) -> None:
        self.inp = [123]
        ans = self._run([3, 0, 4, 0, 99], self.inp, self.out)
        self.assertEqual(ans, [123, 0, 4, 0, 99])
        self.assertEqual(self.out, [123])

    def test_modes(self) -> None:
        ans = self._run([1002, 4, 3, 4, 33])
        self.assertEqual(ans, [1002, 4, 3, 4, 99])

        ans = self._run([1101, 100, -1, 4, 0])
        self.assertEqual(ans, [1101, 100, -1, 4, 99])

    def test_equalto_8(self) -> None:
        prog = [3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8]
        self.inp = [8]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 9, 8, 9, 10, 9, 4, 9, 99, 1, 8])

        prog = [3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8]
        self.inp = [88]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 9, 8, 9, 10, 9, 4, 9, 99, 0, 8])

        prog = [3, 3, 1108, -1, 8, 3, 4, 3, 99]
        self.inp = [8]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1108, 1, 8, 3, 4, 3, 99])

        prog = [3, 3, 1108, -1, 8, 3, 4, 3, 99]
        self.inp = [89]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1108, 0, 8, 3, 4, 3, 99])

        self.assertEqual(self.out, [1, 0, 1, 0])

    def test_lessthan_8(self) -> None:
        prog = [3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8]
        self.inp = [7]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 9, 7, 9, 10, 9, 4, 9, 99, 1, 8])

        prog = [3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8]
        self.inp = [99]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 9, 7, 9, 10, 9, 4, 9, 99, 0, 8])

        prog = [3, 3, 1107, -1, 8, 3, 4, 3, 99]
        self.inp = [0]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1107, 1, 8, 3, 4, 3, 99])

        prog = [3, 3, 1107, -1, 8, 3, 4, 3, 99]
        self.inp = [8]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1107, 0, 8, 3, 4, 3, 99])

        self.assertEqual(self.out, [1, 0, 1, 0])

    def test_jump(self) -> None:
        prog = [3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9]
        self.inp = [0]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(
            ans, [3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, 0, 0, 1, 9]
        )

        prog = [3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9]
        self.inp = [1]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(
            ans, [3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, 1, 1, 1, 9]
        )

        prog = [3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1]
        self.inp = [0]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1105, 0, 9, 1101, 0, 0, 12, 4, 12, 99, 0])

        prog = [3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1]
        self.inp = [1]
        ans = self._run(prog, self.inp, self.out)
        self.assertEqual(ans, [3, 3, 1105, 1, 9, 1101, 0, 0, 12, 4, 12, 99, 1])

        self.assertEqual(self.out, [0, 1, 0, 1])

    def test_larger(self) -> None:
        prog = [
            3,
            21,
            1008,
            21,
            8,
            20,
            1005,
            20,
            22,
            107,
            8,
            21,
            20,
            1006,
            20,
            31,
            1106,
            0,
            36,
            98,
            0,
            0,
            1002,
            21,
            125,
            20,
            4,
            20,
            1105,
            1,
            46,
            104,
            999,
            1105,
            1,
            46,
            1101,
            1000,
            1,
            20,
            4,
            20,
            1105,
            1,
            46,
            98,
            99,
        ]
        self.inp = [1, 8, 888]
        self._run(prog, self.inp, self.out)
        self._run(prog, self.inp, self.out)
        self._run(prog, self.inp, self.out)
        self.assertEqual(self.out, [999, 1000, 1001])

    def tearDown(self) -> None:
        self.assertEqual(self.inp, [])
