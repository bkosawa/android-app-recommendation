package br.com.kosawalabs.apprecommendation.data;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.data.pojo.PackageName;

public interface AppDataRepository {
    void getApps(Long offset, Long limit, DataCallback<List<App>> callback);

    void getApp(Integer appId, DataCallback<App> callback);

    void getRecommendedApps(Long offset, Long limit, DataCallback<List<App>> callback);

    void sendMyApps(List<PackageName> packageNames, DataCallback<Void> callback);
}