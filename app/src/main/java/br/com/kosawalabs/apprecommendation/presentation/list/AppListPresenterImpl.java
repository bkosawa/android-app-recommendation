package br.com.kosawalabs.apprecommendation.presentation.list;


import android.text.TextUtils;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListModel;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListPresenter;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;

import static br.com.kosawalabs.apprecommendation.data.DataError.FORBIDDEN;
import static br.com.kosawalabs.apprecommendation.data.DataError.NOT_FOUND;

public class AppListPresenterImpl implements AppListPresenter.AppListPresenterFromView, AppListPresenter.AppListPresenterFromModel {
    protected static final long PAGE_SIZE = 25L;
    private static final boolean FIRST_PAGE = true;
    private static final boolean NEXT_PAGE = false;
    private final AppListView view;
    private final AppListModel model;
    private final AppDataRepository repository;
    private final TokenDataRepository tokenRepository;
    private boolean isRecommended;
    private boolean isLoading;
    private boolean isLastPage;
    private int current;

    public AppListPresenterImpl(AppListView view, AppDataRepository repository, TokenDataRepository tokenRepository) {
        this.view = view;
        this.model = null;
        this.repository = repository;
        this.tokenRepository = tokenRepository;
    }

    public AppListPresenterImpl(AppListView view, AppListModel model) {
        this.view = view;
        this.model = model;
        this.repository = null;
        this.tokenRepository = null;
    }

    @Override
    public void init() {
        String token = tokenRepository.getToken();
        if (TextUtils.isEmpty(token)) {
            view.showLogin();
            return;
        }
        repository.setToken(token);
        refreshList();
    }

    @Override
    public void refreshList() {
        if (!isRecommended) {
            fetchFirstPage();
        } else {
            fetchRecommendedFirstPage();
        }
    }

    @Override
    public void loadMore() {
        if (!isRecommended) {
            fetchNextPage();
        } else {
            fetchRecommendedNextPage();
        }
    }

    @Override
    public long getPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public void setRecommended(boolean recommended) {
        this.isRecommended = recommended;
    }

    @Override
    public boolean hasStopLoading() {
        return isLastPage;
    }

    protected void fetchFirstPage() {
        current = 0;
        isLastPage = false;
        view.showLoading();
        callFetchApps(FIRST_PAGE);
    }

    protected void fetchNextPage() {
        if (shouldLoadMore()) {
            callFetchApps(NEXT_PAGE);
        }
    }

    protected void fetchRecommendedFirstPage() {
        current = 0;
        isLastPage = false;
        view.showLoading();
        callFetchRecommended(FIRST_PAGE);
    }

    protected void fetchRecommendedNextPage() {
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

    private void callShowApps(List<App> apps, boolean isFirstPage) {
        isLoading = false;
        if (isResultLessThanAPage(apps)) {
            isLastPage = true;
        }
        current += apps.size();
        if (isFirstPage) {
            view.showApps(apps);
        } else {
            view.showMoreApps(apps);
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

    private boolean isNotLoading() {
        return !isLoading;
    }

    boolean shouldLoadMore() {
        return !isLoading && !isLastPage;
    }

    private boolean isResultLessThanAPage(List<App> result) {
        return result.size() < PAGE_SIZE;
    }


    @Override
    public void onResultApps(List<App> apps) {
        view.showApps(apps);
    }

    @Override
    public void onResultMoreApps(List<App> apps) {
        view.showMoreApps(apps);
    }

    @Override
    public void onListNotFound() {
        view.showSendDataButton();
    }

    @Override
    public void onRequestNotAllowed() {
        view.showLogin();
    }

    @Override
    public void onRequestError(String cause) {
        view.showError(cause);
    }
}
