package br.com.kosawalabs.apprecommendation.presentation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.kosawalabs.apprecommendation.AppDetailActivity;
import br.com.kosawalabs.apprecommendation.AppDetailFragment;
import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static br.com.kosawalabs.apprecommendation.MainApplication.EXTRAS_SESSION_TOKEN;

public class AppListActivity extends AppCompatActivity implements AppListView {
    private boolean mTwoPane;
    private RecyclerView recyclerView;
    private AppListPresenterImpl presenter;
    private String token;
    private LinearLayoutManager layoutManager;
    private SimpleItemRecyclerViewAdapter listAdapter;

    public static void start(Activity activity, String token) {
        Intent intent = new Intent(activity, AppListActivity.class);
        intent.putExtra(EXTRAS_SESSION_TOKEN, token);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        recyclerView = (RecyclerView) findViewById(R.id.app_list);
        assert recyclerView != null;

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new AppListOnScrollListener());

        if (findViewById(R.id.app_detail_container) != null) {
            mTwoPane = true;
        }

        token = getIntent().getStringExtra(EXTRAS_SESSION_TOKEN);
        presenter = new AppListPresenterImpl(this, new AppNetworkRepository(token));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.fetchFirstPage();
    }

    @Override
    public void showApps(List<App> apps) {
        listAdapter = new SimpleItemRecyclerViewAdapter(apps);
        recyclerView.setAdapter(listAdapter);
    }

    @Override
    public void showMoreApps(List<App> apps) {
        listAdapter.setApps(apps);
    }

    @Override
    public int getVisibleItemCount() {
        return layoutManager.getChildCount();
    }

    @Override
    public int getTotalItemCount() {
        return layoutManager.getItemCount();
    }

    @Override
    public int getFirstVisibleItemPosition() {
        return layoutManager.findFirstVisibleItemPosition();
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
            holder.mItem = this.apps.get(position);
            holder.mIdView.setText(String.valueOf(this.apps.get(position).getId()));
            holder.mContentView.setText(this.apps.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putInt(AppDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        arguments.putString(EXTRAS_SESSION_TOKEN, token);
                        AppDetailFragment fragment = new AppDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.app_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, AppDetailActivity.class);
                        Bundle arguments = new Bundle();
                        arguments.putInt(AppDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        arguments.putString(EXTRAS_SESSION_TOKEN, token);
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
            public final TextView mIdView;
            public final TextView mContentView;
            public App mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
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

            if (presenter.shouldLoadMore()) {
                if (presenter.listIsAtTheEnd()) {
                    presenter.fetchNextPage();
                }
            }
        }
    }
}
