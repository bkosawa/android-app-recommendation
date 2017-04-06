package br.com.kosawalabs.apprecommendation.presentation.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import br.com.kosawalabs.apprecommendation.data.disk.TokenDiskRepository;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.detail.AppDetailActivity;
import br.com.kosawalabs.apprecommendation.presentation.detail.AppDetailFragment;
import br.com.kosawalabs.apprecommendation.presentation.login.LoginActivity;
import br.com.kosawalabs.apprecommendation.service.UploadMyAppsIService;
import br.com.kosawalabs.apprecommendation.visual.ImageLoaderFacade;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AppListActivity extends AppCompatActivity implements AppListView, View.OnClickListener {
    private boolean mTwoPane;
    private AppListPresenter presenter;
    private LinearLayoutManager layoutManager;
    private SimpleItemRecyclerViewAdapter listAdapter;
    private ProgressBar progress;
    private RecyclerView listFrame;
    private View errorFrame;
    private TextView errorDesc;
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
        listFrame.addOnScrollListener(new AppListOnScrollListener());

        if (findViewById(R.id.app_detail_container) != null) {
            mTwoPane = true;
        }

        progress = (ProgressBar) findViewById(R.id.progress_bar);
        errorFrame = findViewById(R.id.error_frame);
        errorDesc = (TextView) findViewById(R.id.list_error_description);
        sendDataFrame = findViewById(R.id.send_data_frame);
        sendDataButton = (Button) findViewById(R.id.send_data_button);

        sendDataButton.setOnClickListener(this);

        presenter = new AppListPresenterImpl(this, new AppNetworkRepository(), new TokenDiskRepository(getApplicationContext()));
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
                sendMyAppList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.init();
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_data_button:
                sendMyAppList();
                return;
        }
    }

    private boolean listIsAtTheEnd() {
        int visibleItemCount = getVisibleItemCount();
        int totalItemCount = getTotalItemCount();
        int firstVisibleItemPosition = getFirstVisibleItemPosition();

        return (visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= presenter.getPageSize();
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

    private void sendMyAppList() {
        Toast.makeText(this, R.string.toast_sending_packages, Toast.LENGTH_SHORT).show();
        UploadMyAppsIService.startActionUploadApps(this);
    }

    public class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<App> apps;

        public SimpleItemRecyclerViewAdapter(List<App> apps) {
            this.apps = apps;
        }

        public void setApps(List<App> moreApps) {
            this.apps.addAll(moreApps);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.app_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            App app = this.apps.get(position);
            holder.mItem = app;
            ImageLoaderFacade.loadImage(AppListActivity.this, app.getIconUrl(), holder.mIcon);
            holder.mName.setText(app.getName());
            holder.mDeveloper.setText(app.getDeveloperName().toUpperCase());
            holder.mCategory.setText(app.getCategoryName().toUpperCase());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(AppDetailFragment.ARG_ITEM_APP, holder.mItem);
                        AppDetailFragment fragment = new AppDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.app_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AppDetailActivity.class);
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(AppDetailFragment.ARG_ITEM_APP, holder.mItem);
                        intent.putExtras(arguments);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return this.apps.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            private final ImageView mIcon;
            public final TextView mName;
            private final TextView mDeveloper;
            private final TextView mCategory;

            public App mItem;

            public ViewHolder(View view) {
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

    }

    private class AppListOnScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            if (listIsAtTheEnd()) {
                presenter.loadMore();
            }
        }

    }

    private class MyBottomNavListener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_list:
                    presenter.setRecommended(false);
                    break;
                case R.id.action_list_recommended:
                    presenter.setRecommended(true);
                    break;
                default:
                    return false;
            }
            presenter.refreshList();
            return true;
        }

    }
}
