package br.com.kosawalabs.apprecommendation.presentation.login;

import android.text.TextUtils;

import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.LoginNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.SessionToken;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginModel;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginPresenter;


public class LoginModelImpl implements LoginModel {
    private final TokenDiskRepository tokenRepository;
    private final LoginNetworkRepository repository;
    private LoginPresenter.LoginPresenterFromModel presenter;

    public LoginModelImpl(TokenDiskRepository tokenRepository, LoginNetworkRepository repository) {
        this.tokenRepository = tokenRepository;
        this.repository = repository;
    }

    public void setPresenter(LoginPresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    public void login(String username, String password) {
        repository.login(username, password, new DataCallback<SessionToken>() {
            @Override
            public void onSuccess(SessionToken result) {
                presenter.onRequestFinished();
                if (!TextUtils.isEmpty(result.getToken())) {
                    tokenRepository.putToken(result.getToken());
                    presenter.onLoginSuccess();
                } else {
                    onError(new DataError("Empty Token"));
                }
            }

            @Override
            public void onError(DataError error) {
                presenter.onRequestFinished();
                if (error.getCause().equals("Error Status: 400")) {
                    presenter.onLoginErrorIncorrectPassword();
                } else {
                    presenter.onLoginErrorGeneric(error.getCause());
                }
            }
        });
    }

    @Override
    public void logout() {
        tokenRepository.removeToken();
    }

    @Override
    public boolean areFieldsValid(String username, String password) {
        boolean areValid = true;

        if (TextUtils.isEmpty(password)) {
            presenter.onPasswordIsEmpty();
            areValid = false;
        }

        if (TextUtils.isEmpty(username)) {
            presenter.onUsernameIsEmpty();
            areValid = false;
        }

        return areValid;
    }
}
