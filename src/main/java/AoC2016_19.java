import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_19 extends AoCBase {
    
    private final Integer numberOfElves;
	
	private AoC2016_19(List<String> input, boolean debug) {
		super(debug);
		assert input.size() == 1;
		this.numberOfElves = Integer.valueOf(input.get(0));
	}
	
	public static AoC2016_19 create(List<String> input) {
		return new AoC2016_19(input, false);
	}

	public static AoC2016_19 createDebug(List<String> input) {
		return new AoC2016_19(input, true);
	}
	
	@FunctionalInterface
	private interface NextLoserFinder {
		int find(boolean[] a, int j, int count);
	}
	
	private int nextIndex(int n, int size) {
		return (n + 1) % size;
	}
	
	private int findNextWithPresents(boolean[] a, int j) {
		while (!a[j]) {
			j = nextIndex(j, AoC2016_19.this.numberOfElves);
		}
		log("next with presents: " + (j + 1));
		return j;
	}
	
	@Override
	public Integer solvePart1() {
	    final boolean[] a = new boolean[this.numberOfElves];
	    int count = this.numberOfElves;
        Arrays.fill(a , true);
        int i = 0;
        int lasti = 0;
        while (count > 1) {
            int j = nextIndex(i, AoC2016_19.this.numberOfElves);
            if (a[i]) {
            	j = findNextWithPresents(a, j);
                a[j] = false;
                count--;
                lasti = i;
            }
            i = j;
        }
        log(lasti + 1);
        return lasti + 1;
	}
	
	@Override
	public Integer solvePart2() {
	    return 0;
	}
	 
	private Integer solve2() {
		final LinkedList<Integer> a = new LinkedList<>();
	    for (int x = 0; x < this.numberOfElves; x++) {
	    	a.add(x + 1);
	    }
	    a.listIterator();
        int i = 0;
        int winner = 0;
        int count = this.numberOfElves;
	    while (count > 1) {
//	        log(a);
//        	log("i: " + i);
        	final int l = (i + count / 2) % count;
        	winner = a.get(i);
        	final int loser = a.remove(l);
        	count--;
//        	log("winner:" + winner);
//        	log("loser:" + loser);
        	if (i == a.size()) {
        	    i = 0;
        	} else {
        	    i = nextIndex(a.listIterator(i).nextIndex(), count);
        	}
        	if (count % 1000 == 0) {
        		System.out.println("count: " + count);
        	}
	    }
	    return winner;
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_19.createDebug(TEST).solvePart1() == 3;
		assert AoC2016_19.createDebug(TEST).solve2() == 2;

		final List<String> input = Aocd.getData(2016, 19);
		lap("Part 1", () -> AoC2016_19.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_19.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines("5");
}