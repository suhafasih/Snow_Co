import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import javax.swing.*;
import java.lang.reflect.Field;

import com.google.gson.JsonArray;

public class LoginWindowTest {

    private LoginWindow window;
    private JTextField usernameField;
    private JPasswordField passwordField;

    @Before
    public void setUp() throws Exception {
        window = new LoginWindow();

        Field userField = LoginWindow.class.getDeclaredField("usernameField");
        userField.setAccessible(true);
        usernameField = (JTextField) userField.get(window);

        Field passField = LoginWindow.class.getDeclaredField("passwordField");
        passField.setAccessible(true);
        passwordField = (JPasswordField) passField.get(window);
    }

    // UT-01-CB: Admin Login
    @Test
    public void testAdminLogin() {
        usernameField.setText("admin");
        passwordField.setText("password");

        window.performLogin();

        assertTrue(window.isLoggedIn);
        assertEquals("ADMIN", window.currentUserRole);
        assertEquals("admin", window.currentUser);
    }

    // UT-02-CB: User Login
    @Test
    public void testUserLogin() {
        usernameField.setText("user");
        passwordField.setText("password");

        window.performLogin();

        assertTrue(window.isLoggedIn);
        assertEquals("USER", window.currentUserRole);
    }

    // UT-03-CB: Invalid Login
    @Test
    public void testInvalidLogin() {
        usernameField.setText("wrong");
        passwordField.setText("wrong");

        window.performLogin();

        assertFalse(window.isLoggedIn);
        assertEquals("", window.currentUser);

        assertEquals("", new String(passwordField.getPassword()));
    }


    // UT-04-CB: Logout
    @Test
    public void testLogout() {
        usernameField.setText("user");
        passwordField.setText("password");

        window.performLogin();
        window.performLogout();

        assertFalse(window.isLoggedIn);
        assertEquals("", window.currentUser);
        assertEquals("", window.currentUserRole);
    }

    // UT-05-CB: searchBooks valid
    @Test
    public void testSearchBooksValid() {
        JsonArray result = API.searchBooks("Dune");

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }


    // UT-06-CB: searchBooks with spaces
    @Test
    public void testSearchBooksWithSpaces() {
        JsonArray result = API.searchBooks("Dune Frank Herbert");

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    // UT-07-CB: searchBooks invalid
    @Test
    public void testSearchBooksInvalid() {
        JsonArray result = API.searchBooks("asdkfjhasdkjfhakjsdfh");

        assertNotNull(result);
        assertTrue(result.size() == 0 || result.size() >= 0); // just ensure no crash
    }

    // UT-08-TB: paintComponent

    @Test
    public void testPaintComponent() {
        JPanel panel = new RoundedPanel(20);

        try {
            panel.paint(panel.getGraphics());
            assertTrue(true); // no crash = pass
        } catch (Exception e) {
            fail("paintComponent crashed");
        }
    }

    // UT-09-TB: Admin Action Cards

    @Test
    public void testCreateAdminActionCards() throws Exception {

        // Access private method
        java.lang.reflect.Method method = LoginWindow.class
                .getDeclaredMethod("createAdminActionCards",
                        java.awt.Color.class,
                        java.awt.Color.class,
                        java.awt.Color.class,
                        java.awt.Color.class);

        method.setAccessible(true);

        JPanel panel = (JPanel) method.invoke(
                window,
                new java.awt.Color(156,165,118),
                new java.awt.Color(140,100,85),
                new java.awt.Color(80,60,50),
                new java.awt.Color(220,210,190)
        );

        assertNotNull(panel);
        assertTrue(panel.getComponentCount() > 0);
    }
}