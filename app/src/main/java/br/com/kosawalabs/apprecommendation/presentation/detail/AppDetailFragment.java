package br.com.kosawalabs.apprecommendation.presentation.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import br.com.kosawalabs.apprecommendation.data.TokenDataRepository;
import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailPresenter;
import br.com.kosawalabs.apprecommendation.presentation.detail.contract.DetailView;
import br.com.kosawalabs.apprecommendation.visual.ImageLoaderFacade;

public class AppDetailFragment extends Fragment implements DetailView {

    private DetailPresenter.DetailPresenterFromView presenter;

    public static final String ARG_ITEM_APP = "item_app";

    private CollapsingToolbarLayout appBarLayout;
    private TextView detail;
    private ImageView icon;
    private TextView category;
    private TextView developer;
    private Button downloadButton;
    private ImageView backdrop;

    private AppDataRepository repository;

    private App mItem;

    public AppDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        backdrop = (ImageView) activity.findViewById(R.id.detail_backdrop);

        TokenDataRepository tokenRepository = new TokenDiskRepository(getContext().getApplicationContext());
        String token = tokenRepository.getToken();
        if (TextUtils.isEmpty(token)) {
            getActivity().finish();
            return;
        }
        repository = new AppNetworkRepository(token);

        if (getArguments().containsKey(ARG_ITEM_APP)) {
            mItem = getArguments().getParcelable(ARG_ITEM_APP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.app_detail, container, false);
        detail = (TextView) rootView.findViewById(R.id.app_detail);
        icon = (ImageView) rootView.findViewById(R.id.detail_icon);
        category = (TextView) rootView.findViewById(R.id.detail_category);
        developer = (TextView) rootView.findViewById(R.id.detail_developer);
        downloadButton = (Button) rootView.findViewById(R.id.detail_download_button);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mItem != null) {
            setAppBasicInfo();
            repository.getApp(mItem.getId(), new DataCallback<App>() {
                @Override
                public void onSuccess(App result) {
                    mItem = result;
                    setAppComplementaryInfo();
                }

                @Override
                public void onError(DataError error) {
                    Log.d("TEST", String.valueOf(error));
                }
            });
        }
    }

    private void setAppBasicInfo() {
        setTitle();
        setIcon();
        setBackdrop();
        setCategory();
        setDeveloper();
        setDownloadButton();
    }

    private void setAppComplementaryInfo() {
        setDescription();
    }

    private void setTitle() {
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getName());
        }
    }

    private void setDescription() {
        detail.setText(mItem.getDescription());
    }

    private void setIcon() {
        ImageLoaderFacade.loadImage(this, mItem.getIconUrl(), icon);
    }

    private void setBackdrop() {
        if (backdrop != null) {
            ImageLoaderFacade.loadImage(this, mItem.getIconUrl(), backdrop);
        }
    }

    private void setCategory() {
        category.setText(mItem.getCategoryName().toUpperCase());
    }

    private void setDeveloper() {
        developer.setText(mItem.getDeveloperName().toUpperCase());
    }

    private void setDownloadButton() {
        downloadButton.setOnClickListener(new View.OnClickListener() {
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
