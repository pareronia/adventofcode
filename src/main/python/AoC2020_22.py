#! /usr/bin/env python3
#
# Advent of Code 2020 Day 22
#

from dataclasses import dataclass
from copy import deepcopy
from aoc import my_aocd
from aoc.common import log


@dataclass
class Players:
    player1: list[int]
    player2: list[int]


def _parse(inputs: tuple[str]) -> Players:
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
    return Players(pl1, pl2)


def _get_score(players: Players):
    log("")
    log("")
    log("== Post-game results ==")
    log(f"Player 1's deck: {players.player1}")
    log(f"Player 2's deck: {players.player2}")
    log("")
    log("")
    winner = players.player1 if players.player1 else players.player2
    total = 0
    for i, c in enumerate(winner):
        total += (len(winner)-i) * c
    return total


def _play_combat(players: Players, total_games: int, recursive: bool):
    total_games += 1
    # game = total_games
    # clog(lambda: f"=== Game {game} ===")
    seen = set()
    # rnd = 1
    pl1 = players.player1
    pl2 = players.player2
    while pl1 and pl2:
        round_ = (tuple(pl1), tuple(pl2))
        if recursive and round_ in seen:
            return players, total_games
        seen.add(round_)
        # clog(lambda: "")
        # clog(lambda: f"-- Round {rnd} (Game {game}) --")
        # clog(f"Player 1's deck: {pl1}")
        # clog(lambda: f"Player 2's deck: {pl2}")
        n1 = pl1.pop(0)
        # clog(lambda: f"Player 1 plays: {n1}")
        n2 = pl2.pop(0)
        # clog(lambda: f"Player 2 plays: {n2}")
        if recursive and len(pl1) >= n1 and len(pl2) >= n2:
            # clog(lambda: "Playing a sub-game to determine the winner...")
            # clog(lambda: "")
            pl1_sub = deepcopy(pl1[:n1])
            pl2_sub = deepcopy(pl2[:n2])
            players_sub = Players(pl1_sub, pl2_sub)
            players_sub, total_games = _play_combat(players_sub,
                                                    total_games, True)
            winner = 1 if players_sub.player1 else players_sub.player2
            # clog(lambda: "")
            # clog(lambda: f"...anyway, back to game {game}.")
        else:
            winner = 1 if n1 > n2 else 2
        if winner == 1:
            pl1.append(n1)
            pl1.append(n2)
        else:
            pl2.append(n2)
            pl2.append(n1)
        # clog(lambda: f"Player {winner} wins round {rnd} of game {game}!")
        # if recursive and rnd % 2000 == 0:
        #     print('.', end='', flush=True)
        # rnd += 1
    # clog(lambda: f"The winner of game {game} is player {winner}!")
    return players, total_games


def _play_regular_combat(players: Players, total_games: int):
    return _play_combat(players, total_games, False)


def _play_recursive_combat(players: Players, total_games: int):
    return _play_combat(players, total_games, True)


def part_1(inputs: tuple[str]) -> int:
    players = _parse(inputs)
    players, _ = _play_regular_combat(players, total_games=0)
    return _get_score(players)


def part_2(inputs: tuple[str]) -> int:
    players = _parse(inputs)
    players, _ = _play_recursive_combat(players, total_games=0)
    # print("")
    return _get_score(players)


TEST = """\
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

    assert part_1(TEST) == 306
    assert part_2(TEST) == 291

    inputs = my_aocd.get_input(2020, 22, 53)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
