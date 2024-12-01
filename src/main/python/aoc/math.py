import math

SQRT_5 = math.sqrt(5)
GOLDEN_RATIO = (1 + SQRT_5) / 2
PSI = 1 - GOLDEN_RATIO


class Fibonacci:

    @classmethod
    def binet(cls, n: int) -> int:
        return int((GOLDEN_RATIO**n - PSI**n) / SQRT_5)
