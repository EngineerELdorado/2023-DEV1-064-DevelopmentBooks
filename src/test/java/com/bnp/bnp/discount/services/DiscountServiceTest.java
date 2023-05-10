package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import com.bnp.bnp.basket.exceptions.NoBasketException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DiscountServiceTest {

    DiscountService discountService;

    @BeforeEach
    void setUp() {
        discountService = new DiscountService();
    }

    @Test
    void given_empty_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        //Given
        int[] shoppingBasket = {};

        //When //Then
        assertThatThrownBy(() -> discountService.calculatePrice(shoppingBasket))
                .isInstanceOf(EmptyBasketException.class)
                .hasMessage("The basket is empty");
    }

    @Test
    void given_null_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        //Given //When //Then
        assertThatThrownBy(() -> discountService.calculatePrice(null))
                .isInstanceOf(NoBasketException.class)
                .hasMessage("No basket found");
    }

    @Test
    void given_only_one_book_in_the_shopping_basket_when_calculating_the_price_then_apply_no_discount() {
        //Given
        int[] shoppingBasket = {1};

        //When
        double finalPrice = discountService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(50);
    }
}
