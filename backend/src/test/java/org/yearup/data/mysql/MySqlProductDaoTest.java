package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yearup.models.Product;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MySqlProductDaoTest extends BaseDaoTestClass
{
    private MySqlProductDao dao;

    // This method runs before each test and initializes the DAO with a test data source
    @BeforeEach
    public void setup()
    {
        dao = new MySqlProductDao(dataSource);
    }

    // Test to verify that getById() returns the correct product from the database
    @Test
    public void getById_shouldReturn_theCorrectProduct()
    {
        // Arrange - set up the expected product data
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

        // Act - call the actual method to fetch the product
        Product actual = dao.getById(productId);

        // Assert - check if the actual result matches the expected values
        assertEquals(expected.getPrice(), actual.getPrice(), "Expected product price does not match.");
    }

    // Test to verify that update() modifies an existing product correctly
    @Test
    public void update_shouldModifyExistingProduct()
    {
        // Arrange - define new values for an existing product
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

        // Act - update the product and retrieve it again from the database
        dao.update(productId, updated);
        Product actual = dao.getById(productId);

        // Assert - confirm that all updated fields match the expected values
        assertEquals(updated.getName(), actual.getName());
        assertEquals(updated.getPrice(), actual.getPrice());
        assertEquals(updated.getDescription(), actual.getDescription());
        assertEquals(updated.getColor(), actual.getColor());
        assertEquals(updated.getStock(), actual.getStock());
        assertEquals(updated.isFeatured(), actual.isFeatured());
        assertEquals(updated.getImageUrl(), actual.getImageUrl());
    }

    // Test to verify that a new product can be inserted using create()
    @Test
    public void create_shouldInsertNewProduct()
    {
        // Arrange - define a new product to insert
        Product newProduct = new Product()
        {{
            setName("Test Laptop");
            setPrice(new BigDecimal("799.99"));
            setCategoryId(1);
            setDescription("Test description for a laptop.");
            setColor("Silver");
            setStock(25);
            setFeatured(false);
            setImageUrl("test-laptop.jpg");
        }};

        // Act - insert the product
        Product created = dao.create(newProduct);
        Product actual = dao.getById(created.getProductId());

        // Assert - verify fields match the inserted data
        assertEquals(newProduct.getName(), actual.getName());
        assertEquals(newProduct.getPrice(), actual.getPrice());
        assertEquals(newProduct.getColor(), actual.getColor());
        assertEquals(newProduct.getStock(), actual.getStock());
        assertEquals(newProduct.getImageUrl(), actual.getImageUrl());
    }


    // Test to verify that search filters by price range correctly
    @Test
    public void search_shouldReturnFilteredResults()
    {
        // Act - search for products with price between 100 and 600
        var results = dao.search(null, new BigDecimal("100"), new BigDecimal("600"), null);

        // Assert - at least one result is expected within this range
        for (Product p : results)
        {
            BigDecimal price = p.getPrice();
            assert(price.compareTo(new BigDecimal("100")) >= 0 &&
                    price.compareTo(new BigDecimal("600")) <= 0);
        }
    }


    // Test to verify that a product is deleted successfully
    @Test
    public void delete_shouldRemoveProductById()
    {
        // Arrange - insert a product to later delete
        Product toDelete = new Product()
        {{
            setName("Delete Me");
            setPrice(new BigDecimal("99.99"));
            setCategoryId(1);
            setDescription("To be deleted.");
            setColor("Red");
            setStock(10);
            setFeatured(false);
            setImageUrl("delete.jpg");
        }};
        Product inserted = dao.create(toDelete);

        // Act - delete the product and try to retrieve it
        dao.delete(inserted.getProductId());
        Product result = dao.getById(inserted.getProductId());

        // Assert - result should be null after deletion
        assertEquals(null, result);
    }

}
