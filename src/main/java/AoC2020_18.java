import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aocd.Puzzle;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AoC2020_18 extends AoCBase {
    
    private final List<String> input;
    
	private AoC2020_18(final List<String> input, final boolean debug) {
		super(debug);
		this.input = input;
	}
    
    public static AoC2020_18 create(final List<String> input) {
        return new AoC2020_18(input, false);
    }

    public static AoC2020_18 createDebug(final List<String> input) {
        return new AoC2020_18(input, true);
    }
    
    private List<Character> tokenize(final String string) {
        return Utils.asCharacterStream(string)
            .filter(ch -> ch != ' ')
            .collect(toList());
    }
    
    private ResultAndPosition evaluate(final List<Character> expression) {
        return evaluate(expression, 0);
    }
    
    private ResultAndPosition evaluate(final List<Character> expression, final int pos) {
        final Deque<String> stack = new ArrayDeque<>();
        stack.addLast("0");
        stack.addLast("+");
        long result = 0;
        int i = pos;
        while (i < expression.size()) {
            final Character ch = expression.get(i);
            if (Set.of('+', '*').contains(ch)) {
                stack.addLast(ch.toString());
            } else if (ch == ')') {
                return new ResultAndPosition(result, i);
            } else {
                final long operand2;
                if (ch == '(') {
                    final ResultAndPosition sub = evaluate(expression, i + 1);
                    operand2 = sub.result;
                    i = sub.position;
                } else {
                    assert Character.isDigit(ch);
                    operand2 = Character.digit(ch, 10);
                }
                final String operator = stack.pollLast();
                final long operand1 = Long.parseLong(stack.pollLast());
                if ("+".equals(operator)) {
                    result = operand1 + operand2;
                } else {
                    result = operand1 * operand2;
                }
                stack.addLast(String.valueOf(result));
            }
            i++;
        }
        return new ResultAndPosition(result, i);
    }
    
    private String fixForAdditionPreference(final String string) {
        String x = string.replace("(", "((");
        x = x.replace(")", "))");
        return "(" + x.replace("*", ") * (") + ")";
    }
	
    @Override
	public Long solvePart1() {
        return this.input.stream()
                .map(this::tokenize)
                .map(this::evaluate)
                .mapToLong(ResultAndPosition::getResult)
                .sum();
	}

	@Override
	public Long solvePart2() {
	    return this.input.stream()
	            .map(this::fixForAdditionPreference)
	            .map(this::tokenize)
	            .map(this::evaluate)
	            .mapToLong(ResultAndPosition::getResult)
	            .sum();
	}

	public static void main(final String[] args) throws Exception {
		assert createDebug(TEST).solvePart1() == 71 + 51 + 26 + 437 + 12240 + 13632;
		assert createDebug(TEST).solvePart2() == 231 + 51 + 46 + 1445 + 669060 + 23340;
		
        final Puzzle puzzle = puzzle(AoC2020_18.class);
		final List<String> input = puzzle.getInputData();
        puzzle.check(
           () -> lap("Part 1", create(input)::solvePart1),
           () -> lap("Part 2", create(input)::solvePart2)
	    );
	}
	
	private static final List<String> TEST = splitLines(
			"1 + 2 * 3 + 4 * 5 + 6\r\n"                          +
			"1 + (2 * 3) + (4 * (5 + 6))\r\n"                    +
			"2 * 3 + (4 * 5)\r\n"                                +
			"5 + (8 * 3 + 9 + 3 * 4 * 3)\r\n"                    +
			"5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))\r\n"      +
			"((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"
	);
	
	@RequiredArgsConstructor
    private static final class ResultAndPosition {
	    @Getter
        private final long result;
        private final int position;
	}
}