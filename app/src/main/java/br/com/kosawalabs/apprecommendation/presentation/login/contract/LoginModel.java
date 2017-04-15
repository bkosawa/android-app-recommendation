package br.com.kosawalabs.apprecommendation.presentation.login.contract;

public interface LoginModel {
    void login(String username, String password);

    void logout();

    boolean areFieldsValid(String username, String password);
}
