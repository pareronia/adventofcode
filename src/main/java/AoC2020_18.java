import static java.util.stream.Collectors.toList;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import com.github.pareronia.aoc.Utils;
import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2020_18 extends SolutionBase<List<String>, Long, Long> {
    
	private AoC2020_18(final boolean debug) {
		super(debug);
	}
    
    public static AoC2020_18 create() {
        return new AoC2020_18(false);
    }

    public static AoC2020_18 createDebug() {
        return new AoC2020_18(true);
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
    protected List<String> parseInput(final List<String> inputs) {
        return inputs;
    }

    @Override
	public Long solvePart1(final List<String> input) {
        return input.stream()
                .map(this::tokenize)
                .map(this::evaluate)
                .mapToLong(ResultAndPosition::result)
                .sum();
	}

	@Override
	public Long solvePart2(final List<String> input) {
	    return input.stream()
	            .map(this::fixForAdditionPreference)
	            .map(this::tokenize)
	            .map(this::evaluate)
	            .mapToLong(ResultAndPosition::result)
	            .sum();
	}

	@Samples({
	    // 71 + 51 + 26 + 437 + 12240 + 13632
	    @Sample(method = "part1", input = TEST, expected = "26457"),
	    // 231 + 51 + 46 + 1445 + 669060 + 23340
	    @Sample(method = "part2", input = TEST, expected = "694173"),
	})
	public static void main(final String[] args) throws Exception {
		AoC2020_18.create().run();
	}
	
	private static final String TEST = """
	        1 + 2 * 3 + 4 * 5 + 6
			1 + (2 * 3) + (4 * (5 + 6))
			2 * 3 + (4 * 5)
			5 + (8 * 3 + 9 + 3 * 4 * 3)
			5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))
			((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2
			""";
	
    record ResultAndPosition(long result, int position) {}
}