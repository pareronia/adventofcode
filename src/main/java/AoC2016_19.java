import java.util.List;

import com.github.pareronia.aocd.Aocd;

public class AoC2016_19 extends AoCBase {
    
    private final DoublyLinkedList elves;
	
	private AoC2016_19(final List<String> input, final boolean debug) {
		super(debug);
		assert input.size() == 1;
	    this.elves = new DoublyLinkedList();
	    for (int i = 1; i <= Integer.valueOf(input.get(0)); i++) {
	        elves.addTail(i);
	    }
	    this.elves.close();
	}
	
	public static AoC2016_19 create(final List<String> input) {
		return new AoC2016_19(input, false);
	}

	public static AoC2016_19 createDebug(final List<String> input) {
		return new AoC2016_19(input, true);
	}
	
	@Override
	public Integer solvePart1() {
	    Node curr = this.elves.head;
	    while (this.elves.size > 1) {
	        final Node loser = curr.next;
	        elves.remove(loser);
	        curr = curr.next;
	    }
	    return curr.value;
	}
	
	@Override
	public Integer solvePart2() {
	    Node curr = this.elves.head;
	    Node opposite = this.elves.head;
	    for (int i = 0; i < this.elves.size / 2; i++) {
	        opposite = opposite.next;
	    }
	    while (this.elves.size > 1) {
	        final Node loser = opposite;
	        this.elves.remove(loser);
	        if (this.elves.size % 2 == 1) {
	            opposite = opposite.next;
	        } else {
	            opposite = opposite.next.next;
	        }
	        curr = curr.next;
	    }
	    return curr.value;
	}
	 
	public static void main(final String[] args) throws Exception {
		assert AoC2016_19.createDebug(TEST).solvePart1() == 3;
		assert AoC2016_19.createDebug(TEST).solvePart2() == 2;

		final List<String> input = Aocd.getData(2016, 19);
		lap("Part 1", () -> AoC2016_19.create(input).solvePart1());
		lap("Part 2", () -> AoC2016_19.create(input).solvePart2());
	}
	
	private static final List<String> TEST = splitLines("5");

	public static final class DoublyLinkedList {
        
        private Node head;
        private Node tail;
        private int size;

        public DoublyLinkedList() {
            this.head = null;
            this.tail = null;
            this.size = 0;
        }
        
        public void close() {
            head.prev = tail;
            tail.next = head;
        }
        
        public void addTail(final int value) {
            final Node node = new Node(value);
            node.next = null;
            if (this.size == 0) {
                this.tail = node;
                this.head = node;
            } else {
                this.tail.next = node;
                node.prev = this.tail;
                this.tail = node;
            }
            this.size++;
        }
        
        public void remove(final Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
            this.size--;
        }
	}

    public static class Node {
        public int value;
        public Node next;
        public Node prev;
        
        public Node(final int value) {
            this.value = value;
        }
    }
}