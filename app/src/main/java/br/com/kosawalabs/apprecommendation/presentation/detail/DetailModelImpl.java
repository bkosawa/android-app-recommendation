package br.com.kosawalabs.apprecommendation.presentation.detail;

import android.text.TextUtils;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailModel;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailPresenter;


public class DetailModelImpl implements DetailModel {

    private DetailPresenter.DetailPresenterFromModel presenter;
    private final AppDataRepository repository;
    private final TokenDataRepository tokenRepository;

    private App app;

    public DetailModelImpl(AppDataRepository repository, TokenDataRepository tokenRepository) {
        this.repository = repository;
        this.tokenRepository = tokenRepository;
        this.repository.setToken(tokenRepository.getToken());
    }

    public void setPresenter(DetailPresenterImpl presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isNotLogged() {
        return TextUtils.isEmpty(tokenRepository.getToken());
    }

    @Override
    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public void fetchAppInfo() {
        if (app != null) {
            presenter.onFetchBasicInfo(app);
            repository.getApp(app.getId(), new DataCallback<App>() {
                @Override
                public void onSuccess(App result) {
                    app = result;
                    presenter.onFetchComplementaryInfo(app);
                }

                @Override
                public void onError(DataError error) {

                }
            });
        }
    }
}
