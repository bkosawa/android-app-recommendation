package br.com.kosawalabs.apprecommendation.presentation.list.contract;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public abstract class AppListPresenter {
    public interface AppListPresenterFromView {
        void init();

        void refreshList();

        void onListScrolledToTheEnd();

        long getPageSize();

        void onAvailableListClicked();

        void onRecommendedListClicked();

        void onTryAgainButtonClicked();

        void onSendDataButtonClicked();

        boolean hasStopLoading();
    }

    public interface AppListPresenterFromModel {
        void onResultApps(List<App> apps);

        void onResultMoreApps(List<App> apps);

        void onListNotFound();

        void onRequestNotAllowed();

        void onRequestError(String cause);
    }
}
