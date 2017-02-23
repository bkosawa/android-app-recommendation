package br.com.kosawalabs.apprecommendation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.kosawalabs.apprecommendation.data.DataCallback;
import br.com.kosawalabs.apprecommendation.data.DataError;
import br.com.kosawalabs.apprecommendation.data.network.AppNetworkRepository;
import br.com.kosawalabs.apprecommendation.data.pojo.App;

import static br.com.kosawalabs.apprecommendation.dummy.DummyContent.ITEM_MAP;

public class AppListActivity extends AppCompatActivity {

    public static final String EXTRAS_SESSION_TOKEN = "br.com.kosawalabs.apprecommendation.extras.SESSION_TOKEN";
    private boolean mTwoPane;
    private String token;

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

        final View recyclerView = findViewById(R.id.app_list);
        assert recyclerView != null;

        if (findViewById(R.id.app_detail_container) != null) {
            mTwoPane = true;
        }

        token = getIntent().getStringExtra(EXTRAS_SESSION_TOKEN);

        AppNetworkRepository networkRepository = new AppNetworkRepository(token);
        networkRepository.getApps(0L, 0L, new DataCallback<List<App>>() {
            @Override
            public void onSuccess(List<App> result) {
                setupRecyclerView((RecyclerView) recyclerView, result);
            }

            @Override
            public void onError(DataError error) {
                Log.d("TEST", error.getCause());
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<App> apps) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(apps));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<App> apps;

        public SimpleItemRecyclerViewAdapter(List<App> apps) {
            this.apps = apps;
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
}
