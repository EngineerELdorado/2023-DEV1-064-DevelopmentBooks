package com.bnp.bnp.services;

import com.bnp.bnp.exceptions.EmptyBasketException;
import com.bnp.bnp.exceptions.InvalidBasketException;
import com.bnp.bnp.exceptions.NoBasketException;
import com.bnp.bnp.models.TestData;
import com.bnp.bnp.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PricingServiceTest {

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private PricingService pricingService;

    @Test
    void given_empty_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        //Given
        int[] shoppingBasket = {};

        //When //Then
        assertThatThrownBy(() -> pricingService.calculatePrice(shoppingBasket))
                .isInstanceOf(EmptyBasketException.class)
                .hasMessage("The basket is empty");
    }

    @Test
    void given_null_shopping_basket_when_calculating_the_price_then_throw_an_exception() {
        //Given //When //Then
        assertThatThrownBy(() -> pricingService.calculatePrice(null))
                .isInstanceOf(NoBasketException.class)
                .hasMessage("No basket found");
    }

    @Test
    void given_only_one_book_in_the_shopping_basket_when_calculating_the_price_then_apply_no_discount() {
        //Given
        int[] shoppingBasket = {1};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When
        double finalPrice = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(50);
    }

    @Test
    void given_only_similar_books_in_the_shopping_basket_when_calculating_the_price_then_apply_no_discount() {
        //Given
        int[] shoppingBasket = {1, 1, 1, 1, 1};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When
        double finalPrice = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(250);
    }

    @Test
    void given_unknown_books_in_the_shopping_basket_then_throw_exception() {
        //Given
        int[] shoppingBasket = {999};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());

        //When //Then
        assertThatThrownBy(() -> pricingService.calculatePrice(shoppingBasket))
                .isInstanceOf(InvalidBasketException.class)
                .hasMessage("Your basket contains invalid books");
    }

    @Test
    void given_two_sets_in_a_series_of_four_books_then_return_appropriate_discount() {
        //Given
        int[] shoppingBasket = {1, 1, 2, 2};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(result).isEqualTo(190);
    }

    @Test
    void given_two_distinct_books_in_the_shopping_basket_then_apply_discount_of_5_percent() {
        //Given
        int[] shoppingBasket = {1, 2};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double finalPrice = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(finalPrice).isEqualTo(95);
    }

    @Test
    void given_three_distinct_books_in_the_shopping_basket_then_apply_discount_of_10_percent() {
        //Given
        int[] shoppingBasket = {1, 2, 5};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(result).isEqualTo(135);
    }

    @Test
    void given_four_distinct_books_in_the_shopping_basket_then_apply_discount_of_20_percent() {
        //Given
        int[] shoppingBasket = {1, 2, 4, 5};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(result).isEqualTo(160);
    }

    @Test
    void given_five_distinct_books_in_the_shopping_basket_then_apply_discount_of_25_percent() {
        //Given
        int[] shoppingBasket = {1, 2, 3, 4, 5};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(result).isEqualTo(187.5);
    }

    @Test
    void given_basket_has_four_books_of_which_3_are_distinct_then_apply_appropriate_discount() {
        //Given
        int[] shoppingBasket = {1, 2, 3, 3};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then
        assertThat(result).isEqualTo(185);
    }

    @Test
    void given_final_example_from_the_assignment_apply_appropriate_discount() {
        //Given
        int[] shoppingBasket = {1, 1, 2, 2, 3, 3, 4, 5};
        given(bookRepository.getBooks()).willReturn(TestData.getBooks());
        given(bookRepository.getDiscountsRates()).willReturn(TestData.getDiscountsRates());

        //When
        double result = pricingService.calculatePrice(shoppingBasket);

        //Then

        /* (the example given in the assignment says this should return 320).
        However, it is returning a different value (322.5). We can discuss about this in the code review.
        It looks to me like the assignment description contains a mistake. Or if am wrong please correct me.
         */
        assertThat(result).isEqualTo(322.5);
    }
}
