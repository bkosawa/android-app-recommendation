package br.com.kosawalabs.apprecommendation.presentation.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.detail.AppDetailActivity;
import br.com.kosawalabs.apprecommendation.presentation.detail.AppDetailFragment;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListPresenter;
import br.com.kosawalabs.apprecommendation.presentation.list.contract.AppListView;
import br.com.kosawalabs.apprecommendation.presentation.login.LoginActivity;
import br.com.kosawalabs.apprecommendation.service.UploadMyAppsIService;
import br.com.kosawalabs.apprecommendation.visual.ImageLoaderFacade;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AppListActivity extends AppCompatActivity implements AppListView, View.OnClickListener {
    private boolean mTwoPane;

    private AppListPresenter.AppListPresenterFromView presenter;

    private LinearLayoutManager layoutManager;
    private SimpleItemRecyclerViewAdapter listAdapter;

    private ProgressBar progress;
    private RecyclerView listFrame;
    private View errorFrame;
    private TextView errorDesc;
    private Button tryAgainButton;
    private View sendDataFrame;
    private Button sendDataButton;

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, AppListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new MyBottomNavListener());

        listFrame = (RecyclerView) findViewById(R.id.app_list);
        assert listFrame != null;

        layoutManager = new LinearLayoutManager(this);
        listFrame.setLayoutManager(layoutManager);
        listFrame.addOnScrollListener(new AppListOnScrollListener((int) presenter.getPageSize()));

        if (findViewById(R.id.app_detail_container) != null) {
            mTwoPane = true;
        }

        progress = (ProgressBar) findViewById(R.id.progress_bar);
        errorFrame = findViewById(R.id.error_frame);
        errorDesc = (TextView) findViewById(R.id.list_error_description);
        tryAgainButton = (Button) findViewById(R.id.try_again_button);
        sendDataFrame = findViewById(R.id.send_data_frame);
        sendDataButton = (Button) findViewById(R.id.send_data_button);

        sendDataButton.setOnClickListener(this);
        tryAgainButton.setOnClickListener(this);

        presenter = AppListInjector.inject(this, getApplicationContext());
        presenter.init();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_my_apps:
                presenter.onSendDataButtonClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showApps(List<App> apps) {
        progress.setVisibility(GONE);
        errorFrame.setVisibility(GONE);
        sendDataFrame.setVisibility(GONE);
        listFrame.setVisibility(VISIBLE);
        listAdapter = new SimpleItemRecyclerViewAdapter(apps);
        listFrame.setAdapter(listAdapter);
    }

    @Override
    public void showMoreApps(List<App> apps) {
        progress.setVisibility(GONE);
        errorFrame.setVisibility(GONE);
        sendDataFrame.setVisibility(GONE);
        listFrame.setVisibility(VISIBLE);
        listAdapter.setApps(apps);
    }

    @Override
    public void showError(String errorCause) {
        progress.setVisibility(GONE);
        listFrame.setVisibility(GONE);
        sendDataFrame.setVisibility(GONE);
        errorFrame.setVisibility(VISIBLE);
        errorDesc.setText(errorCause);
    }

    @Override
    public void showSendDataButton() {
        progress.setVisibility(GONE);
        listFrame.setVisibility(GONE);
        errorFrame.setVisibility(GONE);
        sendDataFrame.setVisibility(VISIBLE);
    }

    @Override
    public void showLogin() {
        LoginActivity.startWithLogout(this);
        Toast.makeText(getApplicationContext(), R.string.toast_logout, Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void showLoading() {
        progress.setVisibility(VISIBLE);
        listFrame.setVisibility(GONE);
        errorFrame.setVisibility(GONE);
        sendDataFrame.setVisibility(GONE);
    }

    @Override
    public void showSendingAppsMessage() {
        Toast.makeText(this, R.string.toast_sending_packages, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startActionUploadApps() {
        UploadMyAppsIService.startActionUploadApps(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_data_button:
                presenter.onSendDataButtonClicked();
                return;
            case R.id.try_again_button:
                presenter.onTryAgainButtonClicked();
                return;
        }
    }

    private int getVisibleItemCount() {
        return layoutManager.getChildCount();
    }

    private int getTotalItemCount() {
        return layoutManager.getItemCount();
    }

    private int getFirstVisibleItemPosition() {
        return layoutManager.findFirstVisibleItemPosition();
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final int VIEW_ITEM = 1;
        private final int VIEW_PROGRESS = 0;

        private final List<App> apps;

        public SimpleItemRecyclerViewAdapter(List<App> apps) {
            this.apps = apps;
        }

        public void setApps(List<App> moreApps) {
            this.apps.addAll(moreApps);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            return position != apps.size() ? VIEW_ITEM : VIEW_PROGRESS;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_ITEM) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_list_content, parent, false);
                return new CardViewHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_list_progress, parent, false);
                return new ProgressViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (holder instanceof CardViewHolder) {
                final CardViewHolder cardHolder = (CardViewHolder) holder;
                App app = this.apps.get(position);
                cardHolder.mItem = app;
                ImageLoaderFacade.loadImage(AppListActivity.this, app.getIconUrl(), cardHolder.mIcon);
                cardHolder.mName.setText(app.getName());
                cardHolder.mDeveloper.setText(app.getDeveloperName().toUpperCase());
                cardHolder.mCategory.setText(app.getCategoryName().toUpperCase());

                cardHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(AppDetailFragment.ARG_ITEM_APP, cardHolder.mItem);
                            AppDetailFragment fragment = new AppDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.app_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, AppDetailActivity.class);
                            Bundle arguments = new Bundle();
                            arguments.putParcelable(AppDetailFragment.ARG_ITEM_APP, cardHolder.mItem);
                            intent.putExtras(arguments);
                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(AppListActivity.this, cardHolder.mIcon, "app_icon");
                            intent.putExtras(arguments);
                            startActivity(intent, options.toBundle());
                        }
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return presenter.hasStopLoading() ? this.apps.size() : this.apps.size() + 1;
        }

        public abstract class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }

        public class CardViewHolder extends ViewHolder {
            public final View mView;
            public final ImageView mIcon;
            public final TextView mName;
            public final TextView mDeveloper;
            public final TextView mCategory;

            public App mItem;

            public CardViewHolder(View view) {
                super(view);
                mView = view;
                mIcon = (ImageView) view.findViewById(R.id.list_icon);
                mName = (TextView) view.findViewById(R.id.list_name);
                mDeveloper = (TextView) view.findViewById(R.id.list_developer);
                mCategory = (TextView) view.findViewById(R.id.list_category);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mName.getText() + "'";
            }
        }

        public class ProgressViewHolder extends ViewHolder {
            public ProgressViewHolder(View itemView) {
                super(itemView);
            }
        }

    }

    private class AppListOnScrollListener extends RecyclerView.OnScrollListener {

        private final int pageSize;

        public AppListOnScrollListener(int pageSize) {
            this.pageSize = pageSize;
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (listIsAtTheEnd()) {
                presenter.onListScrolledToTheEnd();
            }
        }

        private boolean listIsAtTheEnd() {
            int visibleItemCount = getVisibleItemCount();
            int totalItemCount = getTotalItemCount();
            int firstVisibleItemPosition = getFirstVisibleItemPosition();

            return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= pageSize;
        }

    }

    private class MyBottomNavListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_list:
                    presenter.onAvailableListClicked();
                    break;
                case R.id.action_list_recommended:
                    presenter.onRecommendedListClicked();
                    break;
                default:
                    return false;
            }
            return true;
        }

    }
}
