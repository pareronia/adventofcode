package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.Grid.Cell;

public class GridTestCase {

	@Test
	public void getRow_negativeIndex() {
		assertThatThrownBy(() -> CharGrid.from(asList("123")).getRow(-1))
		    .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void getRow_tooLargeIndex() {
		assertThatThrownBy(() -> CharGrid.from(asList("123")).getRow(1))
		    .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void getRow() {
		final char[] result = CharGrid.from(asList("123")).getRow(0);
		
		assertThat(result).isEqualTo(new char[] {'1', '2', '3'});
	}

	@Test
	public void getRowReversed() {
		final char[] result = CharGrid.from(asList("123")).getRowReversed(0);
		
		assertThat(result).isEqualTo(new char[] {'3', '2', '1'});
	}
	
	@Test
	public void getColumn() {
		final char[] result = CharGrid.from(asList("123", "456")).getColumn(1);
		
		assertThat(result).isEqualTo(new char[] {'2', '5'});
	}
	
	@Test
	public void getColumnReversed() {
		final char[] result = CharGrid.from(asList("123", "456")).getColumnReversed(1);
		
		assertThat(result).isEqualTo(new char[] {'5', '2'});
	}
	
	@Test
	public void getRowAsString() {
		final String result = CharGrid.from(asList("123")).getRowAsString(0);
		
		assertThat(result).isEqualTo("123");
	}

	@Test
	public void getRowsAsString() {
		final CharGrid grid = CharGrid.from(asList("123", "456", "789"));
		
		final Stream<String> result = grid.getRowsAsStrings();
		
		assertThat(result).containsExactly("123", "456", "789");
	}
	
	@Test
	public void getLeftEdge() {
		final CharGrid grid = CharGrid.from(asList(	"123",
													"456",
													"789"));
		
		final char[] result = grid.getLeftEdge();
		
		assertThat(result).isEqualTo(new char[] {'1', '4', '7'});
	}
	
	@Test
	public void getRightEdge() {
		final CharGrid grid = CharGrid.from(asList(	"123",
													"456",
													"789"));
		
		final char[] result = grid.getRightEdge();
		
		assertThat(result).isEqualTo(new char[] {'3', '6', '9'});
	}

	@Test
	public void getWithEdgesRemoved() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRTS"));
				
		final CharGrid result = grid.getWithEdgesRemoved();
		
		assertThat(asStringList(result)).containsExactly( "FG",
													      "JK",
													      "NO");
	}
	
	@Test
	public void rollColumn() {
		final CharGrid grid = CharGrid.from(asList(	"###....",
													"###....",
													"......."));
		final CharGrid result = grid.rollColumn(1, 1);

		assertThat(asStringList(result)).containsExactly( "#.#....",
													      "###....",
													      ".#.....");
	}
	
	@Test
	public void rollRow() {
		final CharGrid grid = CharGrid.from(asList(	"#.#....",
													"###....",
													".#....."));
		final CharGrid result = grid.rollRow(0, 4);

		assertThat(asStringList(result)).containsExactly( "....#.#",
													      "###....",
													      ".#.....");
	}
	
	@Test
	public void rollRow1() {
	    final CharGrid grid = CharGrid.from(asList(	"#.#...."));
	    
	    final CharGrid result = grid.rollRow(0, 1);
	    
	    assertThat(asStringList(result)).containsExactly(".#.#...");
	}
	
	@Test
	public void rotate() {
		final CharGrid grid = CharGrid.from(asList(	"123",
													"456",
													"789"));
		
		final CharGrid result = grid.rotate();
		
		assertThat(asStringList(result)).containsExactly( "741",
													      "852",
													      "963");
	}
	
	@Test
	public void flipHorizontally() {
		final CharGrid grid = CharGrid.from(asList(	"123",
													"456",
													"789"));
		
		final CharGrid result = grid.flipHorizontally();
		
		assertThat(asStringList(result)).containsExactly( "789",
													      "456",
													      "123");
	}
	
	private List<String> asStringList(final CharGrid grid) {
	    return grid.getRowsAsStringList();
	}
	
	@Test
	public void getPermutations_notSquare() {
		final CharGrid grid = CharGrid.from(asList(	"123A",
													"456B",
													"789C"));
		
		assertThatThrownBy(() -> grid.getPermutations())
		    .isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void getPermutations() {
		final CharGrid grid = CharGrid.from(asList(	"123",
													"456",
													"789"));
		
		final Iterator<CharGrid> result = grid.getPermutations();
		
		// Permutation 0 : original
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "123",
															      "456",
															      "789");
		// Permutation 1 : rotated 90
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "741",
															      "852",
															      "963");
		// Permutation 2 : rotated 180
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "987",
															      "654",
															      "321");
		// Permutation 3 : rotated 270
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "369",
															      "258",
		                                                          "147");
		// Permutation 4 : diagonal TB
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "147",
															      "258",
															      "369");
		// Permutation 5 : vertical flip
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "321",
															      "654",
															      "987");
		// Permutation 6 : diagonal BT
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "963",
															      "852",
															      "741");
		// Permutation 7 : horizontal flip
		assertThat(result.hasNext()).isTrue();
		assertThat(asStringList(result.next())).containsExactly(  "789",
															      "456",
															      "123");
		assertThat(result.hasNext()).isFalse();
	}
	
	@Test
	public void getAllEqualTo() {
		final CharGrid grid = CharGrid.from(asList(	"XOX",
													"OXO",
													"XOX"));
		
		assertThat(grid.getAllEqualTo('X')).containsExactly(
				            Cell.at(0,  0), Cell.at(0, 2),
							Cell.at(1,  1),
							Cell.at(2,  0), Cell.at(2, 2));
		assertThat(grid.getAllEqualTo('.')).isEmpty();
	}

	@Test
	public void countAllEqualTo() {
		final CharGrid grid = CharGrid.from(asList(	"XOX",
													"OXO",
													"XOX"));
		
		assertThat(grid.countAllEqualTo('X')).isEqualTo(5L);
		assertThat(grid.countAllEqualTo('O')).isEqualTo(4L);
		assertThat(grid.countAllEqualTo('.')).isEqualTo(0L);
	}
	
	@Test
	public void replace() {
		final CharGrid grid = CharGrid.from(asList(	"XOX", "OXO", "XOX"));
		
		final CharGrid result = grid.replace('X', 'Y');
		
		assertThat(asStringList(result)).containsExactly("YOY", "OYO", "YOY");
	}
	
	@Test
    public void update() {
        final CharGrid grid = CharGrid.from(asList( "XOX", "OXO", "XOX"));
        
        final CharGrid result = grid.update(Set.of(Cell.at(0, 0), Cell.at(2, 2)), '-');
        
        assertThat(asStringList(result)).containsExactly("-OX", "OXO", "XO-");
    }
	
	@Test
	public void subGrid_invalidCoordinates() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
		
		assertThatThrownBy(() -> grid.subGrid(Cell.at(3, 3), Cell.at(1, 1)))
		    .isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void subGrid() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
		
		final CharGrid result1 = grid.subGrid(Cell.at(1, 1), Cell.at(4, 4));
		final CharGrid result2 = grid.subGrid(Cell.at(0, 0), Cell.at(0, 0));
		final CharGrid result3 = grid.subGrid(Cell.at(1, 1), Cell.at(5, 5));

		assertThat(asStringList(result1)).containsExactly("FGH", "JKL", "NOP");
		assertThat(asStringList(result2)).isEmpty();
		assertThat(asStringList(result3)).containsExactly("FGH", "JKL", "NOP", "RST");
	}
	
	@Test
	public void addRowInvalid() {
	    final CharGrid grid = CharGrid.from(asList("ABCD"));
	    
		assertThatThrownBy(() -> grid.addRow("ABC"))
		    .isInstanceOf(IllegalArgumentException.class);
	    
	}

	@Test
	public void addRow() {
	    final CharGrid grid = CharGrid.from(asList("ABCD"));
	    
	    final CharGrid result = grid.addRow("EFGH");

		assertThat(asStringList(result)).containsExactly("ABCD", "EFGH");
	}
	
	@Test
	public void getCellsN() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
		final List<Cell> result1 = grid.getCellsN(Cell.at(2, 1)).collect(toList());
		final List<Cell> result2 = grid.getCellsN(Cell.at(0, 2)).collect(toList());
		
		assertThat(result1).containsExactly(Cell.at(1, 1), Cell.at(0, 1));
		assertThat(result2).isEmpty();
	}

	@Test
	public void getCellsS() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsS(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsS(Cell.at(4, 0)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(3, 1), Cell.at(4, 1));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsE() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsE(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsE(Cell.at(1, 3)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(2, 2), Cell.at(2, 3));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsW() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsW(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsW(Cell.at(1, 0)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(2, 0));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsNW() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsNW(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsNW(Cell.at(0, 0)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(1, 0));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsNE() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsNE(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsNE(Cell.at(0, 3)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(1, 2), Cell.at(0, 3));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsSE() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsSE(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsSE(Cell.at(4, 3)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(3, 2), Cell.at(4, 3));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCellsSW() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
	    
	    final List<Cell> result1 = grid.getCellsSW(Cell.at(2, 1)).collect(toList());
	    final List<Cell> result2 = grid.getCellsSW(Cell.at(4, 0)).collect(toList());
	    
		assertThat(result1).containsExactly(Cell.at(3, 0));
		assertThat(result2).isEmpty();
	}
	
	@Test
	public void getCells() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP",
													"QRST"));
		final String result
		    = grid.getCells().map(grid::getValue).collect(Utils.toAString());
		
		assertThat(result).isEqualTo("ABCDEFGHIJKLMNOPQRST");
	}
	
	@Test
	public void divide() {
		final CharGrid grid = CharGrid.from(asList(	"ABCD",
													"EFGH",
													"IJKL",
													"MNOP"));
	    
		final CharGrid[][] result = grid.divide(2);
		
		assertThat(result).hasDimensions(2, 2);
		assertThat(asStringList(result[0][0])).containsExactly("AB", "EF");
		assertThat(asStringList(result[0][1])).containsExactly("CD", "GH");
		assertThat(asStringList(result[1][0])).containsExactly("IJ", "MN");
		assertThat(asStringList(result[1][1])).containsExactly("KL", "OP");
	}
	
	@Test
	public void merge() {
	    final CharGrid grid1 = CharGrid.from(asList("AB",
	    		                                    "EF"));
	    final CharGrid grid2 = CharGrid.from(asList("CD",
	    		                                    "GH"));
	    final CharGrid grid3 = CharGrid.from(asList("IJ",
	    		                                    "MN"));
	    final CharGrid grid4 = CharGrid.from(asList("KL",
	    		                                    "OP"));
	    final CharGrid grid5 = CharGrid.from(asList("XXX",
	    		                                    "XXX"));
	    
	    final CharGrid result =
	            CharGrid.merge(new CharGrid[][] { { grid1, grid2 }, { grid3, grid4 } });
	    
	    assertThat(asStringList(result)).containsExactly(  "ABCD",
	                                                       "EFGH",
	                                                       "IJKL",
	                                                       "MNOP");

	    try {
            CharGrid.merge(new CharGrid[][] { { grid1, grid2 }, { grid3, grid5 } });
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
        }
	}
}
