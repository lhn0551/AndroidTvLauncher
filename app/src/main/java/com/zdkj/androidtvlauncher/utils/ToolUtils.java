package com.zdkj.androidtvlauncher.utils;

import java.util.List;

public class ToolUtils {
    public static <E> boolean isListEqual(List<E> list1, List<E> list2) {
        if (list1 == list2) {
            return true;
        }
        if ((list1 == null && list2 != null && list2.size() == 0)
                || (list2 == null && list1 != null && list1.size() == 0)) {
            return true;
        }
        if (list1.size() != list2.size()) {
            return false;
        }
        return list1.containsAll(list2);
    }
}
