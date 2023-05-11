package com.bnp.bnp.services;

import com.bnp.bnp.exceptions.EmptyBasketException;
import com.bnp.bnp.exceptions.InvalidBasketException;
import com.bnp.bnp.exceptions.NoBasketException;
import com.bnp.bnp.repositories.BookRepository;
import com.bnp.bnp.utils.ShoppingBasketUtil;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PricingService {

    //this is not field injection. Lombok(@RequiredArgsConstructor) will generate a constructor injection for this
    private final BookRepository bookRepository;
    private static final double PRICE_PER_BOOK = 50;

    public double calculatePrice(@Nullable int[] shoppingBasket) {
        validateShoppingBasket(shoppingBasket);

        double price = PRICE_PER_BOOK * shoppingBasket.length;

        Map<Integer, Double> discountPercentages = bookRepository.getDiscountsRates();
        if (shoppingBasket.length == 1) {
            return price;
        }

        Set<Integer> distinctBooks = ShoppingBasketUtil.getDistinctBooks(shoppingBasket);
        int numberOfDistinctBooks = distinctBooks.size();

        if (numberOfDistinctBooks == 1) {
            return price;
        }

        Map<Integer, Integer> bookCounts = ShoppingBasketUtil.getBookCounts(shoppingBasket);
        double discountAmountForDistinctBooks = 0;

        while (numberOfDistinctBooks > 1) {
            double discountPercentage = discountPercentages.getOrDefault(numberOfDistinctBooks, 0.0);
            int minCountThatQualifiesForDiscount = Integer.MAX_VALUE;

            /*
             * LOGIC EXPLANATION:
             * 1. For each distinct book from the map we get its value (its count)
             * 2. Check if the count is greater than 0(if there is still the same book in the basket).
             * 3. We see if this count is less than what was saved previously.
             * 4. If Yes then we update the min count that qualifies for discount.
             * 5. We return the min count that qualifies for discount.
             */
            for (int count : bookCounts.values()) {
                if (count > 0 && count < minCountThatQualifiesForDiscount) {
                    minCountThatQualifiesForDiscount = count;
                }
            }

            discountAmountForDistinctBooks = discountAmountForDistinctBooks +
                    (numberOfDistinctBooks * minCountThatQualifiesForDiscount *
                            PRICE_PER_BOOK * discountPercentage);
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
        return price - discountAmountForDistinctBooks;
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
