import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import javax.swing.*;
import java.lang.reflect.*;





//to run:
//cd C:\Users\zaina\IdeaProjects\Snow_Co\UI\src
//
//javac -cp ".;..\lib\gson-2.10.1.jar;..\lib\junit-4.13.2.jar;..\lib\hamcrest-core-1.3.jar" BookDetailViewTest.java
//
//java -cp ".;..\lib\gson-2.10.1.jar;..\lib\junit-4.13.2.jar;..\lib\hamcrest-core-1.3.jar" org.junit.runner.JUnitCore BookDetailViewTest


/**
 * TDD Test Suite — Book Detail View Feature (JUnit 4)
 *
 * FEATURE: When a user clicks on a book card, a BookDetailPanel
 * should appear showing full title, author, status, description,
 * and a borrow button.
 *
 * STATUS: RED — All tests WILL FAIL because the feature doesn't exist yet.
 */
public class BookDetailViewTest {

    private LoginWindow loginWindow;

    // Runs before each test — creates a fresh LoginWindow instance
    @Before
    public void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            loginWindow = new LoginWindow();
        });
    }

    // Runs after each test — closes the window to free up memory
    @After
    public void tearDown() {
        if (loginWindow != null) {
            SwingUtilities.invokeLater(() -> loginWindow.dispose());
        }
    }

    // Checks that the BookDetailPanel class has been created.
    // Fails with ClassNotFoundException if the file doesn't exist yet.
    @Test
    public void test1_BookDetailPanel_ClassExists() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        assertNotNull("BookDetailPanel class should exist", clazz);
    }

    // Checks that BookDetailPanel has a constructor that accepts
    // title, author, coverUrl, status, and description as Strings.
    @Test
    public void test2_BookDetailPanel_ConstructorExists() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> constructor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        assertNotNull("Constructor must exist", constructor);
    }

    // Checks that getBookTitle() returns the full untruncated title.
    // Important because the card view cuts titles off at 20 characters.
    @Test
    public void test3_BookDetailPanel_DisplaysFullTitle() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        Object panel = ctor.newInstance("The Great Gatsby", "F. Scott Fitzgerald", "", "Available", "A classic.");
        Method getTitle = clazz.getMethod("getBookTitle");
        String title = (String) getTitle.invoke(panel);
        assertEquals("BookDetailPanel must return full title", "The Great Gatsby", title);
    }

    // Checks that the Borrow button exists and is enabled
    // when a book's status is Available\ so the user can borrow it.
    @Test
    public void test4_BorrowButton_VisibleWhenAvailable() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        Object panel = ctor.newInstance("Dune", "Frank Herbert", "", "Available", "Sci-fi epic.");
        Method getBtn = clazz.getMethod("getBorrowButton");
        JButton btn = (JButton) getBtn.invoke(panel);
        assertNotNull("Borrow button must exist", btn);
        assertTrue("Borrow button must be enabled for Available books", btn.isEnabled());
    }

    // Checks that the Borrow button is disabled when a book is
    // already "Checked Out" — user should not be able to borrow it.
    @Test
    public void test5_BorrowButton_DisabledWhenCheckedOut() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        Object panel = ctor.newInstance("Educated", "Tara Westover", "", "Checked Out", "A memoir.");
        Method getBtn = clazz.getMethod("getBorrowButton");
        JButton btn = (JButton) getBtn.invoke(panel);
        assertFalse("Borrow button must be DISABLED when Checked Out", btn.isEnabled());
    }

    // Checks that LoginWindow has a showBookDetail() method.
    // This method will be called when the user clicks a book card.
    @Test
    public void test6_LoginWindow_HasShowBookDetailMethod() throws Exception {
        Method method = LoginWindow.class.getMethod(
                "showBookDetail",
                String.class, String.class, String.class, String.class, String.class
        );
        assertNotNull("LoginWindow must have showBookDetail() method", method);
    }

    // Checks that getDescription() returns the full synopsis text
    // passed in at construction — nothing should be lost or trimmed
    @Test
    public void test7_BookDetailPanel_ReturnsDescription() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        String synopsis = "A coming-of-age story set in rural Idaho.";
        Object panel = ctor.newInstance("Educated", "Tara Westover", "", "Available", synopsis);
        Method getDesc = clazz.getMethod("getDescription");
        String returned = (String) getDesc.invoke(panel);
        assertEquals("Must return the full description", synopsis, returned);
    }

    // Checks that getAuthorName() returns exactly the author string
    // passed in — used to display the author in the detail view.
    @Test
    public void test8_BookDetailPanel_ReturnsAuthor() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        Object panel = ctor.newInstance("Project Hail Mary", "Andy Weir", "", "Available", "Sci-fi.");
        Method getAuthor = clazz.getMethod("getAuthorName");
        String author = (String) getAuthor.invoke(panel);
        assertEquals("Must return correct author name", "Andy Weir", author);
    }

    // Checks that getStatus() correctly reflects whether a book is
    // "Available" or "Checked Out" — tested with both values.
    @Test
    public void test9_BookDetailPanel_ReturnsCorrectStatus() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        Constructor<?> ctor = clazz.getConstructor(
                String.class, String.class, String.class, String.class, String.class
        );
        Object p1 = ctor.newInstance("Dune", "Frank Herbert", "", "Available", "");
        Object p2 = ctor.newInstance("Dune", "Frank Herbert", "", "Checked Out", "");
        Method getStatus = clazz.getMethod("getStatus");
        assertEquals("Available",   (String) getStatus.invoke(p1));
        assertEquals("Checked Out", (String) getStatus.invoke(p2));
    }

    // Checks that BookDetailPanel extends JPanel so it can be embedded directly into the existing Swing UI layout.
    @Test
    public void test10_BookDetailPanel_ExtendsJPanel() throws Exception {
        Class<?> clazz = Class.forName("BookDetailPanel");
        assertTrue("BookDetailPanel must extend JPanel", JPanel.class.isAssignableFrom(clazz));
    }
}