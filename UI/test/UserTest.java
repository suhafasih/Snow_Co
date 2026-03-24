import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserCreationAndGetters() {
        User testUser = new User("admin_test", "pass123", "ADMIN");

        //Suceed to test every method to ensure it returns what we expect
        assertEquals("Username should match input", "admin_test", testUser.getUsername());
        assertEquals("Role should match input", "ADMIN", testUser.getRole());
        assertEquals("Password should match input", "pass123", testUser.getPassword());
    }
}
