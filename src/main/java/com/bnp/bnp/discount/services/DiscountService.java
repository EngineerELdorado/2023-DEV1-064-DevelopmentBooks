package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import jakarta.annotation.Nullable;

public class DiscountService {

    private static final double PRICE_PER_BOOK = 50;

    public double calculatePrice(@Nullable int[] shoppingBasket) {
        if (shoppingBasket == null) {
            throw new EmptyBasketException("The basket is empty");
        }
        if (shoppingBasket.length == 0) {
            throw new EmptyBasketException("The basket is empty");
        }
        if (shoppingBasket.length == 1) {
            return PRICE_PER_BOOK;
        }
        return 0;
    }
}
