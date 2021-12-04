import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.pareronia.aocd.Aocd;

import lombok.Getter;

public class AoC2021_04 extends AoCBase {
    
    private final List<Integer> draws;
    private final List<Board> boards;
    
	private AoC2021_04(final List<String> input, final boolean debug) {
		super(debug);
		final List<List<String>> blocks = toBlocks(input);
		this.draws = Arrays.stream(blocks.get(0).get(0).split(","))
		        .map(Integer::valueOf).collect(toList());
		log(this.draws);
		this.boards = blocks.subList(1, blocks.size()).stream()
		        .map(b -> new Board(b))
		        .collect(toList());
	}
	
	public static final AoC2021_04 create(final List<String> input) {
		return new AoC2021_04(input, false);
	}

	public static final AoC2021_04 createDebug(final List<String> input) {
		return new AoC2021_04(input, true);
	}
	
	@Override
    public Integer solvePart1() {
	    for (final Integer draw : this.draws) {
	        this.boards.forEach(b -> b.mark(draw));
	        final Optional<Board> winner = this.boards.stream()
	                .filter(Board::win)
	                .findFirst();
	        if (winner.isPresent()) {
	            printGrid(winner.get().getNumbers());
	            return draw * winner.get().value();
	        }
        }
	    throw new IllegalStateException("Unsolvable");
	}
	
	@Override
	public Integer solvePart2() {
	    for (final Integer draw : this.draws) {
	        log("Draw: " + draw);
	        this.boards.forEach(b -> b.mark(draw));
	        final List<Board> winners = this.boards.stream()
	                .filter(Board::win)
	                .collect(toList());
	        if (this.boards.size() == 1 && winners.size() == 1) {
	            printGrid(winners.get(0).getNumbers());
	            return draw * winners.get(0).value();
	        } else {
	            winners.forEach(this.boards::remove);
	        }
        }
	    throw new IllegalStateException("Unsolvable");
	}
	
	public static void main(final String[] args) throws Exception {
		assert AoC2021_04.createDebug(TEST).solvePart1() == 4512;
		assert AoC2021_04.createDebug(TEST).solvePart2() == 1924;
		
		final List<String> input = Aocd.getData(2021, 4);
		lap("Part 1", () -> AoC2021_04.create(input).solvePart1());
		lap("Part 2", () -> AoC2021_04.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines(
	        "7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1\r\n" +
	        "\r\n" +
	        "22 13 17 11  0\r\n" +
	        " 8  2 23  4 24\r\n" +
	        "21  9 14 16  7\r\n" +
	        " 6 10  3 18  5\r\n" +
	        " 1 12 20 15 19\r\n" +
	        "\r\n" +
	        " 3 15  0  2 22\r\n" +
	        " 9 18 13 17  5\r\n" +
	        "19  8  7 25 23\r\n" +
	        "20 11 10 24  4\r\n" +
	        "14 21 16 12  6\r\n" +
	        "\r\n" +
	        "14 21 17 24  4\r\n" +
	        "10 16 15  9 19\r\n" +
	        "18  8 23 26 20\r\n" +
	        "22 11 13  6  5\r\n" +
	        " 2  0 12  3  7"
    );
	
    private void printGrid(final int[][] grid) {
        Arrays.stream(grid).forEach(r ->
                log(Arrays.stream(r)
                        .mapToObj(Integer::valueOf)
                        .map(String::valueOf)
                        .collect(joining(" "))));
    }

    @Getter
	private static class Board {
	    private final int[][] numbers;
	    
	    public Board(final List<String> numbers) {
	        final int[][] cells = new int[5][5];
	        for (int i = 0; i < numbers.size(); i++) {
	            final List<Integer> ints = Arrays.stream(numbers.get(i).split("\\s+"))
	                    .filter(StringUtils::isNotBlank)
	                    .map(Integer::valueOf)
	                    .collect(toList());
	            cells[i] = ArrayUtils.toPrimitive(ints.toArray(Integer[]::new));
	        }
	        this.numbers = cells;
	    }
	    
	    public void mark(final int number) {
	        for (int row = 0; row < getHeight(); row++) {
	            final int[] cs = numbers[row];
	            for (int col = 0; col < getWidth(); col++) {
	                if (cs[col] == number) {
	                    cs[col] = -1;
	                }
	            }
	        }
	    }
	    
	    public boolean win() {
	        for (int row = 0; row < getHeight(); row++) {
	            if (allMarked(this.numbers[row])) {
	                return true;
	            }
	        }
	        for (int col = 0; col < getWidth(); col++) {
	            if (allMarked(getColumn(col))) {
	                return true;
	            }
	        }
	        return false;
	    }
	    
	    public int value() {
	        int value = 0;
	        for (int row = 0; row < getHeight(); row++) {
	            final int[] cs = numbers[row];
	            for (int col = 0; col < getWidth(); col++) {
	                if (cs[col] != -1) {
	                    value += cs[col];
	                }
	            }
	        }
	        return value;
	    }
	    
	    private boolean allMarked(final int[] rc) {
	        return Arrays.stream(rc).filter(n -> n != -1).count() == 0;
	    }
	    
	    private int[] getColumn(final int col) {
	        final int[] column = new int[getHeight()];
	        for (int row = 0; row < getHeight(); row++) {
	            column[row] = this.numbers[row][col];
	        }
	        return column;
	    }
	    
	    private int getWidth() {
	        return 5;
	    }

	    private int getHeight() {
	        return 5;
	    }
	}
}
