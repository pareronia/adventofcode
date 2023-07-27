ADD = 1
MUL = 2
INP = 3
OUT = 4
JIT = 5
JIF = 6
LT = 7
EQ = 8
HLT = 99

POSITION = 0
IMMEDIATE = 1


class IntCode:
    ip: int
    prog: list[int]

    def run(
        self, prog: list[int], inputs: list[int] = [], outputs: list[int] = []
    ) -> list[int]:
        self.ip = 0
        self.prog = prog

        while True:
            op = self.prog[self.ip]
            opcode = op % 100
            modes = [
                -1,
                (op // 100) % 10,
                (op // 1000) % 10,
                (op // 10000) % 10,
            ]
            addr = self._get_addr(modes)
            if opcode == ADD:
                self[addr[3]] = self[addr[1]] + self[addr[2]]
                self.ip += 4
            elif opcode == MUL:
                self[addr[3]] = self[addr[1]] * self[addr[2]]
                self.ip += 4
            elif opcode == INP:
                self[addr[1]] = inputs.pop(0)
                self.ip += 2
            elif opcode == OUT:
                outputs.append(self[addr[1]])
                self.ip += 2
            elif opcode == HLT:
                return self.prog
            elif opcode == JIT:
                self.ip = self[addr[2]] if self[addr[1]] != 0 else self.ip + 3
            elif opcode == JIF:
                self.ip = self[addr[2]] if self[addr[1]] == 0 else self.ip + 3
            elif opcode == LT:
                self[addr[3]] = 1 if self[addr[1]] < self[addr[2]] else 0
                self.ip += 4
            elif opcode == EQ:
                self[addr[3]] = 1 if self[addr[1]] == self[addr[2]] else 0
                self.ip += 4
            else:
                raise ValueError(f"Invalid opcode '{opcode}'")

    def _get_addr(self, modes: list[int]) -> dict[int]:
        addr = dict()
        try:
            for i in [1, 2, 3]:
                if modes[i] == POSITION:
                    addr[i] = self[self.ip + i]
                elif modes[i] == IMMEDIATE:
                    addr[i] = self.ip + i
                else:
                    raise ValueError(f"Invalid mode '{modes[i]}'")
        except IndexError:
            pass
        return addr

    def __getitem__(self, addr: int) -> int:
        assert addr >= 0
        return self.prog[addr]

    def __setitem__(self, addr: int, value: int):
        assert addr >= 0
        self.prog[addr] = value
