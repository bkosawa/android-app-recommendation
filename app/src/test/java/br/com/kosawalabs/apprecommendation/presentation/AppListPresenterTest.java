package br.com.kosawalabs.apprecommendation.presentation;

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
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static br.com.kosawalabs.apprecommendation.presentation.AppListPresenterImpl.PAGE_SIZE;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppListPresenterTest {

    @Mock
    private AppDataRepository repository;

    @Mock
    private AppListView view;

    @Captor
    private ArgumentCaptor<DataCallback<List<App>>> dataCallbackArgumentCaptor;

    private AppListPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);
        presenter = new AppListPresenterImpl(view, repository);
    }

    @Test
    public void givenFetchFirstPageIsCalledItShouldCallRepositoryWithZeroOffsetAndZeroLimit() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), any(DataCallback.class));
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsIsSuccessfullyItShouldCallShowAppsOnView() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(5);

        dataCallback.onSuccess(mockList);

        verify(view).showApps(mockList);
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsReturnErrorItShouldNotCallShowAppsOnView() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        verify(view, never()).showApps(anyListOf(App.class));
    }

    @Test
    public void givenFetchFirstPageIsNotCalledYetItShouldBePossibleToLoadMore() {
        assertTrue(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasNotReturnItShouldNotBePossibleToLoadMore() {
        presenter.fetchFirstPage();

        assertFalse(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithPageSizeItemsItShouldBePossibleToLoadMore() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(25);

        dataCallback.onSuccess(mockList);

        assertTrue(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithLessThanPageSizeItemsItShouldNotBePossibleToLoadMore() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(24);

        dataCallback.onSuccess(mockList);

        assertFalse(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsHasReturnWithErrorItShouldNotBePossibleToLoadMore() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        assertFalse(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsIsSuccessfullyItShouldCallShowMoreAppsOnView() {
        presenter.fetchNextPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(5);

        dataCallback.onSuccess(mockList);

        verify(view).showMoreApps(mockList);
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsReturnErrorItShouldNotCallShowMoreAppsOnView() {
        presenter.fetchNextPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        verify(view, never()).showMoreApps(anyListOf(App.class));
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasNotReturnItShouldNotBePossibleToLoadMore() {
        presenter.fetchNextPage();

        assertFalse(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithPageSizeItemsItShouldBePossibleToLoadMore() {
        presenter.fetchNextPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(25);

        dataCallback.onSuccess(mockList);

        assertTrue(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithLessThanPageSizeItemsItShouldNotBePossibleToLoadMore() {
        presenter.fetchNextPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        List<App> mockList = getMockedAppList(24);

        dataCallback.onSuccess(mockList);

        assertFalse(presenter.shouldLoadMore());
    }

    @Test
    public void givenFetchNextPageIsCalledAndGetAppsHasReturnWithErrorItShouldNotBePossibleToLoadMore() {
        presenter.fetchNextPage();

        verify(repository).getApps(eq(0L), eq(PAGE_SIZE), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onError(new DataError("Error"));

        assertFalse(presenter.shouldLoadMore());
    }

    private List<App> getMockedAppList(int listSize) {
        List<App> mockedAppList = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            mockedAppList.add(new App());
        }
        return mockedAppList;
    }
}