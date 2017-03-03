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
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppListPresenterTest {

    @Mock
    private AppDataRepository repository;

    @Mock
    private AppListView view;

    private List<App> mockList;

    @Captor
    private ArgumentCaptor<DataCallback> dataCallbackArgumentCaptor;

    private AppListPresenter presenter;

    @Before
    public void setup() {
        initMocks(this);
        mockList = getMockedAppList();
        presenter = new AppListPresenterImpl(view, repository);
    }

    @Test
    public void givenFetchFirstPageIsCalledItShouldCallRepositoryWithZeroOffsetAndZeroLimit() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(0L), any(DataCallback.class));
    }

    @Test
    public void givenFetchFirstPageIsCalledAndGetAppsIsSuccessfullyItShouldCallShowAppsOnView() {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(0L), dataCallbackArgumentCaptor.capture());

        DataCallback<List<App>> dataCallback = dataCallbackArgumentCaptor.getValue();

        dataCallback.onSuccess(mockList);

        verify(view).showApps(mockList);
    }

    public List<App> getMockedAppList() {
        List<App> mockedAppList = new ArrayList<>();
        mockedAppList.add(new App());
        mockedAppList.add(new App());
        mockedAppList.add(new App());
        mockedAppList.add(new App());
        return mockedAppList;
    }
}