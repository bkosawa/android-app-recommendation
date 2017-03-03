package br.com.kosawalabs.apprecommendation.presentation;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

public class AppListPresenterImpl implements AppListPresenter {
    public static final long PAGE_SIZE = 25L;
    private final AppListView view;
    private final AppDataRepository repository;
    private boolean isLoading;
    private boolean isLastPage;
    private int current;


    public AppListPresenterImpl(AppListView view, AppDataRepository repository) {
        this.view = view;
        this.repository = repository;
    }

    @Override
    public void fetchFirstPage() {
        isLoading = true;
        repository.getApps(0L, PAGE_SIZE, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                isLoading = false;
                if (result.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                current = result.size();
                view.showApps(result);
            }

            @Override
            public void onError(DataError error) {
                isLoading = false;
                isLastPage = true;
            }
        });
    }
}
