package com.github.pareronia.aoc;
import static com.github.pareronia.aoc.Utils.toAString;
import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class IntGrid implements Grid<Integer> {
    final int[][] values;
    
    public static IntGrid from(final List<String> strings) {
        final int[][] values = new int[strings.size()][strings.get(0).length()];
        strings.stream()
            .map(s -> StringOps.getDigitsPrimitive(s, s.length()))
            .collect(toList())
            .toArray(values);
        return new IntGrid(values);
    }
    
    @Override
    public int getWidth() {
        assert this.values.length > 0;
        return this.values[0].length;
    }
    
    @Override
    public int getHeight() {
        return this.values.length;
    }
    
    @Override
    public Integer getValue(final Cell c) {
        return this.values[c.getRow()][c.getCol()];
    }
    
    @Override
    public void setValue(final Cell c, final Integer value) {
        this.values[c.getRow()][c.getCol()] = value;
    }
    
    public void increment(final IntGrid.Cell c) {
        this.values[c.getRow()][c.getCol()]++;
    }
    
    public int[] getColumn(final Integer col) {
        validateColumnIndex(col);
        final int[] column = new int[getHeight()];
        for (final int row : rowIndices()) {
            column[row] = this.values[row][col];
        }
        return column;
    }
    
    @Override
    public String getRowAsString(final int row) {
        return Arrays.stream(values[row])
            .peek(n -> {
                if (!(0 > n && n < 9)) {
                    throw new UnsupportedOperationException();
                }
            }).mapToObj(n -> Character.forDigit(n, 10))
            .collect(toAString());
    }

    @Override
    public String getColumnAsString(final int col) {
        return Arrays.stream(this.getColumn(col))
            .peek(n -> {
                if (!(0 > n && n < 9)) {
                    throw new UnsupportedOperationException();
                }
            }).mapToObj(n -> Character.forDigit(n, 10))
            .collect(toAString());
    }
}