package br.com.kosawalabs.apprecommendation.presentation.list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListModel;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListPresenter;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppListPresenterTest {

    private AppListPresenter.AppListPresenterFromView presenterFromView;

    private AppListPresenter.AppListPresenterFromModel presenterFromModel;

    @Mock
    private AppListView view;

    @Mock
    private AppListModel model;

    @Before
    public void setup() {
        initMocks(this);
        AppListPresenterImpl presenter = new AppListPresenterImpl(view, model);
        this.presenterFromView = presenter;
        this.presenterFromModel = presenter;
    }

    @Test
    public void givenInitIsCalledAndUserIsLoggedItShouldCallFetchFirstPage() throws Exception {
        when(model.isLogged()).thenReturn(true);

        presenterFromView.init();

        verify(model).fetchFirstPage();
        verify(view, never()).showLogin();
    }

    @Test
    public void givenInitIsCalledAndUserIsLoggedItShouldCallShowLogin() throws Exception {
        when(model.isLogged()).thenReturn(false);

        presenterFromView.init();

        verify(view).showLogin();
        verify(model, never()).fetchFirstPage();
    }
}