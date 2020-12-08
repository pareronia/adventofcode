#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
import my_aocd


def _parse(inputs: tuple[str]) -> list[tuple[str, int]]:
    inss = list[tuple[str, int]]()
    for input_ in inputs:
        sp = input_.split()
        inss.append((sp[0], int(sp[1])))
    return inss


def _log(msg: str) -> None:
    if __debug__:
        print(msg)


def _run_program(inss: list[tuple[str, int]], error_on_inf_loop: bool) -> int:
    _log(inss)
    acc = 0
    seen = set[int]()
    i = 0
    while i not in seen:
        if i == len(inss):
            _log("Normal exit")
            return acc
        seen.add(i)
        ins = inss[i]
        _log(f"{i}: {ins}")
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
        _log(f" -> {i} - {acc}")
    if error_on_inf_loop:
        raise RuntimeError("Infinite loop!")
    else:
        return acc


def part_1(inputs: tuple[str]) -> int:
    _log(inputs)
    inss = _parse(inputs)
    return _run_program(inss, False)


def part_2(inputs: tuple[str]) -> int:
    _log(inputs)
    inss = _parse(inputs)
    for i in range(len(inss)):
        ins = inss[i]
        _log(ins)
        if ins[0] == "nop":
            if ins[1] == 0:
                _log("nop 0 : skip")
                continue
            inss[i] = ("jmp", ins[1])
            _log("nop -> jmp")
            try:
                return _run_program(inss, True)
            except RuntimeError:
                inss[i] = ("nop", ins[1])
        if ins[0] == "jmp":
            inss[i] = ("nop", ins[1])
            _log("jmp -> nop")
            try:
                return _run_program(inss, True)
            except RuntimeError:
                inss[i] = ("jmp", ins[1])
        if ins[0] == "acc":
            _log("acc: skip")
            continue


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
    assert part_2(test) == 8

    inputs = my_aocd.get_input_as_tuple(2020, 8, 608)
    result1 = part_1(inputs)
    result2 = part_2(inputs)
    print(f"Part 1: {result1}")
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
