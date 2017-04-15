package br.com.kosawalabs.apprecommendation.presentation.list;

import android.content.Context;

import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;

public class AppListInjector {
    public static AppListPresenterImpl inject(AppListView view, Context context) {
        AppListModelImpl model = new AppListModelImpl(
                new AppNetworkRepository(),
                new TokenDiskRepository(context));
        AppListPresenterImpl presenter = new AppListPresenterImpl(view, model);
        model.setPresenter(presenter);
        return presenter;
    }
}
