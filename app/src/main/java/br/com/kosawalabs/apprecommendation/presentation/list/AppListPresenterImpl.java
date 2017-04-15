package br.com.kosawalabs.apprecommendation.presentation.list;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListModel;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListPresenter;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;

public class AppListPresenterImpl implements AppListPresenter.AppListPresenterFromView, AppListPresenter.AppListPresenterFromModel {
    private final AppListView view;
    private final AppListModel model;

    public AppListPresenterImpl(AppListView view, AppListModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void init() {
        if (model.isLogged()) {
            refreshList();
        } else {
            view.showLogin();
        }
    }

    @Override
    public void refreshList() {
        view.showLoading();
        model.fetchFirstPage();
    }

    @Override
    public void onListScrolledToTheEnd() {
        model.fetchNextPage();
    }

    @Override
    public void onAvailableListClicked() {
        model.setRecommended(false);
        refreshList();
    }

    @Override
    public void onRecommendedListClicked() {
        model.setRecommended(true);
        refreshList();
    }

    @Override
    public long getPageSize() {
        return model.getPageSize();
    }

    @Override
    public boolean hasStopLoading() {
        return model.hasStopLoading();
    }

    @Override
    public void onTryAgainButtonClicked() {
        refreshList();
    }

    @Override
    public void onResultApps(List<App> apps) {
        view.showApps(apps);
    }

    @Override
    public void onResultMoreApps(List<App> apps) {
        view.showMoreApps(apps);
    }

    @Override
    public void onListNotFound() {
        view.showSendDataButton();
    }

    @Override
    public void onRequestNotAllowed() {
        view.showLogin();
    }

    @Override
    public void onRequestError(String cause) {
        view.showError(cause);
    }
}
