#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
import my_aocd


def _parse(inputs: tuple[str]) -> list[tuple[str, int]]:
    inss = list[tuple[str, int]]()
    for input_ in inputs:
        sp = input_.split()
        inss.append((sp[0], sp[1]))
    return inss


def part_1(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    print(inss)
    acc = 0
    seen = set[int]()
    i = 0
    while i not in seen:
        seen.add(i)
        ins = inss[i]
        print(f"{i}: {ins}")
        if ins[0] == "nop":
            i += 1
        elif ins[0] == "acc":
            acc += int(ins[1])
            i += 1
        elif ins[0] == "jmp":
            i += int(ins[1])
            if i < 0:
                raise ValueError("Invalid input")
        else:
            raise ValueError("Invalid input")
        print(f" -> {i} - {acc}")
    return acc


test = ("nop +0",
        "acc +1",
        "jmp +4",
        "acc +3",
        "jmp -3",
        "acc -99",
        "acc +1",
        "jmp -4",
        "acc +6",
        )


def main() -> None:
    my_aocd.print_header(2020, 8)

    assert part_1(test) == 5
    # assert part_2(test) == 6

    inputs = my_aocd.get_input_as_tuple(2020, 8, 608)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
