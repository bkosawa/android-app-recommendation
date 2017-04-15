package br.com.kosawalabs.apprecommendation.presentation.list;

import org.junit.Before;
import org.mockito.Mock;

import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListModel;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;

import static org.mockito.MockitoAnnotations.initMocks;

public class AppListPresenterTest {

    @Mock
    private AppListView view;

    @Mock
    private AppListModel model;

    private AppListPresenterImpl presenter;

    @Before
    public void setup() {
        initMocks(this);
        presenter = new AppListPresenterImpl(view, model);
    }

}