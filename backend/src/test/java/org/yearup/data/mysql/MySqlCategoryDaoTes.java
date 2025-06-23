package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MySqlCategoryDaoTest extends BaseDaoTestClass
{
    private CategoryDao dao;

    // Setup method runs before each test case
    @BeforeEach
    public void setup()
    {
        // Initialize DAO with test data source
        dao = new MySqlCategoryDao(dataSource);
    }

    // Test: getAllCategories() should return a non-empty list of categories
    @Test
    public void getAll_shouldReturn_nonEmptyList()
    {
        List<Category> categories = dao.getAllCategories();
        assertNotNull(categories);                         // Ensure result is not null
        assertTrue(categories.size() > 0);       // Ensure there's at least one category
    }

    // Test: getById() should return a category that exists
    @Test
    public void getById_shouldReturn_existingCategory()
    {
        Category category = dao.getById(1);      // Assumes category with ID 1 exists in test DB
        assertNotNull(category);                 // Ensure the category is found
        assertEquals(1, category.getCategoryId());
    }

    // Test: create() should insert a new category and return it with a generated ID
    @Test
    public void create_shouldInsert_andReturnCategory()
    {
        Category input = new Category();
        input.setName("Test Category");
        input.setDescription("Test description");

        Category saved = dao.create(input);

        assertNotNull(saved);                    // Ensure object is returned
        assertTrue(saved.getCategoryId() > 0);   // Ensure ID is assigned
        assertEquals("Test Category", saved.getName());
    }

    // Test: update() should successfully change category fields
    @Test
    public void update_shouldModify_existingCategory()
    {
        // First create a category to update
        Category existing = dao.create(new Category() {{
            setName("Before Update");
            setDescription("Initial description");
        }});

        // Change fields
        existing.setName("After Update");
        existing.setDescription("Updated description");

        // Apply update
        dao.update(existing.getCategoryId(), existing);

        // Retrieve updated category and verify changes
        Category updated = dao.getById(existing.getCategoryId());

        assertEquals("After Update", updated.getName());
        assertEquals("Updated description", updated.getDescription());
    }

    // Test: delete() should remove the category from the database
    @Test
    public void delete_shouldRemove_existingCategory()
    {
        // First create a category to delete
        Category category = dao.create(new Category() {{
            setName("To Delete");
            setDescription("Temporary");
        }});

        // Perform delete
        dao.delete(category.getCategoryId());

        // Confirm it's Deleted
        Category deleted = dao.getById(category.getCategoryId());
        assertNull(deleted);                     // Should return null if deleted
    }
}
