package br.com.kosawalabs.apprecommendation.presentation.login;


import android.content.Context;

import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.LoginNetworkRepository;
import br.com.kosawalabs.apprecommendation.presentation.login.contract.LoginView;

class LoginInjector {
    public static LoginPresenterImpl inject(LoginView view, Context context) {
        LoginModelImpl model = new LoginModelImpl(new TokenDiskRepository(context), new LoginNetworkRepository());
        LoginPresenterImpl presenter = new LoginPresenterImpl(view, model);
        model.setPresenter(presenter);
        return presenter;
    }
}
