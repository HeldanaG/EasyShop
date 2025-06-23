package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yearup.models.Product;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // arrange
        int productId = 1;
        Product expected = new Product()
        {{
            setProductId(1);
            setName("Smartphone");
            setPrice(new BigDecimal("499.99"));
            setCategoryId(1);
            setDescription("A powerful and feature-rich smartphone for all your communication needs.");
            setColor("Black");
            setStock(50);
            setFeatured(false);
            setImageUrl("smartphone.jpg");
        }};

        // act
        var actual = dao.getById(productId);

        // assert
        assertEquals(expected.getPrice(), actual.getPrice(), "Because I tried to get product 1 from the database.");
    }
    @Test
    public void update_shouldModifyExistingProduct()
    {
        int productId = 1;
        Product updated = new Product()
        {{
            setProductId(productId);
            setName("Updated Smartphone");
            setPrice(new BigDecimal("399.99"));
            setCategoryId(1);
            setDescription("Updated description");
            setColor("Blue");
            setStock(100);
            setFeatured(true);
            setImageUrl("updated.jpg");
        }};

        dao.update(productId, updated);
        Product actual = dao.getById(productId);

        assertEquals(updated.getName(), actual.getName());
        assertEquals(updated.getPrice(), actual.getPrice());
        assertEquals(updated.getDescription(), actual.getDescription());
        assertEquals(updated.getColor(), actual.getColor());
        assertEquals(updated.getStock(), actual.getStock());
        assertEquals(updated.isFeatured(), actual.isFeatured());
        assertEquals(updated.getImageUrl(), actual.getImageUrl());
    }



}