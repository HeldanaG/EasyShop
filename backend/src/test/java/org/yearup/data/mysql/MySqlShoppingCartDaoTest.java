package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yearup.models.ShoppingCart;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MySqlShoppingCartDaoTest extends BaseDaoTestClass {

    private MySqlShoppingCartDao cartDao;
    private MySqlProductDao productDao;

    @BeforeEach
    public void setup() {
        productDao = new MySqlProductDao(dataSource);
        cartDao = new MySqlShoppingCartDao(dataSource, productDao);
    }

    // Test: get cart returns items correctly
    @Test
    public void getByUserId_shouldReturnCorrectCart() {
        // Arrange: add known product to user cart
        cartDao.addOrIncrement(1, 1);

        // Act: retrieve cart
        ShoppingCart cart = cartDao.getByUserId(1);

        // Assert: cart should have 1 item
        assertEquals(1, cart.getItems().size());
    }

    // Test: insert new product and increment quantity
    @Test
    public void addOrIncrement_shouldInsertOrIncreaseQuantity() {
        // Act: add same product twice
        cartDao.addOrIncrement(1, 1);
        cartDao.addOrIncrement(1, 1);

        // Assert: quantity should be 2
        ShoppingCart cart = cartDao.getByUserId(1);
        assertEquals(2, cart.get(1).getQuantity());
    }

    // Test: setQuantity should change quantity if product exists
    @Test
    public void setQuantity_shouldUpdateQuantityIfItemExists() {
        // Arrange: add product to cart
        cartDao.addOrIncrement(1, 1);

        // Act: update quantity to 5
        cartDao.setQuantity(1, 1, 5);

        // Assert: quantity should now be 5
        ShoppingCart cart = cartDao.getByUserId(1);
        assertEquals(5, cart.get(1).getQuantity());
    }

    // Test: clear all items from cart
    @Test
    public void clear_shouldRemoveAllCartItems() {
        // Arrange: add item to cart
        cartDao.addOrIncrement(1, 1);

        // Act: clear cart
        cartDao.clear(1);

        // Assert: cart should be empty
        ShoppingCart cart = cartDao.getByUserId(1);
        assertTrue(cart.getItems().isEmpty(), "Cart should be empty after clear()");
    }

    // Test: remove single item from cart
    @Test
    public void removeItem_shouldDeleteOneProduct() {
        // Arrange: add product to cart
        cartDao.addOrIncrement(1, 1);

        // Act: remove the item
        cartDao.removeItem(1, 1);

        // Assert: item should no longer exist in cart
        ShoppingCart cart = cartDao.getByUserId(1);
        assertFalse(cart.contains(1), "Cart should not contain product 1 after removal");
    }
}
