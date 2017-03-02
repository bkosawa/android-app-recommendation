package br.com.kosawalabs.apprecommendation.presentation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppListPresenterTest {

    @Mock
    private AppDataRepository repository;

    private AppListPresenter presenter;

    @Before
    public void setup(){
        initMocks(this);
        presenter = new AppListPresenter(repository);
    }

    @Test
    public void givenFetchFirstPageIsCalledItShouldCallRepositoryWithZeroOffsetAndZeroLimit() throws Exception {
        presenter.fetchFirstPage();

        verify(repository).getApps(eq(0L), eq(0L), any(DataCallback.class));
    }

}