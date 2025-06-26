package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.OrderDao;
import org.yearup.models.Order;
import org.yearup.models.OrderLineItem;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

@Component
public class MySqlOrderDao extends MySqlDaoBase implements OrderDao {

    public MySqlOrderDao(DataSource dataSource) {
        super(dataSource);
    }

    // Inserts a new order into the orders table.
    @Override
    public Order createOrder(Order order) {
        String sql = """
            INSERT INTO orders (user_id, date, address, city, state, zip, shipping_amount)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setDate(2, Date.valueOf(order.getDate() != null ? order.getDate() : LocalDate.now()));
            stmt.setString(3, order.getAddress());
            stmt.setString(4, order.getCity());
            stmt.setString(5, order.getState());
            stmt.setString(6, order.getZip());
            stmt.setDouble(7, order.getShippingAmount());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    order.setOrderId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }

     // Inserts an order line item into the order_line_items table.
    @Override
    public void insertLineItem(OrderLineItem lineItem) {
        String sql = """
            INSERT INTO order_line_items (order_id, product_id, sales_price, quantity, discount)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, lineItem.getOrderId());
            stmt.setInt(2, lineItem.getProductId());
            stmt.setDouble(3, lineItem.getSalesPrice());
            stmt.setInt(4, lineItem.getQuantity());
            stmt.setDouble(5, lineItem.getDiscount());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
