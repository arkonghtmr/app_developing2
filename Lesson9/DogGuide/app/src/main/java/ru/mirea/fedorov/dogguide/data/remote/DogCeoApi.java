package ru.mirea.fedorov.dogguide.data.remote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DogCeoApi {
    private static final String BASE_URL = "https://dog.ceo/api";

    public Map<String, List<String>> getAllBreeds() throws Exception {
        JSONObject root = get("/breeds/list/all");
        JSONObject message = root.getJSONObject("message");
        Map<String, List<String>> result = new LinkedHashMap<>();
        Iterator<String> keys = message.keys();
        while (keys.hasNext()) {
            String breed = keys.next();
            JSONArray subBreeds = message.getJSONArray(breed);
            List<String> sub = new ArrayList<>();
            for (int i = 0; i < subBreeds.length(); i++) {
                sub.add(subBreeds.getString(i));
            }
            result.put(breed, sub);
        }
        return result;
    }

    public String getRandomImage(String breedPath) throws Exception {
        JSONObject root = get("/breed/" + breedPath + "/images/random");
        return root.getString("message");
    }

    private JSONObject get(String path) throws Exception {
        URL url = new URL(BASE_URL + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        try {
            int code = connection.getResponseCode();
            InputStream stream = code >= 200 && code < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            String body = read(stream);
            if (code < 200 || code >= 300) {
                throw new IllegalStateException("Dog CEO HTTP " + code + ": " + body);
            }
            JSONObject json = new JSONObject(body);
            if (!"success".equals(json.optString("status"))) {
                throw new IllegalStateException("Dog CEO error: " + body);
            }
            return json;
        } finally {
            connection.disconnect();
        }
    }

    private String read(InputStream stream) throws Exception {
        if (stream == null) {
            return "";
        }
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)
        );
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        reader.close();
        return builder.toString();
    }
}
