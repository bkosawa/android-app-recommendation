package br.com.kosawalabs.apprecommendation.data;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.pojo.App;

public interface AppDataRepository {
    void getApps(Long offset, Long limit, DataCallback<List<App>> callback);

    void getApp(Integer appId, DataCallback<App> callback);
}