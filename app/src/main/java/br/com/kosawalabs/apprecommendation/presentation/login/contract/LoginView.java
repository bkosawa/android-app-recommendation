package br.com.kosawalabs.apprecommendation.presentation.login.contract;

/**
 * Created by bruno.costa on 4/15/17.
 */

public interface LoginView {
    void showLoading();

    void clearEditTextErrors();

    void showErrorUsernameFieldEmpty();

    void showErrorPasswordFieldEmpty();

    void dismissProgress();

    void showAppList();

    void showIncorrectPasswordError();

    void showGenericError(String cause);
}
