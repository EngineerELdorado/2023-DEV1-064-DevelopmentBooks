package com.bnp.bnp.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BookUtil {

    private BookUtil() {

    }

    public static Map<Integer, Integer> getBookCounts(int[] shoppingBasket) {
        Map<Integer, Integer> bookCounts = new HashMap<>();
        for (int book : shoppingBasket) {
            bookCounts.put(book, bookCounts.getOrDefault(book, 0) + 1);
        }
        return bookCounts;
    }

    public static Set<Integer> getDistinctBooks(int[] shoppingBasket) {
        Set<Integer> distinctBooks = new HashSet<>();
        for (int book : shoppingBasket) {
            distinctBooks.add(book);
        }
        return distinctBooks;
    }
}
