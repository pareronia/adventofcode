package com.github.pareronia.aoc;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
		
		assertThat(result).isEqualTo("ABCEFGHIJKLOPRSUYZ");
	}
}
