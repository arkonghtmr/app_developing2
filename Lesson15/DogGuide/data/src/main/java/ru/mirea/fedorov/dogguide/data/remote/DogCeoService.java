package ru.mirea.fedorov.dogguide.data.remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DogCeoService {
    @GET("breeds/list/all")
    Call<BreedListResponse> getAllBreeds();

    @GET("breed/{path}/images/random")
    Call<ImageResponse> getRandomImage(@Path(value = "path", encoded = true) String path);
}
