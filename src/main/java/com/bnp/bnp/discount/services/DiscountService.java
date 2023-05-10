package com.bnp.bnp.discount.services;

import com.bnp.bnp.basket.exceptions.EmptyBasketException;
import com.bnp.bnp.basket.exceptions.InvalidBasketException;
import com.bnp.bnp.basket.exceptions.NoBasketException;
import com.bnp.bnp.books.repositories.BookRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

        if (shoppingBasket.length == 1) {
            return PRICE_PER_BOOK;
        }

        Set<Integer> distinctBooks = new HashSet<>();
        for (int book : shoppingBasket) {
            distinctBooks.add(book);
        }

        if (distinctBooks.size() == 1) {
            return PRICE_PER_BOOK * shoppingBasket.length;
        }
        return 0;
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
