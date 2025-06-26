package org.yearup.data;

import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;



public interface OrderDao {

     // Inserts a new order into the database.
    Order createOrder(Order order);

     // Inserts a new order line item associated with an order.
    void insertLineItem(OrderLineItem lineItem);
}
