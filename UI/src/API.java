
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.*;

public class API {

    public static JsonArray searchBooks(String query) {
        try {
            query = query.replace(" ", "+");

            //only send back 20 results to reducing download time.
            String urlString = "https://openlibrary.org/search.json?q=" + query + "&limit=20";

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
}