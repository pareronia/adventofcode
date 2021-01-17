import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.deepEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Iterator;

import org.junit.Test;

public class TilePermutatorTestCase {
	
	@Test
	public void test() {
		final char[][] grid = {
				{ 'X', 'Y', '.' },
				{ '.', '.', '.' },
				{ '.', '.', '.' }
		};
		final AoC2020_20.Tile in = new AoC2020_20.Tile(-1, grid);
		
		final Iterator<AoC2020_20.Tile> allPermutations
				= AoC2020_20.TilePermutator.getAllPermutations(in);
		
		// Permutation 0 : original
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ 'X', 'Y', '.' },
			{ '.', '.', '.' },
			{ '.', '.', '.' }
		}), is (TRUE));
		// Permutation 1 : rotated 90
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', '.', 'X' },
			{ '.', '.', 'Y' },
			{ '.', '.', '.' }
		}), is (TRUE));
		// Permutation 2 : rotated 180
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', '.', '.' },
			{ '.', '.', '.' },
			{ '.', 'Y', 'X' }
		}), is (TRUE));
		// Permutation 3 : rotated 270
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', '.', '.' },
			{ 'Y', '.', '.' },
			{ 'X', '.', '.' }
		}), is (TRUE));
		// Permutation 4 : diagonal TB
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ 'X', '.', '.' },
			{ 'Y', '.', '.' },
			{ '.', '.', '.' }
		}), is (TRUE));
		// Permutation 5 : vertical flip
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', 'Y', 'X' },
			{ '.', '.', '.' },
			{ '.', '.', '.' }
		}), is (TRUE));
		// Permutation 6 : diagonal BT
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', '.', '.' },
			{ '.', '.', 'Y' },
			{ '.', '.', 'X' }
		}), is (TRUE));
		// Permutation 7 : horizontal flip
		assertThat(allPermutations.hasNext(), is(TRUE));
		assertThat(deepEquals(allPermutations.next().getGrid(), new char [][] {
			{ '.', '.', '.' },
			{ '.', '.', '.' },
			{ 'X', 'Y', '.' }
		}), is (TRUE));
		assertThat(allPermutations.hasNext(), is(FALSE));
	}
}
