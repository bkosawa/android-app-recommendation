package br.com.kosawalabs.apprecommendation.presentation.detail;

import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailModel;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailPresenter;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailView;

class DetailPresenterImpl implements DetailPresenter.DetailPresenterFromView, DetailPresenter.DetailPresenterFromModel {

    private final DetailView view;
    private final DetailModel model;

    DetailPresenterImpl(DetailView view, DetailModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void init(App app) {
        if (model.isNotLogged()) {
            view.closeView();
        } else {
            model.setApp(app);
        }
    }

    @Override
    public void onViewIsReady() {
        model.fetchAppInfo();
    }

    @Override
    public void onFetchBasicInfo(App app) {
        view.setAppBasicInfo(app);
    }

    @Override
    public void onFetchComplementaryInfo(App app) {
        view.setAppComplementaryInfo(app);
    }
}
