#! /usr/bin/env python3
#
# Advent of Code 2024 Day 24
#

import sys
from collections import deque
from operator import and_
from operator import or_
from operator import xor
from typing import cast

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Gate = tuple[str, str, str, str]
Input = tuple[dict[str, int], list[Gate]]
Output1 = int
Output2 = str

OPS = {
    "OR": or_,
    "AND": and_,
    "XOR": xor,
}


TEST1 = """\
x00: 1
x01: 1
x02: 1
y00: 0
y01: 1
y02: 0

x00 AND y00 -> z00
x01 XOR y01 -> z01
x02 OR y02 -> z02
"""
TEST2 = """\
x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        wires = dict[str, int]()
        gates = list[Gate]()
        for line in input_data:
            if ": " in line:
                name, value = line.split(": ")
                wires[name] = int(value)
            elif " -> " in line:
                in1, op, in2, _, out = line.split()
                gates.append((in1, op, in2, out))
        return wires, gates

    def part_1(self, system: Input) -> Output1:
        wires, gates = system
        q = deque(gates)
        while q:
            in1, op, in2, out = q.popleft()
            if in1 in wires and in2 in wires:
                wires[out] = cast("int", OPS[op](wires[in1], wires[in2]))
            else:
                q.append((in1, op, in2, out))
        max_z = max(
            int(out[1:]) for _, _, _, out in gates if out.startswith("z")
        )
        return sum((1 << i) * wires[f"z{i:0>2}"] for i in range(max_z, -1, -1))

    def part_2(self, system: Input) -> Output2:
        def is_swapped(gate: Gate) -> bool:
            def outputs_to_z_except_first_one_and_not_xor(gate: Gate) -> bool:
                _, op, _, out = gate
                return out[0] == "z" and op != "XOR" and out != "z45"

            def is_xor_not_connected_to_x_or_y_or_z(gate: Gate) -> bool:
                in1, op, in2, out = gate
                return op == "XOR" and not any(
                    name[0] in ("x", "y", "z") for name in (in1, in2, out)
                )

            def is_and_except_last_with_output_not_to_or(gate: Gate) -> bool:
                in1, op, in2, out = gate
                return (
                    op == "AND"
                    and not (in1 == "x00" or in2 == "x00")
                    and any(
                        out in (other_in1, other_in2) and other_op != "OR"
                        for other_in1, other_op, other_in2, _ in gates
                    )
                )

            def is_xor_with_output_to_or(gate: Gate) -> bool:
                in1, op, in2, out = gate
                return (
                    op == "XOR"
                    and not (in1 == "x00" or in2 == "x00")
                    and any(
                        out in (other_in1, other_in2) and other_op == "OR"
                        for other_in1, other_op, other_in2, _ in gates
                    )
                )

            return (
                outputs_to_z_except_first_one_and_not_xor(gate)
                or is_xor_not_connected_to_x_or_y_or_z(gate)
                or is_and_except_last_with_output_not_to_or(gate)
                or is_xor_with_output_to_or(gate)
            )

        _, gates = system
        return ",".join(sorted(gate[3] for gate in gates if is_swapped(gate)))

    @aoc_samples(
        (
            ("part_1", TEST1, 4),
            ("part_1", TEST2, 2024),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 24)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
