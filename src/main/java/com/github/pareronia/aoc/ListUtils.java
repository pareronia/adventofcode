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
    
    public static <T> List<Integer> indexesOfSubList(final List<T> list, final List<T> subList) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(subList);
        if (list.isEmpty() || subList.isEmpty()) {
            return List.of();
        }
        final List<Integer> ans = new ArrayList<>();
        int p0 = 0;
        while (p0 + subList.size() <= list.size()) {
            final int idx = Collections.indexOfSubList(list.subList(p0, list.size()), subList);
            if (idx == -1) {
                break;
            } else {
                ans.add(p0 + idx);
                p0 += idx + subList.size();
            }
        }
        return ans;
    }
    
    public static <T> List<T> subtractAll(final List<T> list, final List<T> subList) {
        final List<T> ans = new ArrayList<>();
        int i = 0;
        for (final int idx : ListUtils.indexesOfSubList(list, subList)) {
            while (i < idx) {
                ans.add(list.get(i));
                i++;
            }
            i += subList.size();
        }
        ans.addAll(list.subList(i, list.size()));
        return ans;
    }
}
