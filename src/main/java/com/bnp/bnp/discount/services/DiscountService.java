package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import jakarta.annotation.Nullable;

public class DiscountService {

    public void calculatePrice(@Nullable int[] shoppingBasket) {
        if (shoppingBasket==null){
            throw new EmptyBasketException("The basket is empty");
        }
        if (shoppingBasket.length == 0) {
            throw new EmptyBasketException("The basket is empty");
        }
    }
}
