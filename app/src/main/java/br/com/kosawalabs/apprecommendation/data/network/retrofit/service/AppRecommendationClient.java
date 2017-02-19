package br.com.kosawalabs.apprecommendation.data.network.retrofit.service;

import br.com.kosawalabs.apprecommendation.data.pojo.AppResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppRecommendationClient {
    @GET("/apps")
    Call<AppResult> getApps(@Query("offset") Long offset,
                            @Query("limit") Long limit);
}