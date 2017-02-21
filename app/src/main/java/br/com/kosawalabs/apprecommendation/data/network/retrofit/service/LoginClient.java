package br.com.kosawalabs.apprecommendation.data.network.retrofit.service;

import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginClient {
    @POST("/api/api-token-auth/")
    Call<SessionToken> login(@Query("username") String username,
                             @Query("password") String password);
}
