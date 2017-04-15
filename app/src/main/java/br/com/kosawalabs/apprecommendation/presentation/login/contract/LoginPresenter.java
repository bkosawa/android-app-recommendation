package br.com.kosawalabs.apprecommendation.presentation.login.contract;

public abstract class LoginPresenter {
    public interface LoginPresenterFromView {
        void init(boolean isLogout);

        void onEditorAction(String username, String password);

        void onLoginButtonClicked(String username, String password);
    }

    public interface LoginPresenterFromModel {
        void onPasswordIsEmpty();

        void onUsernameIsEmpty();

        void onRequestFinished();

        void onLoginSuccess();

        void onLoginErrorIncorrectPassword();

        void onLoginErrorGeneric(String cause);
    }
}
