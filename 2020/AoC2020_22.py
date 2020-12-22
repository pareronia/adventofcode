#! /usr/bin/env python3
#
# Advent of Code 2020 Day 22
#

import my_aocd
from common import log
from copy import deepcopy


def _parse(inputs: tuple[str]) -> list[list[int]]:
    blocks = my_aocd.to_blocks(inputs)
    pl1 = []
    for i, line in enumerate(blocks[0]):
        if i == 0:
            continue
        pl1.append(int(line))
    pl2 = []
    for i, line in enumerate(blocks[1]):
        if i == 0:
            continue
        pl2.append(int(line))
    return pl1, pl2


def _play_round(pl1, pl2, rnd: int, game: int = None):
    log_game = f" (Game {game})" if game is not None else ""
    log(f"-- Round {rnd}" + log_game + " --")
    log(f"Player 1's deck: {pl1}")
    log(f"Player 2's deck: {pl2}")
    n1 = pl1.pop(0)
    log(f"Player 1 plays: {n1}")
    n2 = pl2.pop(0)
    log(f"Player 2 plays: {n2}")
    log_rnd_win = f"round {rnd} of game {game}!"\
        if game is not None else "the round!"
    if n1 > n2:
        log("Player 1 wins " + log_rnd_win)
        pl1.append(n1)
        pl1.append(n2)
    else:
        log("Player 2 wins " + log_rnd_win)
        pl2.append(n2)
        pl2.append(n1)
    log("")
    return pl1, pl2


def _play_1(pl1, pl2):
    rnd = 1
    while len(pl1) != 0 and len(pl2) != 0:
        _play_round(pl1, pl2, rnd)
        rnd += 1
    return pl1, pl2


def _get_score(pl1, pl2):
    winner = pl1 if len(pl2) == 0 else pl2
    total = 0
    for i, c in enumerate(winner):
        total += (len(winner)-i) * c
    return total


def part_1(inputs: tuple[str]) -> int:
    pl1, pl2 = _parse(inputs)
    _play_1(pl1, pl2)
    log("== Post-game results ==")
    log(f"Player 1's deck: {pl1}")
    log(f"Player 2's deck: {pl2}")
    return _get_score(pl1, pl2)


def _play_2(pl1, pl2, rnd: int = 1, game: int = 1):
    log(f"=== Game {game} ===")
    seen = set[tuple[tuple[int], tuple[int]]]()
    pl1_old = []
    pl2_old = []
    while len(pl1) != 0 and len(pl2) != 0:
        if (tuple(pl1), tuple(pl2)) in seen:
            pl2 = []
            break
        pl1_old = tuple(deepcopy(pl1))
        pl2_old = tuple(deepcopy(pl2))
        seen.add((pl1_old, pl2_old))
        log("")
        log(f"-- Round {rnd} (Game {game}) --")
        log(f"Player 1's deck: {pl1}")
        log(f"Player 2's deck: {pl2}")
        n1 = pl1.pop(0)
        log(f"Player 1 plays: {n1}")
        n2 = pl2.pop(0)
        log(f"Player 2 plays: {n2}")
        if len(pl1) >= n1 and len(pl2) >= n2:
            pl1_sub = deepcopy(pl1[0:n1])
            pl2_sub = deepcopy(pl2[0:n2])
            log("Playing a sub-game to determine the winner...")
            log("")
            _play_2(pl1_sub, pl2_sub, rnd=1, game=game+1)
            log("")
            log(f"...anyway, back to game {game}.")
            winner = 1 if len(pl2_sub) == 0 else 2
        else:
            winner = 1 if n1 > n2 else 2
        if winner == 1:
            pl1.append(n1)
            pl1.append(n2)
        else:
            pl2.append(n2)
            pl2.append(n1)
        log(f"Player {winner} wins round {rnd} of game {game}!")
        rnd += 1
    winner = 1 if len(pl2) == 0 else 2
    log(f"The winner of game {game} is player {winner}!")


def part_2(inputs: tuple[str]) -> int:
    pl1, pl2 = _parse(inputs)
    _play_2(pl1, pl2)
    log("")
    log("")
    log("== Post-game results ==")
    log(f"Player 1's deck: {pl1}")
    log(f"Player 2's deck: {pl2}")
    return _get_score(pl1, pl2)


test = """\
Player 1:
9
2
6
3
1

Player 2:
5
8
4
7
10
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 22)

    # assert part_1(test) == 306
    assert part_2(test) == 291

    # inputs = my_aocd.get_input_as_tuple(2020, 22, 53)
    # result1 = part_1(inputs)
    # print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
