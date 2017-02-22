package br.com.kosawalabs.apprecommendation.data.network.retrofit.service;

import br.com.kosawalabs.apprecommendation.data.pojo.Login;
import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginClient {
    @POST("/api/api-token-auth/")
    Call<SessionToken> login(@Body Login login);
}
