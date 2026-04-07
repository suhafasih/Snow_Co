import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

public class API {

    public static JsonArray searchBooks(String query) {
        try {
            String advancedQuery = "title:(" + query + ") OR author:(" + query + ") OR subject:(" + query + ")";

            String encodedQuery = java.net.URLEncoder.encode(advancedQuery, "UTF-8");

            String urlString = "https://openlibrary.org/search.json?q=" + encodedQuery;

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }

            reader.close();

            JsonObject data =
                    JsonParser.parseString(json.toString()).getAsJsonObject();

            return data.getAsJsonArray("docs");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonArray();
    }
    public static JsonArray getAutocompleteSuggestions(String query) {
        try {
            String advancedQuery = "title:(" + query + "*) OR author:(" + query + "*) OR subject:(" + query + "*)";
            String encodedQuery = java.net.URLEncoder.encode(advancedQuery, "UTF-8");

            String urlString = "https://openlibrary.org/search.json?q=" + encodedQuery + "&limit=8";

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));

            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            JsonObject data = JsonParser.parseString(json.toString()).getAsJsonObject();
            return data.getAsJsonArray("docs");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonArray();
    }
}
