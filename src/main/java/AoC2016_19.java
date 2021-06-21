import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

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
	
	private Integer solve(NextLoserFinder nextLoserFinder) {
	    final boolean[] a = new boolean[this.numberOfElves];
	    int count = this.numberOfElves;
        Arrays.fill(a , true);
        int i = 0;
        int lasti = 0;
        while (count > 1) {
            int j = nextIndex(i, AoC2016_19.this.numberOfElves);
            if (a[i]) {
            	j = nextLoserFinder.find(a, j, count);
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
	public Integer solvePart1() {
		return solve((a, j, count) -> {
			return findNextWithPresents(a, j);
		});
	}
	
	private void logArray(int[] a) {
		if (!debug) {
			return;
		}
		log(StringUtils.join(a, ','));
	}
	
	@Override
	public Integer solvePart2() {
	    int[] a = new int[this.numberOfElves];
	    for (int x = 0; x < a.length; x++) {
	    	a[x] = x + 1;
	    }
	    logArray(a);
        int i = 0;
        int lastWinner = 0;
        while (a.length > 1) {
        	log("i: " + i);
        	int l = (i + a.length / 2) % a.length;
        	int winner = a[i];
        	log("winner:" + winner);
        	log("loser:" + a[l]);
        	a = ArrayUtils.remove(a, l);
        	logArray(a);
        	i = Arrays.binarySearch(a, winner);
        	lastWinner = a[i];
        	i = nextIndex(i, a.length);
        	if (a.length % 1000 == 0) {
        		System.out.println("count: " + a.length);
        	}
        }
        log(lastWinner);
        return lastWinner;
	}
	
	private Integer solve2() {
		final LinkedList<Integer> a = new LinkedList<>(); 
	    for (int x = 0; x < this.numberOfElves; x++) {
	    	a.add(x + 1);
	    }
	    a.listIterator();
        int i = a.getFirst();
	    while (a.size() > 1) {
        	log("i: " + i);
        	int l = (i + a.size() / 2) % a.size();
        	int winner = i;	    	
        	log("winner:" + winner);
        	log("loser:" + a.get(l));
        	a.remove(l);
	    }
	    return 0; // FIXME
	}
	
	public static void main(String[] args) throws Exception {
		assert AoC2016_19.createDebug(TEST).solvePart1() == 3;
		assert AoC2016_19.createDebug(TEST).solvePart2() == 2;

		final List<String> input = Aocd.getData(2016, 19);
		lap("Part 1", () -> AoC2016_19.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_19.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines("5");
}