package br.com.kosawalabs.apprecommendation;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static br.com.kosawalabs.apprecommendation.AppListActivity.EXTRAS_SESSION_TOKEN;

public class AppDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private CollapsingToolbarLayout appBarLayout;
    private TextView detail;

    private AppDataRepository repository;

    private App mItem;

    public AppDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID) && getArguments().containsKey(EXTRAS_SESSION_TOKEN)) {
            String token = getArguments().getString(EXTRAS_SESSION_TOKEN);
            int appId = getArguments().getInt(ARG_ITEM_ID);
            repository = new AppNetworkRepository(token);
            repository.getApp(appId, new DataCallback<App>() {
                @Override
                public void onSuccess(App result) {
                    mItem = result;
                    setTitle();
                    setDescription();
                }

                @Override
                public void onError(DataError error) {
                    Log.d("TEST", String.valueOf(error));
                }
            });
        }

        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_detail, container, false);
        detail = (TextView) rootView.findViewById(R.id.app_detail);
        return rootView;
    }

    private void setTitle() {
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getName());
        }
    }

    private void setDescription() {
        detail.setText(mItem.getName());
    }
}
