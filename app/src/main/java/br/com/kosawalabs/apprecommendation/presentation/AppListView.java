package br.com.kosawalabs.apprecommendation.presentation;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface AppListView {
    void showApps(List<App> apps);
}
