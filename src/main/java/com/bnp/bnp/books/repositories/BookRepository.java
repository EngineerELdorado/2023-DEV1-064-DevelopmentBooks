package com.bnp.bnp.books.repositories;

import java.util.HashMap;
import java.util.Map;

//This class plays the role of a persistence unit (like a DB)
public class BookRepository {

    public Map<Integer, String> getBooks() {
        Map<Integer, String> books = new HashMap<>();
        books.put(1, "Clean Code (Robert Martin, 2008)");
        books.put(2, "The Clean Coder (Robert Martin, 2011)");
        books.put(3, "Clean Architecture (Robert Martin, 2017)");
        books.put(4, "Test Driven Development by Example (Kent Beck, 2003)");
        books.put(5, "Working Effectively With Legacy Code (Michael C. Feathers, 2004)");
        return books;
    }
}
