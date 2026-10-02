package ru.mirea.fedorov.dogguide.data.remote;

import android.util.Log;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DogCeoApi implements NetworkApi {
    private static final String TAG = "DogCeoApi";
    private static final String BASE_URL = "https://dog.ceo/api/";

    private final DogCeoService service;

    public DogCeoApi() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.service = retrofit.create(DogCeoService.class);
    }

    @Override
    public Map<String, List<String>> getAllBreeds() throws Exception {
        Response<BreedListResponse> response = service.getAllBreeds().execute();
        if (!response.isSuccessful() || response.body() == null) {
            String error = readError(response);
            Log.e(TAG, "getAllBreeds HTTP " + response.code() + ": " + error);
            throw new IllegalStateException("Dog CEO HTTP " + response.code() + ": " + error);
        }
        BreedListResponse body = response.body();
        if (!"success".equals(body.getStatus()) || body.getMessage() == null) {
            throw new IllegalStateException("Dog CEO error: " + body.getStatus());
        }
        return body.getMessage();
    }

    @Override
    public String getRandomImage(String breedPath) throws Exception {
        Response<ImageResponse> response = service.getRandomImage(breedPath).execute();
        if (!response.isSuccessful() || response.body() == null) {
            String error = readError(response);
            Log.e(TAG, "getRandomImage HTTP " + response.code() + ": " + error);
            throw new IllegalStateException("Dog CEO HTTP " + response.code() + ": " + error);
        }
        ImageResponse body = response.body();
        if (!"success".equals(body.getStatus()) || body.getMessage() == null) {
            throw new IllegalStateException("Dog CEO error: " + body.getStatus());
        }
        return body.getMessage();
    }

    private String readError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            }
        } catch (Exception ignored) {
            // оставляем код ответа
        }
        return response.message();
    }
}
