package br.com.kosawalabs.apprecommendation.presentation.list.contract;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface AppListView {
    void showApps(List<App> apps);

    void showMoreApps(List<App> apps);

    void showError(String errorCause);

    void showSendDataButton();

    void showLogoutMessage();

    void showLogin();

    void showLoading();

    void showSendingAppsMessage();

    void startActionUploadApps();
}
