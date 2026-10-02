package ru.mirea.fedorov.dogguide.data.remote;

import com.google.gson.annotations.SerializedName;

public class ImageResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private String status;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
