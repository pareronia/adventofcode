import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class NessieFinderTestCase {
	
	@Test
	public void test() {
		final List<String> grid = new ArrayList<>();
		grid.add(".#.#...#.###...#.##.##..");
		grid.add("#.#.##.###.#.##.##.#####");
		grid.add("..##.###.####..#.####.##");
		
		final List<Pair<Integer, Integer>> result = AoC2020_20.NessieFinder.findNessies(grid);
		
		assertThat(result.size(), is(1));
		assertThat(result.get(0).getLeft(), is(1));
		assertThat(result.get(0).getRight(), is(2));
		final List<String> expected = new ArrayList<>();
		expected.add(".#.#...#.###...#.##.O#..");
		expected.add("#.O.##.OO#.#.OO.##.OOO##");
		expected.add("..#O.#O#.O##O..O.#O##.##");
		assertThat(grid, equalTo(expected));
	}
}
