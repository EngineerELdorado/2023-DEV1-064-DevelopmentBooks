package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscountServiceTest {

    @Test
    void given_empty_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        DiscountService discountService = new DiscountService();

        //Given
        int[] shoppingBasket = {};

        //When //Then
        assertThatThrownBy(() -> discountService.calculatePrice(shoppingBasket))
                .isInstanceOf(EmptyBasketException.class)
                .hasMessage("The basket is empty");
    }

    @Test
    void given_null_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        DiscountService discountService = new DiscountService();

        //Given //When //Then
        assertThatThrownBy(() -> discountService.calculatePrice(null))
                .isInstanceOf(EmptyBasketException.class)
                .hasMessage("The basket is empty");
    }
}
