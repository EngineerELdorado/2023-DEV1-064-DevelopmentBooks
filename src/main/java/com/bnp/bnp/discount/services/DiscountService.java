package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import com.bnp.bnp.basket.exceptions.InvalidBasketException;
import com.bnp.bnp.basket.exceptions.NoBasketException;
import com.bnp.bnp.books.repositories.BookRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DiscountService {

    //this is not field injection. Lombok(@RequiredArgsConstructor) will generate a constructor for this
    private final BookRepository bookRepository;
    private static final double PRICE_PER_BOOK = 50;

    public double calculatePrice(@Nullable int[] shoppingBasket) {
        validateShoppingBasket(shoppingBasket);

        double totalCost = PRICE_PER_BOOK * shoppingBasket.length;

        Map<Integer, Double> discountPercentages = bookRepository.getDiscountsRates();
        if (shoppingBasket.length == 1) {
            return totalCost;
        }

        Set<Integer> distinctBooks = getDistinctBooks(shoppingBasket);
        int numberOfDistinctBooks = distinctBooks.size();

        if (numberOfDistinctBooks == 1) {
            return totalCost;
        }

        Map<Integer, Integer> bookCounts = getBookCounts(shoppingBasket);

        double discountForDistinctBooks = 0;

        //As long as we have more than one distinct book we need to apply discount again for them
        while (numberOfDistinctBooks > 1) {

            int minCountThatQualifiesForDiscount = Integer.MAX_VALUE;

            /*
             * LOGIC EXPLANATION:
             * 1. For each distinct book from the map we get its value (its count)
             * 2. Check if the count is greater than 0. Meaning if there is a copy remaining on the basket for this book.
             * 3. We see if this count is less than what was saved previously as the min count that qualifies for discount.
             * 4. If Yes then we update the min count that qualifies for discount.
             * 5. We return the min count that qualifies for discount.
             */
            for (int count : bookCounts.values()) {
                if (count > 0 && count < minCountThatQualifiesForDiscount) {
                    minCountThatQualifiesForDiscount = count;
                }
            }
            double discountPercentage = discountPercentages.getOrDefault(numberOfDistinctBooks, 0.0);

            discountForDistinctBooks = discountForDistinctBooks +
                    (numberOfDistinctBooks * minCountThatQualifiesForDiscount * PRICE_PER_BOOK * discountPercentage);

            /*
            Check if the book counts still have values that are greater than the current min count
            If yes then reduce the count (because we will process the discount for it too).
            This allows us to remove distinct books that are already been processed for discount.
             */
            for (Map.Entry<Integer, Integer> entry : bookCounts.entrySet()) {
                int count = entry.getValue();
                if (count >= minCountThatQualifiesForDiscount) {
                    entry.setValue(count - minCountThatQualifiesForDiscount);
                }
            }

            bookCounts.values().removeIf(count -> count == 0);
            numberOfDistinctBooks = bookCounts.keySet().size();
        }
        return totalCost - discountForDistinctBooks;
    }

    private static Map<Integer, Integer> getBookCounts(int[] shoppingBasket) {
        Map<Integer, Integer> bookCounts = new HashMap<>();
        for (int book : shoppingBasket) {
            bookCounts.put(book, bookCounts.getOrDefault(book, 0) + 1);
        }
        return bookCounts;
    }

    private static Set<Integer> getDistinctBooks(int[] shoppingBasket) {
        Set<Integer> distinctBooks = new HashSet<>();
        for (int book : shoppingBasket) {
            distinctBooks.add(book);
        }
        return distinctBooks;
    }

    private void validateShoppingBasket(int[] shoppingBasket) {
        if (shoppingBasket == null) {
            throw new NoBasketException("No basket found");
        }

        Map<Integer, String> books = bookRepository.getBooks();

        for (int id : shoppingBasket) {
            if (!books.containsKey(id)) {
                throw new InvalidBasketException("Your basket contains invalid books");
            }
        }

        if (shoppingBasket.length == 0) {
            throw new EmptyBasketException("The basket is empty");
        }
    }
}
