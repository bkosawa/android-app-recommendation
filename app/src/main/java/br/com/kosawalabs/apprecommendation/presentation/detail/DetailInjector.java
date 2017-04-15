package br.com.kosawalabs.apprecommendation.presentation.detail;


import android.content.Context;

import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailView;

public class DetailInjector {
    public static DetailPresenterImpl inject(DetailView view, Context context) {
        DetailModelImpl model = new DetailModelImpl(new AppNetworkRepository(), new TokenDiskRepository(context));
        DetailPresenterImpl presenter = new DetailPresenterImpl(view, model);
        model.setPresenter(presenter);
        return presenter;
    }
}
