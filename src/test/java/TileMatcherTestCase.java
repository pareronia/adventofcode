import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.stream.Collectors.toCollection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.Test;

public class TileMatcherTestCase {
	
	private final AoC2020_20.Tile tile1 = new AoC2020_20.Tile(
		1,
		new char[][] {
			{ '.', '.', 'X' },
			{ '.', '.', 'Y' },
			{ '.', '.', 'Z' }
		}
	);
	private final AoC2020_20.Tile tile2 = new AoC2020_20.Tile(
		2,
		new char[][] {
			{ '.', '.', '.' },
			{ '.', '.', '.' },
			{ 'A', 'B', 'C' }
		}
	);
	private final Set<AoC2020_20.Tile> tiles = Stream.of(tile1, tile2)
			.collect(toCollection(HashSet::new));

	@Test
	public void testRightSide1() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', 'X' },
				{ '.', '.', 'Y' },
				{ '.', '.', 'Z' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findRightSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile1));
	}
	
	@Test
	public void testRightSide2() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', 'Z' },
				{ '.', '.', 'Y' },
				{ '.', '.', 'X' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findRightSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile1));
	}

	@Test
	public void testRightSide3() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', 'C' },
				{ '.', '.', 'B' },
				{ '.', '.', 'A' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findRightSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile2));
	}
		
	@Test
	public void testRightSideNoMatch() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', 'G' },
				{ '.', '.', 'G' },
				{ '.', '.', 'G' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findRightSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(FALSE));
	}

	@Test
	public void testBottomSide1() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', '.' },
				{ '.', '.', '.' },
				{ 'X', 'Y', 'Z' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findBottomSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile1));
	}
	
	@Test
	public void testBottomSide2() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', '.' },
				{ '.', '.', '.' },
				{ 'Z', 'Y', 'X' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findBottomSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile1));
	}
	
	@Test
	public void testBottomSide3() {
		final AoC2020_20.Tile tile = new AoC2020_20.Tile(
			0,
			new char[][] {
				{ '.', '.', '.' },
				{ '.', '.', '.' },
				{ 'C', 'B', 'A' }
			}
		);
		
		final Optional<AoC2020_20.Tile> match = AoC2020_20.TileMatcher.findBottomSideMatch(tile, tiles);
		assertThat(match.isPresent(), is(TRUE));
		assertThat(match.get(), is(tile2));
	}
}
