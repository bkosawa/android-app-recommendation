package br.com.kosawalabs.apprecommendation.presentation.login;


import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginModel;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginPresenter;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginView;

class LoginPresenterImpl implements LoginPresenter.LoginPresenterFromView, LoginPresenter.LoginPresenterFromModel {

    private final LoginView view;
    private final LoginModel model;

    public LoginPresenterImpl(LoginView view, LoginModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void init(boolean isLogout) {
        if (isLogout) {
            model.logout();
        }
    }

    @Override
    public void onEditorAction(String username, String password) {
        login(username, password);
    }

    @Override
    public void onLoginButtonClicked(String username, String password) {
        login(username, password);
    }

    public void login(String username, String password) {
        view.clearEditTextErrors();
        if (model.areFieldsValid(username, password)) {
            view.showLoading();
            model.login(username, password);
        }
    }

    @Override
    public void onPasswordIsEmpty() {
        view.showErrorPasswordFieldEmpty();
    }

    @Override
    public void onUsernameIsEmpty() {
        view.showErrorUsernameFieldEmpty();
    }

    @Override
    public void onRequestFinished() {
        view.dismissProgress();
    }

    @Override
    public void onLoginSuccess() {
        view.showAppList();
    }

    @Override
    public void onLoginErrorIncorrectPassword() {
        view.showIncorrectPasswordError();
    }

    @Override
    public void onLoginErrorGeneric(String cause) {
        view.showGenericError(cause);
    }
}
