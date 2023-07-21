package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.github.pareronia.aoc.Grid.Cell;

public class GridTestCase {

	@Test
	public void getRow_negativeIndex() {
		assertThatThrownBy(() -> Grid.from(asList("123")).getRow(-1))
		    .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void getRow_tooLargeIndex() {
		assertThatThrownBy(() -> Grid.from(asList("123")).getRow(1))
		    .isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void getRow() {
		final char[] result = Grid.from(asList("123")).getRow(0);
		
		assertThat(result).isEqualTo(new char[] {'1', '2', '3'});
	}

	@Test
	public void getRowReversed() {
		final char[] result = Grid.from(asList("123")).getRowReversed(0);
		
		assertThat(result).isEqualTo(new char[] {'3', '2', '1'});
	}
	
	@Test
	public void getColumn() {
		final char[] result = Grid.from(asList("123", "456")).getColumn(1);
		
		assertThat(result).isEqualTo(new char[] {'2', '5'});
	}
	
	@Test
	public void getColumnReversed() {
		final char[] result = Grid.from(asList("123", "456")).getColumnReversed(1);
		
		assertThat(result).isEqualTo(new char[] {'5', '2'});
	}
	
	@Test
	public void getRowAsString() {
		final String result = Grid.from(asList("123")).getRowAsString(0);
		
		assertThat(result).isEqualTo("123");
	}

	@Test
	public void getRowsAsString() {
		final Grid grid = Grid.from(asList("123", "456", "789"));
		
		final Iterable<String> result = grid.getRowsAsStrings();
		
		assertThat(result).containsExactly("123", "456", "789");
	}
	
	@Test
	public void getLeftEdge() {
		final Grid grid = Grid.from(asList(	"123",
											"456",
											"789"));
		
		final char[] result = grid.getLeftEdge();
		
		assertThat(result).isEqualTo(new char[] {'1', '4', '7'});
	}
	
	@Test
	public void getRightEdge() {
		final Grid grid = Grid.from(asList(	"123",
											"456",
											"789"));
		
		final char[] result = grid.getRightEdge();
		
		assertThat(result).isEqualTo(new char[] {'3', '6', '9'});
	}

	@Test
	public void getWithEdgesRemoved() {
		final Grid grid = Grid.from(asList(	"ABCD",
											"EFGH",
											"IJKL",
											"MNOP",
											"QRTS"));
				
		final Grid result = grid.getWithEdgesRemoved();
		
		assertThat(asStringList(result)).containsExactly( "FG",
													      "JK",
													      "NO");
	}
	
	@Test
	public void rollColumn() {
		final Grid grid = Grid.from(asList(	"###....",
											"###....",
											"......."));
		final Grid result = grid.rollColumn(1, 1);

		assertThat(asStringList(result)).containsExactly( "#.#....",
													      "###....",
													      ".#.....");
	}
	
	@Test
	public void rollRow() {
		final Grid grid = Grid.from(asList(	"#.#....",
											"###....",
											".#....."));
		final Grid result = grid.rollRow(0, 4);

		assertThat(asStringList(result)).containsExactly( "....#.#",
													      "###....",
													      ".#.....");
	}
	
	@Test
	public void rollRow1() {
	    final Grid grid = Grid.from(asList(	"#.#...."));
	    
	    final Grid result = grid.rollRow(0, 1);
	    
	    assertThat(asStringList(result)).containsExactly(".#.#...");
	}
	
	@Test
	public void rotate() {
		final Grid grid = Grid.from(asList(	"123",
											"456",
											"789"));
		
		final Grid result = grid.rotate();
		
		assertThat(asStringList(result)).containsExactly( "741",
													      "852",
													      "963");
	}
	
	@Test
	public void flipHorizontally() {
		final Grid grid = Grid.from(asList(	"123",
											"456",
											"789"));
		
		final Grid result = grid.flipHorizontally();
		
		assertThat(asStringList(result)).containsExactly( "789",
													      "456",
													      "123");
	}
	
	private List<String> asStringList(final Grid grid) {
	    return grid.getRowsAsStringList();
	}
	
	@Test
	public void getPermutations_notSquare() {
		final Grid grid = Grid.from(asList(	"123A",
											"456B",
											"789C"));
		
		assertThatThrownBy(() -> grid.getPermutations())
		    .isInstanceOf(UnsupportedOperationException.class);
	}

	@Test
	public void getPermutations() {
		final Grid grid = Grid.from(asList(	"123",
											"456",
											"789"));
		
		final Iterator<Grid> result = grid.getPermutations();
		
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
		final Grid grid = Grid.from(asList(	"XOX",
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
		final Grid grid = Grid.from(asList(	"XOX",
											"OXO",
											"XOX"));
		
		assertThat(grid.countAllEqualTo('X')).isEqualTo(5L);
		assertThat(grid.countAllEqualTo('O')).isEqualTo(4L);
		assertThat(grid.countAllEqualTo('.')).isEqualTo(0L);
	}
	
	@Test
	public void replace() {
		final Grid grid = Grid.from(asList(	"XOX", "OXO", "XOX"));
		
		final Grid result = grid.replace('X', 'Y');
		
		assertThat(asStringList(result)).containsExactly("YOY", "OYO", "YOY");
	}
	
	@Test
    public void update() {
        final Grid grid = Grid.from(asList( "XOX", "OXO", "XOX"));
        
        final Grid result = grid.update(Set.of(Cell.at(0, 0), Cell.at(2, 2)), '-');
        
        assertThat(asStringList(result)).containsExactly("-OX", "OXO", "XO-");
    }
	
	@Test
	public void subGrid_invalidCoordinates() {
		final Grid grid = Grid.from(asList(	"ABCD",
											"EFGH",
											"IJKL",
											"MNOP",
											"QRST"));
		
		assertThatThrownBy(() -> grid.subGrid(Cell.at(3, 3), Cell.at(1, 1)))
		    .isInstanceOf(IllegalArgumentException.class);
	}
	
	@Test
	public void subGrid() {
		final Grid grid = Grid.from(asList(	"ABCD",
											"EFGH",
											"IJKL",
											"MNOP",
											"QRST"));
		
		final Grid result1 = grid.subGrid(Cell.at(1, 1), Cell.at(4, 4));
		final Grid result2 = grid.subGrid(Cell.at(0, 0), Cell.at(0, 0));
		final Grid result3 = grid.subGrid(Cell.at(1, 1), Cell.at(5, 5));

		assertThat(asStringList(result1)).containsExactly("FGH", "JKL", "NOP");
		assertThat(asStringList(result2)).isEmpty();
		assertThat(asStringList(result3)).containsExactly("FGH", "JKL", "NOP", "RST");
	}
	
	@Test
	public void addRowInvalid() {
	    final Grid grid = Grid.from(asList("ABCD"));
	    
		assertThatThrownBy(() -> grid.addRow("ABC"))
		    .isInstanceOf(IllegalArgumentException.class);
	    
	}

	@Test
	public void addRow() {
	    final Grid grid = Grid.from(asList("ABCD"));
	    
	    final Grid result = grid.addRow("EFGH");

		assertThat(asStringList(result)).containsExactly("ABCD", "EFGH");
	}
	
	@Test
	public void getCellsN() {
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
		final Grid grid = Grid.from(asList(	"ABCD",
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
	public void divide() {
		final Grid grid = Grid.from(asList(	"ABCD",
											"EFGH",
											"IJKL",
											"MNOP"));
	    
		final Grid[][] result = grid.divide(2);
		
		assertThat(result).hasDimensions(2, 2);
		assertThat(asStringList(result[0][0])).containsExactly("AB", "EF");
		assertThat(asStringList(result[0][1])).containsExactly("CD", "GH");
		assertThat(asStringList(result[1][0])).containsExactly("IJ", "MN");
		assertThat(asStringList(result[1][1])).containsExactly("KL", "OP");
	}
	
	@Test
	public void merge() {
	    final Grid grid1 = Grid.from(asList("AB",
	                                        "EF"));
	    final Grid grid2 = Grid.from(asList("CD",
	                                        "GH"));
	    final Grid grid3 = Grid.from(asList("IJ",
	                                        "MN"));
	    final Grid grid4 = Grid.from(asList("KL",
	                                        "OP"));
	    final Grid grid5 = Grid.from(asList("XXX",
	                                        "XXX"));
	    
	    final Grid result =
	            Grid.merge(new Grid[][] { { grid1, grid2 }, { grid3, grid4 } });
	    
	    assertThat(asStringList(result)).containsExactly(  "ABCD",
	                                                       "EFGH",
	                                                       "IJKL",
	                                                       "MNOP");

	    try {
            Grid.merge(new Grid[][] { { grid1, grid2 }, { grid3, grid5 } });
            fail("Expected IllegalArgumentException");
        } catch (final IllegalArgumentException expected) {
        }
	}
}
