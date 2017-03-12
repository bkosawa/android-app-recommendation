package br.com.kosawalabs.apprecommendation.presentation.list;


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

    @Override
    public void fetchNextPage() {
        isLoading = true;
        repository.getApps((long) current, PAGE_SIZE, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                isLoading = false;
                if (result.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                current += result.size();
                view.showMoreApps(result);
            }

            @Override
            public void onError(DataError error) {
                isLoading = false;
                isLastPage = true;
            }
        });
    }

    @Override
    public boolean shouldLoadMore() {
        return !isLoading && !isLastPage;
    }

    @Override
    public boolean listIsAtTheEnd() {
        int visibleItemCount = view.getVisibleItemCount();
        int totalItemCount = view.getTotalItemCount();
        int firstVisibleItemPosition = view.getFirstVisibleItemPosition();
        return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE;
    }

    @Override
    public void fetchRecommendedFirstPage() {
        isLoading = true;
        repository.getRecommendedApps(0L, PAGE_SIZE, new DataCallback<List<App>>() {
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

    @Override
    public void fetchRecommendedNextPage() {
        isLoading = true;
        repository.getRecommendedApps((long) current, PAGE_SIZE, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                isLoading = false;
                if (result.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                current += result.size();
                view.showMoreApps(result);
            }

            @Override
            public void onError(DataError error) {
                isLoading = false;
                isLastPage = true;
            }
        });
    }
}
