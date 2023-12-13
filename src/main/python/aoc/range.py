class Range:
    @classmethod
    def left_join(
        cls, r1: tuple[int, int], r2: tuple[int, int]
    ) -> tuple[tuple[int, int] | None, set[tuple[int, int]]]:
        overlap = None
        others = set()
        o0 = max(r1[0], r2[0])
        o1 = min(r1[1], r2[1])
        if o0 < o1:
            overlap = (o0, o1)
            if o0 > r1[0]:
                others.add((r1[0], o0))
            if o1 < r1[1]:
                others.add((o1, r1[1]))
        else:
            others.add(r1)
        return overlap, others
