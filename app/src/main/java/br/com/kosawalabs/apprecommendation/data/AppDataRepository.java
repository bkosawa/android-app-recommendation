package br.com.kosawalabs.apprecommendation.data;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.data.pojo.PackageName;

public interface AppDataRepository {
    void setToken(String token);

    void getApps(Integer offset, Integer limit, DataCallback<List<App>> callback);

    void getApp(Integer appId, DataCallback<App> callback);

    void getRecommendedApps(Integer offset, Integer limit, DataCallback<List<App>> callback);

    void sendMyApps(List<PackageName> packageNames, DataCallback<Void> callback);
}