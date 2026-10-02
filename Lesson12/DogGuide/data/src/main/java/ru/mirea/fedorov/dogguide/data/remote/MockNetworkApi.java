package ru.mirea.fedorov.dogguide.data.remote;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Заглушка набора данных Dog CEO: тот же JSON-контракт, что и у внешнего API.
 * Используется, если сеть недоступна.
 */
public class MockNetworkApi implements NetworkApi {
    private static final String MOCK_BREEDS_JSON = "{"
            + "\"status\":\"success\","
            + "\"message\":{"
            + "\"husky\":[],"
            + "\"pug\":[],"
            + "\"beagle\":[],"
            + "\"retriever\":[\"golden\"],"
            + "\"labrador\":[],"
            + "\"hound\":[\"afghan\"],"
            + "\"corgi\":[\"cardigan\"],"
            + "\"shepherd\":[\"german\"],"
            + "\"boxer\":[],"
            + "\"dalmatian\":[],"
            + "\"shiba\":[],"
            + "\"chihuahua\":[],"
            + "\"malamute\":[],"
            + "\"akita\":[],"
            + "\"samoyed\":[],"
            + "\"pomeranian\":[]"
            + "}}";

    private static final Map<String, String> MOCK_IMAGES = new LinkedHashMap<>();

    static {
        MOCK_IMAGES.put("husky", "https://images.dog.ceo/breeds/husky/n02110185_10047.jpg");
        MOCK_IMAGES.put("pug", "https://images.dog.ceo/breeds/pug/n02110958_15626.jpg");
        MOCK_IMAGES.put("retriever", "https://images.dog.ceo/breeds/retriever-golden/n02099601_100.jpg");
        MOCK_IMAGES.put("labrador", "https://images.dog.ceo/breeds/labrador/n02099712_1383.jpg");
        MOCK_IMAGES.put("beagle", "https://images.dog.ceo/breeds/beagle/n02088364_11136.jpg");
        MOCK_IMAGES.put("hound", "https://images.dog.ceo/breeds/hound-afghan/n02088094_1003.jpg");
        MOCK_IMAGES.put("corgi", "https://images.dog.ceo/breeds/corgi-cardigan/n02113186_1030.jpg");
        MOCK_IMAGES.put("shepherd", "https://images.dog.ceo/breeds/shepherd-german/n02106662_1087.jpg");
        MOCK_IMAGES.put("boxer", "https://images.dog.ceo/breeds/boxer/n02108089_1030.jpg");
        MOCK_IMAGES.put("dalmatian", "https://images.dog.ceo/breeds/dalmatian/n02110341_108.jpg");
        MOCK_IMAGES.put("shiba", "https://images.dog.ceo/breeds/shiba/shiba-1.jpg");
        MOCK_IMAGES.put("chihuahua", "https://images.dog.ceo/breeds/chihuahua/n02085620_10074.jpg");
        MOCK_IMAGES.put("malamute", "https://images.dog.ceo/breeds/malamute/n02110063_1104.jpg");
        MOCK_IMAGES.put("akita", "https://images.dog.ceo/breeds/akita/Akita_inu.jpg");
        MOCK_IMAGES.put("samoyed", "https://images.dog.ceo/breeds/samoyed/n02111889_1099.jpg");
        MOCK_IMAGES.put("pomeranian", "https://images.dog.ceo/breeds/pomeranian/n02112018_1090.jpg");
    }

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
        for (Map.Entry<String, String> entry : MOCK_IMAGES.entrySet()) {
            if (breedPath.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return MOCK_IMAGES.get("husky");
    }
}
