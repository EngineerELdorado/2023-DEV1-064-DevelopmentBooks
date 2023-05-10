package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import com.bnp.bnp.basket.exceptions.NoBasketException;
import jakarta.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

public class DiscountService {

    private static final double PRICE_PER_BOOK = 50;

    public double calculatePrice(@Nullable int[] shoppingBasket) {
        validateShoppingBasket(shoppingBasket);

        if (shoppingBasket.length == 1) {
            return PRICE_PER_BOOK;
        }

        Set<Integer> distinctBooksMap = new HashSet<>();
        for (int book : shoppingBasket) {
            distinctBooksMap.add(book);
        }

        if (distinctBooksMap.size() == 1) {
            return PRICE_PER_BOOK * shoppingBasket.length;
        }
        return 0;
    }

    private static void validateShoppingBasket(int[] shoppingBasket) {
        if (shoppingBasket == null) {
            throw new NoBasketException("No basket found");
        }
        if (shoppingBasket.length == 0) {
            throw new EmptyBasketException("The basket is empty");
        }
    }
}
