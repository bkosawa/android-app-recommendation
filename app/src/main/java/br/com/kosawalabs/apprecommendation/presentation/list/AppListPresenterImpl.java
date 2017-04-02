package br.com.kosawalabs.apprecommendation.presentation.list;


import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static br.com.kosawalabs.apprecommendation.data.DataError.FORBIDDEN;
import static br.com.kosawalabs.apprecommendation.data.DataError.NOT_FOUND;

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
        isLastPage = false;
        view.showLoading();
        callFetchApps(FIRST_PAGE);
    }

    @Override
    public void fetchNextPage() {
        if (shouldLoadMore()) {
            callFetchApps(NEXT_PAGE);
        }
    }

    @Override
    public void fetchRecommendedFirstPage() {
        current = 0;
        isLastPage = false;
        view.showLoading();
        callFetchRecommended(FIRST_PAGE);
    }

    @Override
    public void fetchRecommendedNextPage() {
        if (shouldLoadMore()) {
            callFetchRecommended(NEXT_PAGE);
        }
    }

    private void callFetchApps(final boolean isFirstPage) {
        if (isNotLoading()) {
            isLoading = true;
            repository.getApps((long) current, PAGE_SIZE, new DataCallback<List<App>>() {
                @Override
                public void onSuccess(List<App> result) {
                    callShowApps(result, isFirstPage);
                }

                @Override
                public void onError(DataError error) {
                    callShowError(error);
                }
            });
        }
    }

    private void callFetchRecommended(final boolean isFirstPage) {
        if (isNotLoading()) {
            isLoading = true;
            repository.getRecommendedApps((long) current, PAGE_SIZE, new DataCallback<List<App>>() {
                @Override
                public void onSuccess(List<App> result) {
                    callShowApps(result, isFirstPage);
                }

                @Override
                public void onError(DataError error) {
                    callShowError(error);
                }
            });
        }
    }

    private void callShowApps(List<App> result, boolean isFirstPage) {
        isLoading = false;
        if (isResultLessThanAPage(result)) {
            isLastPage = true;
        }
        current += result.size();
        if (isFirstPage) {
            view.showApps(result);
        } else {
            view.showMoreApps(result);
        }
    }

    private void callShowError(DataError error) {
        isLoading = false;
        isLastPage = true;
        switch (error.getErrorCode()) {
            case NOT_FOUND:
                view.showSendDataButton();
                break;
            case FORBIDDEN:
                view.showLogin();
                break;
            default:
                view.showError(error.getCause());
                break;
        }
    }

    long getPageSize() {
        return PAGE_SIZE;
    }

    private boolean isNotLoading() {
        return !isLoading;
    }

    boolean shouldLoadMore() {
        return !isLoading && !isLastPage;
    }

    private boolean isResultLessThanAPage(List<App> result) {
        return result.size() < PAGE_SIZE;
    }
}
