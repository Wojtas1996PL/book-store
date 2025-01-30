package mate.academy.service;

import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shopping.cart.ShoppingCartDto;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.service.repository.shopping.cart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public ShoppingCartDto getById(Long id) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findShoppingCartById(id));
    }

    @Override
    public ShoppingCartDto addBook(ShoppingCartDto shoppingCartDto, Book book) {
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        shoppingCartDto.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCartMapper.toEntity(shoppingCartDto));
        return shoppingCartDto;
    }

    @Override
    public ShoppingCartDto updateBookQuantity(ShoppingCartDto shoppingCartDto,
                                              Book book,
                                              int quantity) {
        Set<CartItem> items = shoppingCartDto.getCartItems();
        for (CartItem item : items) {
            if (item.getBook().equals(book)) {
                item.setQuantity(quantity);
                shoppingCartDto.setCartItems(items);
            }
        }
        return shoppingCartMapper
                .toDto(shoppingCartRepository
                        .save(shoppingCartMapper
                                .toEntity(shoppingCartDto)));
    }

    @Override
    public void deleteById(Long id) {
        shoppingCartRepository.deleteById(id);
    }
}
