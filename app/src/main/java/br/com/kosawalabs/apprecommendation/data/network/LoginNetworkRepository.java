package br.com.kosawalabs.apprecommendation.data.network;

import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.LoginDataRepository;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.ServiceGenerator;
import br.com.kosawalabs.apprecommendation.data.network.retrofit.service.LoginClient;
import br.com.kosawalabs.apprecommendation.data.pojo.Login;
import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginNetworkRepository implements LoginDataRepository {

    private LoginClient service;

    public LoginNetworkRepository() {
        service = ServiceGenerator.createService(LoginClient.class);
    }

    @Override
    public void login(String username, String password, final DataCallback<SessionToken> callback) {
        Call<SessionToken> login = service.login(new Login(username, password));
        login.enqueue(new Callback<SessionToken>() {
            @Override
            public void onResponse(Call<SessionToken> call, Response<SessionToken> response) {
                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        if (response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError(new DataError("Empty Body"));
                        }
                        break;
                    default:
                        callback.onError(new DataError("Error Status: " + responseCode));
                }
            }

            @Override
            public void onFailure(Call<SessionToken> call, Throwable t) {
                callback.onError(new DataError("RequestFailed"));
            }
        });
    }
}
