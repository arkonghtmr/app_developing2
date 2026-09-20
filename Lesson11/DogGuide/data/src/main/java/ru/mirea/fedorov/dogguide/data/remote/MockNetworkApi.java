package ru.mirea.fedorov.dogguide.data.remote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockNetworkApi implements NetworkApi {
    private static final String MOCK_BREEDS_JSON = "{"
            + "\"status\":\"success\","
            + "\"message\":{"
            + "\"husky\":[],"
            + "\"pug\":[],"
            + "\"beagle\":[],"
            + "\"retriever\":[\"golden\"],"
            + "\"labrador\":[]"
            + "}}";

    @Override
    public Map<String, List<String>> getAllBreeds() throws Exception {
        JSONObject root = new JSONObject(MOCK_BREEDS_JSON);
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

    @Override
    public String getRandomImage(String breedPath) {
        if (breedPath.contains("pug")) {
            return "https://images.dog.ceo/breeds/pug/n02110958_15626.jpg";
        }
        if (breedPath.contains("retriever")) {
            return "https://images.dog.ceo/breeds/retriever-golden/n02099601_100.jpg";
        }
        if (breedPath.contains("labrador")) {
            return "https://images.dog.ceo/breeds/labrador/n02099712_1383.jpg";
        }
        if (breedPath.contains("beagle")) {
            return "https://images.dog.ceo/breeds/beagle/n02088364_11136.jpg";
        }
        return "https://images.dog.ceo/breeds/husky/n02110185_10047.jpg";
    }
}
