package br.com.kosawalabs.apprecommendation.presentation.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import br.com.kosawalabs.apprecommendation.R;
import br.com.kosawalabs.apprecommendation.data.pojo.App;
import br.com.kosawalabs.apprecommendation.presentation.list.AppListActivity;

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
            App app = receivedArguments.getParcelable(AppDetailFragment.ARG_ITEM_APP);
            addDetailFragment(app);
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

    private void addDetailFragment(App app) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(AppDetailFragment.ARG_ITEM_APP, app);
        AppDetailFragment fragment = new AppDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.app_detail_container, fragment)
                .commit();
    }
}
