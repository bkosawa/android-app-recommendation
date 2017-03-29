package br.com.kosawalabs.apprecommendation.presentation.list;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface AppListView {
    void showApps(List<App> apps);

    void showMoreApps(List<App> apps);

    int getVisibleItemCount();

    int getTotalItemCount();

    int getFirstVisibleItemPosition();

    void showError(String errorCause);
}
