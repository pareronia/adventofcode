from typing import Any


def index_of_sublist(lst: list[Any], sub: list[Any]) -> int:
    size = len(sub)
    if len(lst) == 0 or size == 0 or len(lst) < size:
        return -1
    for idx in (i for i, e in enumerate(lst) if e == sub[0]):
        if lst[idx : idx + size] == sub:  # noqa E203
            return idx
    return -1


def indexes_of_sublist(lst: list[Any], sub: list[Any]) -> list[int]:
    idxs: list[int] = []
    i = 0
    while i + len(sub) <= len(lst):
        idx = index_of_sublist(lst[i:], sub)
        if idx == -1:
            break
        idxs.append(i + idx)
        i += idx + len(sub)
    return idxs


def subtract_all(lst: list[Any], sub: list[Any]) -> list[Any]:
    ans = list[Any]()
    i = 0
    for idx in indexes_of_sublist(lst, sub):
        while i < idx:
            ans.append(lst[i])
            i += 1
        i += len(sub)
    ans += lst[i:][:]
    return ans
