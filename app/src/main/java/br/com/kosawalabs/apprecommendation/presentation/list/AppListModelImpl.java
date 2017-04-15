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

import static br.com.kosawalabs.apprecommendation.data.DataError.FORBIDDEN;
import static br.com.kosawalabs.apprecommendation.data.DataError.NOT_FOUND;

public class AppListModelImpl implements AppListModel {
    protected static final long PAGE_SIZE = 25L;
    private static final boolean FIRST_PAGE = true;
    private static final boolean NEXT_PAGE = false;

    private final AppDataRepository repository;
    private final TokenDataRepository tokenRepository;
    private AppListPresenter.AppListPresenterFromModel presenter;

    private boolean isRecommended;
    private boolean isLoading;
    private boolean isLastPage;
    private int current;

    public AppListModelImpl(AppDataRepository repository, TokenDataRepository tokenRepository) {

        this.repository = repository;
        this.tokenRepository = tokenRepository;
        repository.setToken(tokenRepository.getToken());
    }

    public void setPresenter(AppListPresenter.AppListPresenterFromModel presenter) {
        this.presenter = presenter;
    }

    @Override
    public boolean isLogged() {
        if (TextUtils.isEmpty(tokenRepository.getToken())) {
            return false;
        }
        return true;
    }

    @Override
    public void fetchFirstPage() {
        if (!isRecommended) {
            fetchAppsFirstPage();
        } else {
            fetchRecommendedFirstPage();
        }
    }

    @Override
    public void fetchNextPage() {
        if (!isRecommended) {
            fetchAppsNextPage();
        } else {
            fetchRecommendedNextPage();
        }
    }

    @Override
    public void setRecommended(boolean recommended) {
        this.isRecommended = recommended;
    }

    @Override
    public long getPageSize() {
        return PAGE_SIZE;
    }

    @Override
    public boolean hasStopLoading() {
        return isLastPage;
    }

    protected void fetchAppsFirstPage() {
        current = 0;
        isLastPage = false;
        callFetchApps(FIRST_PAGE);
    }

    protected void fetchAppsNextPage() {
        if (shouldLoadMore()) {
            callFetchApps(NEXT_PAGE);
        }
    }

    protected void fetchRecommendedFirstPage() {
        current = 0;
        isLastPage = false;
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
            presenter.onResultApps(apps);
        } else {
            presenter.onResultMoreApps(apps);
        }
    }

    private void callShowError(DataError error) {
        isLoading = false;
        isLastPage = true;
        switch (error.getErrorCode()) {
            case NOT_FOUND:
                presenter.onListNotFound();
                break;
            case FORBIDDEN:
                presenter.onRequestNotAllowed();
                break;
            default:
                presenter.onRequestError(error.getCause());
                break;
        }
    }

    private boolean shouldLoadMore() {
        return !isLoading && !isLastPage;
    }

    private boolean isNotLoading() {
        return !isLoading;
    }

    private boolean isResultLessThanAPage(List<App> result) {
        return result.size() < PAGE_SIZE;
    }
}
