package org.yearup.data.mysql;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.yearup.models.Profile;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class MySqlProfileDaoTest extends BaseDaoTestClass
{
    private MySqlProfileDao dao;

    @BeforeEach
    public void setup()
    {
        // Initialize DAO using test DataSource provided by BaseDaoTestClass
        dao = new MySqlProfileDao(dataSource);
    }

    /**
     * Test: create() should insert a profile and return it with matching fields.
     */
    @Test
    public void create_shouldInsertProfileSuccessfully()
    {

        String insertUserSql = "INSERT INTO users (user_id, username, hashed_password, role) " +
                "VALUES (4, 'testuser', 'testpass', 'ROLE_USER')";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(insertUserSql))
        {
            stmt.executeUpdate();
        }
        catch (SQLException e)
        {
            fail("Failed to insert user for test: " + e.getMessage());
        }

        // Arrange
        Profile newProfile = new Profile(4 ,"Test", "User", "999-999-9999",
                "test@example.com", "123 Test St", "Dallas", "TX", "75000");

        // Act
        Profile created = dao.create(newProfile);

        // Assert
        assertNotNull(created);
        assertEquals("Test", created.getFirstName());
        assertEquals("User", created.getLastName());

        // Also verify by retrieving it back
        Profile fetched = dao.getByUserId(4);
        assertNotNull(fetched);
        assertEquals("test@example.com", fetched.getEmail());
    }


     //getByUserId() should return a profile for valid userId
    @Test
    public void getByUserId_shouldReturnProfile_whenExists()
    {
        // Arrange
        int userId = 1;

        // Act
        Profile result = dao.getByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals("Joe", result.getFirstName());
    }


     // update() should modify the address of a profile correctly.
    @Test
    public void update_shouldChangeProfileFields()
    {
        // Arrange
        int userId = 1;
        Profile profile = dao.getByUserId(userId);
        assertNotNull(profile);

        String newAddress = "987 Update St";
        profile.setAddress(newAddress);

        // Act
        dao.update(profile);

        // Assert
        Profile updated = dao.getByUserId(userId);
        assertEquals(newAddress, updated.getAddress());
    }


      //getByUserId() should return null if profile doesn't exist.
    @Test
    public void getByUserId_shouldReturnNull_forInvalidUserId()
    {
        // Act
        Profile result = dao.getByUserId(9999);

        // Assert
        assertNull(result);
    }
}
