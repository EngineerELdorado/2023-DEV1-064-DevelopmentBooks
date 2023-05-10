package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;

public class DiscountService {

    public void calculatePrice(int[] shoppingBasket) {
        if (shoppingBasket.length == 0) {
            throw new EmptyBasketException("The basket is empty");
        }
    }
}
