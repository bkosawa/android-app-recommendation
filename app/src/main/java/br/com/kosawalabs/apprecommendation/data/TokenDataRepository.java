package br.com.kosawalabs.apprecommendation.data;

public interface TokenDataRepository {
    String getToken();

    void putToken(String token);
}
