package br.com.kosawalabs.apprecommendation.presentation.detail.contract;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface DetailModel {
    boolean isNotLogged();

    void setApp(App app);

    void fetchAppInfo();
}
