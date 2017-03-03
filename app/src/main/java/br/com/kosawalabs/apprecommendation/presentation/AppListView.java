package br.com.kosawalabs.apprecommendation.presentation;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface AppListView {
    void showApps(List<App> apps);

    void showMoreApps(List<App> apps);

    int getVisibleItemCount();

    int getTotalItemCount();

    int getFirstVisibleItemPosition();
}
