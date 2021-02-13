package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class OCRTestCase {

	@Test
	public void test() {
		final Grid grid = Grid.from(asList(
			" **  ***   **  **** ****  **  *  *  ***   ** *  * *     **  ***  ***   *** *  * *   ***** ",
			"*  * *  * *  * *    *    *  * *  *   *     * * *  *    *  * *  * *  * *    *  * *   *   * ",
			"*  * ***  *    ***  ***  *    ****   *     * **   *    *  * *  * *  * *    *  *  * *   *  ",
			"**** *  * *    *    *    * ** *  *   *     * * *  *    *  * ***  ***   **  *  *   *   *   ",
			"*  * *  * *  * *    *    *  * *  *   *  *  * * *  *    *  * *    * *     * *  *   *  *    ",
			"*  * ***   **  **** *     *** *  *  ***  **  *  * ****  **  *    *  * ***   **    *  **** "
        ));
		
		final String result = OCR.convert6(grid, '*', ' ');
		
		assertThat(result, is("ABCEFGHIJKLOPRSUYZ"));
	}
}
