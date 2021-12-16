#! /usr/bin/env python3
#
# Advent of Code 2021 Day 16
#

from aoc import my_aocd
from aoc.common import log
import aocd


def _hex2bin(hex_data: str) -> str:
    bit_count = len(hex_data) * 4
    return bin(int(hex_data, 16))[2:].zfill(bit_count)


def _bin2dec(bin_data: str) -> int:
    return int(bin_data, 2)


def _split_literal(bin_data: str, i: int):
    log("literal")
    val = ""
    while True:
        # breakpoint()
        proceed = bin_data[i]
        val += bin_data[i+1:i+5]
        i += 5
        if proceed == '0':
            break
    log(f"value: {val} ({_bin2dec(val)})")
    return val, i


def _split_operator(bin_data: str, i):
    log("operator")
    length_type_id = bin_data[i]
    i += 1
    if length_type_id == '0':
        size = _bin2dec(bin_data[i:i+15])
        log(f"size: {size}")
        i += 15
        sub_packets = []
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
        return (sub_packets, i)
    else:
        cnt = _bin2dec(bin_data[i:i+11])
        log(f"count: {cnt}")
        i += 11
        sub_packets = []
        for j in range(cnt):
            sub_packet, i = _split(bin_data, i)
            sub_packets.append(sub_packet)
        return (sub_packets, i)


def _split(bin_data: str, i: int):
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


def _log_packet(packet):
    log(packet)
    if packet is None:
        return
    version, type_id, val, sub_packets = packet
    if type_id == 4:
        log("/".join([str(version), str(type_id), str(val)]))
    else:
        for sub_packet in sub_packets:
            _log_packet(sub_packet)


def _sum_versions(packet) -> int:
    if packet is None:
        return 0
    version, type_id, val, sub_packets = packet
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


def part_2(inputs: tuple[str]) -> int:
    return None


TEST1 = "D2FE28".splitlines()
TEST2 = "38006F45291200".splitlines()
TEST3 = "EE00D40C823060".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 16)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 9
    assert part_1(TEST3) == 14
    assert part_2(TEST1) == 0

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
