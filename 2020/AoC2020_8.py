#! /usr/bin/env python3
#
# Advent of Code 2020 Day 8
#
import my_aocd


def _parse(inputs: tuple[str]) -> list[tuple[str, int]]:
    instructions = list[tuple[str, int]]()
    for input_ in inputs:
        sp = input_.split()
        instructions.append((sp[0], int(sp[1])))
    return instructions


def _log(msg: str) -> None:
    if __debug__:
        print(msg)


def _run_program(instructions: list[tuple[str, int]],
                 error_on_inf_loop: bool) -> int:
    _log(instructions)
    accumulator = 0
    seen = set[int]()
    ip = 0  # Instruction pointer
    while ip not in seen:
        if ip == len(instructions):
            _log("Normal exit")
            return accumulator
        seen.add(ip)
        ins = instructions[ip]
        _log(f"{ip}: {ins}")
        if ins[0] == "nop":
            ip += 1
        elif ins[0] == "acc":
            accumulator += ins[1]
            ip += 1
        elif ins[0] == "jmp":
            ip += ins[1]
            if ip < 0:
                raise ValueError("Invalid instruction")
        else:
            raise ValueError("Invalid instruction")
        _log(f" -> {ip} - {accumulator}")
    if error_on_inf_loop:
        raise RuntimeError("Infinite loop!")
    else:
        return accumulator


def part_1(inputs: tuple[str]) -> int:
    _log(inputs)
    instructions = _parse(inputs)
    return _run_program(instructions, False)


def part_2(inputs: tuple[str]) -> int:
    _log(inputs)
    instructions = _parse(inputs)
    for i in range(len(instructions)):
        instruction = instructions[i]
        _log(instruction)
        if instruction[0] == "nop":
            if instruction[1] == 0:
                _log("nop 0 : skip")
                continue
            instructions[i] = ("jmp", instruction[1])
            _log("nop -> jmp")
            try:
                return _run_program(instructions, True)
            except RuntimeError:
                instructions[i] = ("nop", instruction[1])
        if instruction[0] == "jmp":
            instructions[i] = ("nop", instruction[1])
            _log("jmp -> nop")
            try:
                return _run_program(instructions, True)
            except RuntimeError:
                instructions[i] = ("jmp", instruction[1])
        if instruction[0] == "acc":
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
