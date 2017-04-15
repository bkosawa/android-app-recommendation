package br.com.kosawalabs.apprecommendation.presentation.detail.contract;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface DetailView {
    void setAppBasicInfo(final App app);

    void setAppComplementaryInfo(App app);

    void closeView();
}
