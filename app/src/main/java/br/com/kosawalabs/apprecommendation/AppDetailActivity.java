package br.com.kosawalabs.apprecommendation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static br.com.kosawalabs.apprecommendation.MainApplication.EXTRAS_SESSION_TOKEN;

public class AppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        setupActionBar();

        if (savedInstanceState == null) {
            Bundle receivedArguments = getIntent().getExtras();
            String token = receivedArguments.getString(EXTRAS_SESSION_TOKEN);
            int appId = receivedArguments.getInt(AppDetailFragment.ARG_ITEM_ID, -1);

            addDetailFragment(token, appId);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, AppListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void addDetailFragment(String token, int appId) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRAS_SESSION_TOKEN, token);
        arguments.putInt(AppDetailFragment.ARG_ITEM_ID, appId);
        AppDetailFragment fragment = new AppDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.app_detail_container, fragment)
                .commit();
    }
}
