package ru.mirea.fedorov.dogguide.data.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class BreedListResponse {
    @SerializedName("message")
    private Map<String, List<String>> message;

    @SerializedName("status")
    private String status;

    public Map<String, List<String>> getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
