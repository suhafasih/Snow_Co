import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserCreationAndGetters() {
        // 1. Arrange: Create the test data
        User testUser = new User("admin_test", "pass123", "ADMIN");

        // 2. Act & Assert: Test every method to ensure it returns what we expect
        // Note: In JUnit 4, the custom message goes FIRST!
        assertEquals("Username should match input", "admin_test", testUser.getUsername());
        assertEquals("Role should match input", "ADMIN", testUser.getRole());
    }
}
