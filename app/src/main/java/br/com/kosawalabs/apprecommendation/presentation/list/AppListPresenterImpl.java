package br.com.kosawalabs.apprecommendation.presentation.list;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

public class AppListPresenterImpl implements AppListPresenter {
    protected static final long PAGE_SIZE = 25L;
    private static final boolean FIRST_PAGE = true;
    private static final boolean NEXT_PAGE = false;
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
        current = 0;
        callFetchApps(0L, PAGE_SIZE, FIRST_PAGE);
    }

    @Override
    public void fetchNextPage() {
        callFetchApps(current, PAGE_SIZE, NEXT_PAGE);
    }

    @Override
    public boolean shouldLoadMore() {
        return !isLoading && !isLastPage;
    }

    @Override
    public void fetchRecommendedFirstPage() {
        current = 0;
        callFetchRecommended(0L, PAGE_SIZE, FIRST_PAGE);
    }

    @Override
    public void fetchRecommendedNextPage() {
        callFetchRecommended(current, PAGE_SIZE, NEXT_PAGE);
    }

    private void callFetchApps(long offset, long limit, final boolean isFirstPage) {
        isLoading = true;
        repository.getApps(offset, limit, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                isLoading = false;
                if (result.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                current += result.size();
                if (isFirstPage) {
                    view.showApps(result);
                } else {
                    view.showMoreApps(result);
                }
            }

            @Override
            public void onError(DataError error) {
                callShowError(error);
            }
        });
    }

    private void callFetchRecommended(long offset, long limit, final boolean isFirstPage) {
        isLoading = true;
        repository.getRecommendedApps(offset, limit, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                isLoading = false;
                if (result.size() < PAGE_SIZE) {
                    isLastPage = true;
                }
                current += result.size();
                if (isFirstPage) {
                    view.showApps(result);
                } else {
                    view.showMoreApps(result);
                }
            }

            @Override
            public void onError(DataError error) {
                callShowError(error);
            }
        });
    }

    private void callShowError(DataError error) {
        isLoading = false;
        isLastPage = true;
        view.showError(error.getCause());
    }

    public long getPageSize() {
        return PAGE_SIZE;
    }
}
