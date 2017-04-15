package br.com.kosawalabs.apprecommendation.presentation.list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListModel;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListPresenter;

import static br.com.kosawalabs.apprecommendation.presentation.list.AppListModelImpl.PAGE_SIZE;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppListModelTest {

    private AppListModel model;

    @Mock
    private AppListPresenter.AppListPresenterFromModel presenter;

    @Mock
    private AppDataRepository repository;

    @Mock
    private TokenDataRepository tokenRepository;

    @Captor
    private ArgumentCaptor<DataCallback<List<App>>> dataCallbackArgumentCaptor;

    @Before
    public void setup() {
        initMocks(this);
        model = new AppListModelImpl(repository, tokenRepository);
        ((AppListModelImpl) model).setPresenter(presenter);
    }

    @Test
    public void givenFetchFirstPageIsCalledItShouldCallRepositoryWithZeroOffsetAndZeroLimit() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), any(DataCallback.class));
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsIsSuccessfullyItShouldCallShowAppsOnView() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(5);

        dataCallback.onSuccess(mockList);

        verify(presenter).onResultApps(mockList);
//        verify(view).showApps(mockList);
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsReturnErrorItShouldNotCallShowAppsOnView() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        verify(presenter, never()).onResultApps(anyListOf(App.class));
//        verify(view, never()).showApps(anyListOf(App.class));
    }

    @Test
    public void givenFetchFirstPageIsNotCalledYetItShouldBePossibleToLoadMore() {
        assertTrue(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasNotReturnItShouldNotBePossibleToLoadMore() {
        model.fetchFirstPage();

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithPageSizeItemsItShouldBePossibleToLoadMore() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(25);

        dataCallback.onSuccess(mockList);

        assertTrue(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithLessThanPageSizeItemsItShouldNotBePossibleToLoadMore() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(24);

        dataCallback.onSuccess(mockList);

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithErrorItShouldNotBePossibleToLoadMore() {
        model.fetchFirstPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsIsSuccessfullyItShouldCallShowMoreAppsOnView() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(5);

        dataCallback.onSuccess(mockList);

        verify(presenter).onResultMoreApps(mockList);
//        verify(view).showMoreApps(mockList);
    }

    @Test
    public void givenFetchNextPageIsCalledForTheSecondTimeAndGetAppsIsSuccessfullyItShouldCallShowMoreAppsOnViewWithCorrectOffset() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList((int) PAGE_SIZE);

        dataCallback.onSuccess(mockList);

        verify(presenter).onResultMoreApps(mockList);
//        verify(view).showMoreApps(mockList);

        model.fetchNextPage();

        verify(repository).getApps(eq(PAGE_SIZE), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsReturnErrorItShouldNotCallShowMoreAppsOnView() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        verify(presenter, never()).onResultMoreApps(anyListOf(App.class));
//        verify(view, never()).showMoreApps(anyListOf(App.class));
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasNotReturnItShouldNotBePossibleToLoadMore() {
        model.fetchNextPage();

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithPageSizeItemsItShouldBePossibleToLoadMore() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(25);

        dataCallback.onSuccess(mockList);

        assertTrue(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithLessThanPageSizeItemsItShouldNotBePossibleToLoadMore() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(24);

        dataCallback.onSuccess(mockList);

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithErrorItShouldNotBePossibleToLoadMore() {
        model.fetchNextPage();

        verify(repository).getApps(eq(0), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        assertFalse(((AppListModelImpl) model).shouldLoadMore());
    }

    private List<App> getMockedAppList(int listSize) {
        List<App> mockedAppList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            mockedAppList.add(new App());
        }
        return mockedAppList;
    }
}
