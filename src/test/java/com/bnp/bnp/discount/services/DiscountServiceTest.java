package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import com.bnp.bnp.basket.exceptions.InvalidBasketException;
import com.bnp.bnp.basket.exceptions.NoBasketException;
import com.bnp.bnp.books.repositories.BookRepository;
import com.bnp.bnp.models.TestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private DiscountService discountService;

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
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When
        double finalPrice = discountService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(50);
    }

    @Test
    void given_only_similar_books_in_the_shopping_basket_when_calculating_the_price_then_apply_no_discount() {
        //Given
        int[] shoppingBasket = {1, 1, 1, 1, 1};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When
        double finalPrice = discountService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(250);
    }

    @Test
    void given_unknown_books_in_the_shopping_basket_then_throw_exception() {
        //Given
        int[] shoppingBasket = {999};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When //Then
        assertThatThrownBy(() -> discountService.calculatePrice(shoppingBasket))
                .isInstanceOf(InvalidBasketException.class)
                .hasMessage("Your basket contains invalid books");
    }

    @Test
    void given_two_distinct_books_in_the_shopping_basket_then_apply_discount_of_10_percent() {
        //Given
        int[] shoppingBasket = {1, 2};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double finalPrice = discountService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(95);
    }

    @Test
    void given_three_distinct_books_in_the_shopping_basket_then_apply_discount_of_10_percent() {
        //Given
        int[] booksIds = {1, 2, 5};

        //When
        double result = discountService.calculatePrice(booksIds);

        //Then
        assertThat(result).isEqualTo(135);
    }
}
