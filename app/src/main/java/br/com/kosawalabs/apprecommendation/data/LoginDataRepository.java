package br.com.kosawalabs.apprecommendation.data;


import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;

public interface LoginDataRepository {
    void login(String username, String password, DataCallback<SessionToken> callback);
}
