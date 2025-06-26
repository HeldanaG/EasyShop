package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.*;
import org.yearup.models.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/orders")
@PreAuthorize("isAuthenticated()")
public class OrdersController {

    private final OrderDao orderDao;
    private final ShoppingCartDao shoppingCartDao;
    private final UserDao userDao;
    private final ProfileDao profileDao;

    @Autowired
    public OrdersController(OrderDao orderDao,
                            ShoppingCartDao shoppingCartDao,
                            UserDao userDao,
                            ProfileDao profileDao) {
        this.orderDao = orderDao;
        this.shoppingCartDao = shoppingCartDao;
        this.userDao = userDao;
        this.profileDao = profileDao;
    }

    @PostMapping
    public void checkout(Principal principal) {
        try {
            // Get the logged-in user
            String userName = principal.getName();
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // Get the user's profile for shipping info
            Profile profile = profileDao.getByUserId(userId);

            // Get current shopping cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            // Create a new order using profile's shipping info
            Order order = new Order();
            order.setUserId(userId);
            order.setDate(LocalDate.now());
            order.setAddress(profile.getAddress());
            order.setCity(profile.getCity());
            order.setState(profile.getState());
            order.setZip(profile.getZip());
            order.setShippingAmount(9.99); // Hypothetical amount

            // Insert order into DB
            order = orderDao.createOrder(order);

            // Add each shopping cart item as an order line item
            for (Map.Entry<Integer, ShoppingCartItem> entry : cart.getItems().entrySet()) {
                ShoppingCartItem item = entry.getValue();

                OrderLineItem lineItem = new OrderLineItem();
                lineItem.setOrderId(order.getOrderId());
                lineItem.setProductId(item.getProduct().getProductId());
                lineItem.setSalesPrice(item.getProduct().getPrice());
                lineItem.setQuantity(item.getQuantity());
                lineItem.setDiscount(item.getDiscountPercent());

                orderDao.insertLineItem(lineItem);
            }

            // Clear shopping cart after order is placed
            shoppingCartDao.clear(userId);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Checkout failed.", e);
        }
    }
}
