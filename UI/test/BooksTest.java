import org.junit.Test;
import static org.junit.Assert.*;

public class BooksTest {

    // 1. Create a "Manual Mock" class right inside your test file
    // It extends your real Books class, but overrides the methods to return fake data
    class MockBook extends Books {
        public MockBook() {
            super("", "", "", "", "", ""); // Give the superclass empty junk
        }

        @Override
        public String getTitle() { return "The Fake Book"; }

        @Override
        public String getAuthor() { return "Fake Author"; }

        @Override
        public String getIsbn() { return "000000000"; }
    }

    @Test
    public void testBookMethodsWithManualMock() {
        // 2. Arrange: Create your fake object
        Books myMock = new MockBook();

        // 3. Act & Assert: Test that your program interacts with the fake object correctly
        assertEquals("The Fake Book", myMock.getTitle());
        assertEquals("Fake Author", myMock.getAuthor());
        assertEquals("000000000", myMock.getIsbn());
    }
}