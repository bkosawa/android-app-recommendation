package br.com.kosawalabs.apprecommendation.presentation;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

public class AppListPresenter {
    private AppDataRepository repository;

    public AppListPresenter(AppDataRepository repository) {
        this.repository = repository;
    }

    public void fetchFirstPage() {
        repository.getApps(0L, 0L, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {

            }

            @Override
            public void onError(DataError error) {

            }
        });
    }
}
