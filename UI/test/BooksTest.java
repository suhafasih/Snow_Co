import org.junit.Test;
import static org.junit.Assert.*;

public class BooksTest {

    // a "Manual Mock" class
    // it extends the real Books class, but overrides the methods to return fake data
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
        Books myMock = new MockBook();

        //Suceed to test that the program interacts with the fake object correctly
        assertEquals("The Fake Book", myMock.getTitle());
        assertEquals("Fake Author", myMock.getAuthor());
        assertEquals("000000000", myMock.getIsbn());
    }
}