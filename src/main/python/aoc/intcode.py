def run(prog: list[int]) -> list[int]:
    for i in range(0, len(prog), 4):
        op = prog[i]
        if op == 1:
            prog[prog[i + 3]] = prog[prog[i + 1]] + prog[prog[i + 2]]
        elif op == 2:
            prog[prog[i + 3]] = prog[prog[i + 1]] * prog[prog[i + 2]]
        elif op == 99:
            return prog
        else:
            raise ValueError("Invalid op")
