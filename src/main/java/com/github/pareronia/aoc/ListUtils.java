package com.github.pareronia.aoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ListUtils {

    public static <T> List<T> reversed(final List<T> list) {
        final List<T> ans = new ArrayList<>(Objects.requireNonNull(list));
        Collections.reverse(ans);
        return ans;
    }
}
