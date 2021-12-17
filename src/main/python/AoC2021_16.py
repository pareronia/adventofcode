#! /usr/bin/env python3
#
# Advent of Code 2021 Day 16
#

from math import prod
from aoc import my_aocd
from aoc.common import log
import aocd


def _hex2bin(hex_data: str) -> str:
    bit_count = len(hex_data) * 4
    return bin(int(hex_data, 16))[2:].zfill(bit_count)


def _bin2dec(bin_data: str) -> int:
    return int(bin_data, 2)


def _split_literal(bin_data: str, i: int) -> tuple[int, int]:
    log("literal")
    val = ""
    while True:
        proceed = bin_data[i]
        val += bin_data[i+1:i+5]
        i += 5
        if proceed == '0':
            break
    log(f"value: {val} ({_bin2dec(val)})")
    return val, i


def _split_operator(bin_data: str, i) -> tuple[list, int]:
    log("operator")
    length_type_id = bin_data[i]
    i += 1
    sub_packets = []
    if length_type_id == '0':
        size = _bin2dec(bin_data[i:i+15])
        log(f"size: {size}")
        i += 15
        cnt = 0
        while True:
            sub_packet, ii = _split(bin_data, i)
            cnt += ii - i
            log(f"used: {cnt}")
            i = ii
            if sub_packet is not None:
                sub_packets.append(sub_packet)
            if cnt == size:
                break
    elif length_type_id == '1':
        cnt = _bin2dec(bin_data[i:i+11])
        log(f"count: {cnt}")
        i += 11
        for j in range(cnt):
            sub_packet, i = _split(bin_data, i)
            sub_packets.append(sub_packet)
    else:
        raise ValueError("unexpected length_type_id")
    return (sub_packets, i)


def _split(bin_data: str, i: int) -> tuple[int, int, int, list]:
    tail = len(bin_data) - i
    if tail < 8:
        return (None, i + tail)
    version = _bin2dec(bin_data[i:i+3])
    log(f"version: {version}")
    type_id = _bin2dec(bin_data[i+3:i+6])
    log(f"type_id: {type_id}")
    i = i+6
    if type_id == 4:
        val, i = _split_literal(bin_data, i)
        return ((version, type_id, _bin2dec(val), []), i)
    else:
        operator_packets, i = _split_operator(bin_data, i)
        return ((version, type_id, None, operator_packets), i)


def _log_packet(packet: tuple[int, int, int, list]) -> None:
    log(packet)
    version, type_id, val, sub_packets = packet
    if type_id == 4:
        log("/".join([str(version), str(type_id), str(val)]))
    else:
        [_log_packet(p) for p in sub_packets]


def _sum_versions(packet: tuple[int, int, int, list]) -> int:
    version, _, _, sub_packets = packet
    return int(version) + sum(_sum_versions(p) for p in sub_packets)


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    log(inputs[0])
    bin_data = _hex2bin(inputs[0])
    log(bin_data)
    packet, _ = _split(bin_data, 0)
    _log_packet(packet)
    log("")
    return _sum_versions(packet)


def _calc_value(packet: tuple[int, int, int, list]) -> int:
    _, type_id, _, sub_packets = packet
    values = [p[2] if p[2] else _calc_value(p) for p in sub_packets]
    if type_id == 0:
        return sum(values)
    elif type_id == 1:
        return prod(values)
    elif type_id == 2:
        return min(values)
    elif type_id == 3:
        return max(values)
    elif type_id == 5:
        assert len(values) == 2
        return values[0] > values[1]
    elif type_id == 6:
        assert len(values) == 2
        return values[0] < values[1]
    elif type_id == 7:
        assert len(values) == 2
        return values[0] == values[1]
    else:
        raise ValueError("unexpected type_id")


def part_2(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    bin_data = _hex2bin(inputs[0])
    packet, _ = _split(bin_data, 0)
    return _calc_value(packet)


TEST1 = "D2FE28".splitlines()
TEST2 = "38006F45291200".splitlines()
TEST3 = "EE00D40C823060".splitlines()
TEST4 = "C200B40A82".splitlines()
TEST5 = "04005AC33890".splitlines()
TEST6 = "880086C3E88112".splitlines()
TEST7 = "CE00C43D881120".splitlines()
TEST8 = "D8005AC2A8F0".splitlines()
TEST9 = "F600BC2D8F".splitlines()
TEST10 = "9C005AC2F8F0".splitlines()
TEST11 = "9C0141080250320F1802104A08".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 16)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 9
    assert part_1(TEST3) == 14
    assert part_2(TEST4) == 3
    assert part_2(TEST5) == 54
    assert part_2(TEST6) == 7
    assert part_2(TEST7) == 9
    assert part_2(TEST8) == 1
    assert part_2(TEST9) == 0
    assert part_2(TEST10) == 0
    assert part_2(TEST11) == 1

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
