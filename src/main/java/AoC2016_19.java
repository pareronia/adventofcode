import java.util.List;

import com.github.pareronia.aoc.solution.Sample;
import com.github.pareronia.aoc.solution.Samples;
import com.github.pareronia.aoc.solution.SolutionBase;

public class AoC2016_19 extends SolutionBase<String, Integer, Integer> {
    
	private AoC2016_19(final boolean debug) {
		super(debug);
	}
	
	public static AoC2016_19 create() {
		return new AoC2016_19(false);
	}

	public static AoC2016_19 createDebug() {
		return new AoC2016_19(true);
	}
	
	@Override
    protected String parseInput(final List<String> inputs) {
        return inputs.get(0);
    }

    @Override
	public Integer solvePart1(final String input) {
	    final Elves elves = Elves.fromInput(input);
	    Node curr = elves.head;
	    while (elves.size > 1) {
	        final Node loser = curr.next;
	        elves.remove(loser);
	        curr = curr.next;
	    }
	    return curr.value;
	}
	
	@Override
	public Integer solvePart2(final String input) {
	    final Elves elves = Elves.fromInput(input);
	    Node curr = elves.head;
	    Node opposite = elves.head;
	    for (int i = 0; i < elves.size / 2; i++) {
	        opposite = opposite.next;
	    }
	    while (elves.size > 1) {
	        final Node loser = opposite;
	        elves.remove(loser);
	        if (elves.size % 2 == 1) {
	            opposite = opposite.next;
	        } else {
	            opposite = opposite.next.next;
	        }
	        curr = curr.next;
	    }
	    return curr.value;
	}
	 
	@Samples({
	    @Sample(method = "part1", input = TEST, expected = "3"),
	    @Sample(method = "part2", input = TEST, expected = "2"),
	})
	public static void main(final String[] args) throws Exception {
	    AoC2016_19.create().run();
	}
	
	private static final String TEST = "5";

	private static final class Elves {
        
        private Node head;
        private int size;

        public Elves() {
            this.head = null;
            this.size = 0;
        }
        
        public static Elves fromInput(final String input) {
            final Elves elves = new Elves();
            Node node = null;
            Node prev = null;
            for (int i = 0; i < Integer.parseInt(input); i++) {
                node = new Node(i + 1);
                if (elves.size == 0) {
                    elves.head = node;
                } else {
                    node.prev = prev;
                    prev.next = node;
                }
                prev = node;
                elves.size++;
            }
            elves.head.prev = node;
            node.next = elves.head;
            return elves;
        }
        
        public void remove(final Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            this.size--;
        }
	}

    private static class Node {
        public int value;
        public Node next;
        public Node prev;
        
        public Node(final int value) {
            this.value = value;
        }
    }
}