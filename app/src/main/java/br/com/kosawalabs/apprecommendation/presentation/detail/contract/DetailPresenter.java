package br.com.kosawalabs.apprecommendation.presentation.detail.contract;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public abstract class DetailPresenter {
    public interface DetailPresenterFromView {
        void init(App app);

        void onViewIsReady();
    }

    public interface DetailPresenterFromModel {
        void onFetchBasicInfo(App app);

        void onFetchComplementaryInfo(App app);
    }
}
