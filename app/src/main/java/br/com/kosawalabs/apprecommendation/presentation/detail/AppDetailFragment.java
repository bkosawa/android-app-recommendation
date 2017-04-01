package br.com.kosawalabs.apprecommendation.presentation.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.AppDataRepository;
import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.visual.ImageLoaderFacade;

import static br.com.kosawalabs.apprecommendation.MainApplication.EXTRAS_SESSION_TOKEN;

public class AppDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    private CollapsingToolbarLayout appBarLayout;
    private TextView detail;
    private ImageView icon;
    private TextView category;
    private TextView developer;
    private Button downloadButtom;

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
                    setIcon();
                    setCategory();
                    setDeveloper();
                    setDownloadButton();
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
        icon = (ImageView) rootView.findViewById(R.id.detail_icon);
        category = (TextView) rootView.findViewById(R.id.detail_category);
        developer = (TextView) rootView.findViewById(R.id.detail_developer);
        downloadButtom = (Button) rootView.findViewById(R.id.detail_download_button);
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

    private void setIcon() {
        ImageLoaderFacade.loadImage(this, mItem.getIconUrl(), icon);
    }

    private void setCategory() {
        category.setText(mItem.getCategoryKey());
    }

    private void setDeveloper() {
        developer.setText(mItem.getDeveloperName());
    }

    private void setDownloadButton() {
        downloadButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getGooglePlayIntent(mItem.getPackageName()));
            }
        });
    }

    @NonNull
    private Intent getGooglePlayIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        return intent.resolveActivityInfo(getActivity().getPackageManager(), 0) != null ? intent : getGooglePlayWebIntent(packageName);
    }

    @NonNull
    public Intent getGooglePlayWebIntent(String packageName) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + packageName));
    }
}
