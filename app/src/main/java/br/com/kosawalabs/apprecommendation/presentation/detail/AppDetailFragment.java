package br.com.kosawalabs.apprecommendation.presentation.detail;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.kosawalabs.apprecommendation.R;
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

    public AppDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = DetailInjector.inject(this, getContext().getApplicationContext());
        presenter.init(getAppFromBundle());
    }

    private App getAppFromBundle() {
        return getArguments().containsKey(ARG_ITEM_APP) ?
                (App) getArguments().getParcelable(ARG_ITEM_APP) : null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Activity activity = this.getActivity();
        appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        backdrop = (ImageView) activity.findViewById(R.id.detail_backdrop);

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
        presenter.onViewIsReady();
    }

    @Override
    public void setAppBasicInfo(final App app) {
        if (appBarLayout != null) {
            appBarLayout.setTitle(app.getName());
        }
        if (backdrop != null) {
            ImageLoaderFacade.loadImage(this, app.getIconUrl(), backdrop);
        }
        ImageLoaderFacade.loadImage(this, app.getIconUrl(), icon);
        category.setText(app.getCategoryName().toUpperCase());
        developer.setText(app.getDeveloperName().toUpperCase());
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(getGooglePlayIntent(app.getPackageName()));
            }
        });
    }

    @Override
    public void setAppComplementaryInfo(App app) {
        detail.setText(app.getDescription());
    }
    
    @Override
    public void closeView() {
        if (getActivity() != null)
            getActivity().finish();
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
