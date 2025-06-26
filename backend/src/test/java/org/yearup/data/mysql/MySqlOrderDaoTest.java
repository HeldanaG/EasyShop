package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yearup.data.mysql.MySqlOrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MySqlOrderDaoTest extends BaseDaoTestClass {

    private MySqlOrderDao orderDao;

    @BeforeEach
    public void setup() {
        orderDao = new MySqlOrderDao(dataSource);
    }

    // Test: createOrder inserts an order with correct fields
    @Test
    public void createOrder_shouldInsertOrderAndReturnOrderWithId() {
        // Arrange
        Order order = new Order();
        order.setUserId(1);  // Ensure user_id 1 exists
        order.setDate(LocalDate.now());
        order.setAddress("Test Address");
        order.setCity("Dallas");
        order.setState("TX");
        order.setZip("75001");
        order.setShippingAmount(Double.valueOf(5.99));

        // Act
        Order result = orderDao.createOrder(order);

        // Assert
        assertNotNull(result, "Order should not be null");
        assertTrue(result.getOrderId() > 0, "Order ID should be generated");
    }

    // Test: insertLineItem adds order_line_item correctly
    @Test
    public void insertLineItem_shouldInsertSuccessfully() {
        // Arrange: first create an order
        Order order = new Order();
        order.setUserId(1);
        order.setDate(LocalDate.now());
        order.setAddress("Test Address");
        order.setCity("Dallas");
        order.setState("TX");
        order.setZip("75001");
        order.setShippingAmount(Double.valueOf(4.99));
        order = orderDao.createOrder(order);

        // Create order line item
        OrderLineItem item = new OrderLineItem();
        item.setOrderId(order.getOrderId());
        item.setProductId(1); // Ensure product 1 exists in DB
        item.setSalesPrice(BigDecimal.valueOf(19.99));
        item.setQuantity(2);
        item.setDiscount(BigDecimal.valueOf(0.10));

        // Act & Assert
        assertDoesNotThrow(() -> orderDao.insertLineItem(item));
    }
}
