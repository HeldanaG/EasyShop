package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface    ShoppingCartDao
{

    // 1. Get current user's cart with all items and total
    ShoppingCart getByUserId(int userId);

    // add additional method signatures here

    // 2. Add a product OR increment its quantity by 1
    void addOrIncrement(int userId, int productId);

    // 3. Set quantity for a product
    void setQuantity(int userId, int productId, int quantity);

    // 4. Clear the user's entire cart
    void clear(int userId);
}
