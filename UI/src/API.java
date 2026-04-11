import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

public class API {
    /**
     * Searches for the books by using Open Library API
     * query expands to search by title, author, and subject fields
     *
     * @param query Search term entered by user
     * @return JsonArray of results of books (docs)
     */
    public static JsonArray searchBooks(String query) {
        try {
            // Builds a query more advanced to search across multiple fields
            String advancedQuery = "title:(" + query + ") OR author:(" + query + ") OR subject:(" + query + ")";

            // Encode the query to make it safe to use in a URL(can handle spaces and special characters)
            String encodedQuery = java.net.URLEncoder.encode(advancedQuery, "UTF-8");

            // API URL being constructed
            String urlString = "https://openlibrary.org/search.json?q=" + encodedQuery;

            // URL object and open HTTP connection created
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // Set the request method to GET
            conn.setRequestMethod("GET");

            // Reads the response from the API
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;

            // Each line is read from the response and is appended to the StringBuilder
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            // Reader is closed to free resources
            reader.close();

            // JSON response is parsed into a JsonObject
            JsonObject data =
                    JsonParser.parseString(json.toString()).getAsJsonObject();

            // "docs" Array is returned containing the search results
            return data.getAsJsonArray("docs");

        } catch (Exception e) {
            // Prints stack trace error if something goes wrong(network issue, error with parsing etc.)
            e.printStackTrace();
        }

        // Empty array is returned if an error is to occur
        return new JsonArray();
    }

    /**
     * Provided suggestions that are autocompleted based on partial input from user
     * uses * to match the partial words
     * @param query Partial input from user
     * @return JsonArray of suggested book results
     */
    public static JsonArray getAutocompleteSuggestions(String query) {
        try {
            // Add * for partial matching (this is autocomplete behavior)
            String advancedQuery = "title:(" + query + "*) OR author:(" + query + "*) OR subject:(" + query + "*)";

            // Encode the query to make it safe to us
            String encodedQuery = java.net.URLEncoder.encode(advancedQuery, "UTF-8");

            // Results is limited to 8 suggestions for faster, cleaner, autocomplete
            String urlString = "https://openlibrary.org/search.json?q=" + encodedQuery + "&limit=8";

            // API open connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Request method timeouts set (it prevents hanging for long periods of time)
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            // Reads the response from the API
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;

            // Response lines are appended into one JSON string
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            // reader is closed
            reader.close();

            // JSON string parsed into JsonObject
            JsonObject data = JsonParser.parseString(json.toString()).getAsJsonObject();

            // Return suggestions(from docs array)
            return data.getAsJsonArray("docs");

        } catch (Exception e) {
            // Prints stack trace error if something goes wrong
            e.printStackTrace();
        }

        // Empty array is returned if something fails
        return new JsonArray();
    }
}
